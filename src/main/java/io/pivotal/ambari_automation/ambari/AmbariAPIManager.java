package io.pivotal.ambari_automation.ambari;

import io.pivotal.ambari_automation.AmbariAutomationRuntimeException;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;
import io.pivotal.ambari_automation.ambari.restapi.v1.BlueprintApi;
import io.pivotal.ambari_automation.ambari.restapi.v1.BootStrapApi;
import io.pivotal.ambari_automation.ambari.restapi.v1.ClustersApi;
import io.pivotal.ambari_automation.ambari.restapi.v1.HostApi;
import io.pivotal.ambari_automation.ambari.restapi.v1.HostComponentApi;
import io.pivotal.ambari_automation.ambari.restapi.v1.ServiceApi;
import io.pivotal.ambari_automation.ambari.restapi.v1.StacksApi;
import io.pivotal.ambari_automation.conf.Topology;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.log4testng.Logger;

/**
 * Ambari Rest API wrapper
 * 
 * @author Hao Zou
 */
public final class AmbariAPIManager {
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger.getLogger(AmbariAPIManager.class);
	
	private String host;
	private int port;
	private String user;
	private String password;
	
	public AmbariAPIManager(String host, int port, String user, String password){
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	public <T extends BaseApi> T getApi(Class<? extends BaseApi> clazz){
		Constructor<? extends BaseApi> constructor;
		try {
			constructor = clazz.getConstructor(String.class, int.class, String.class, String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new AmbariAutomationRuntimeException(e.getMessage(), e);
		}

		try {
			return (T) constructor.newInstance(host, port, user, password);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new AmbariAutomationRuntimeException(e.getMessage(), e);
		}

	}

	/**
	 * deploy the ambari cluster 
	 * @param clusterName cluster name of the cluster
	 * @param blueprintName blueprintName to create
	 * @param bluePrint blueprint content
	 * @param hostMapping hostmapping content
	 * @return
	 * @throws Exception
	 */
	public int delopyHadoop(String clusterName, String blueprintName, String blueprint, String hostMapping) throws Exception {
		log.debug("Deploying the cluster on " + this.host + " :");
		
		AmbariAutomationResponse resp;
		BlueprintApi blueprintApi = getApi(BlueprintApi.class);

		log.debug("uploading blueprint to " + this.host);
		resp = blueprintApi.uploadBlueprint(blueprintName, blueprint);
		
		if (resp.getStatusCode() == 201) {
			log.info("blueprint uploaded");
		} else if (resp.getStatusCode() == 409) {
			log.warn(resp.getBody());
		} else {
			throw new Exception(resp.getBody());
		}
		ClustersApi clustersApi = getApi(ClustersApi.class);
		
		log.debug("creating cluster with hostmapping in " + this.host);
		resp = clustersApi.createClusterWithHostMapping(clusterName, hostMapping);
		if (resp.getStatusCode() == 202) {
			log.debug("response.getBody(): " + resp.getBody());
		}
		else if (resp.getStatusCode() == 409) {
			log.warn(resp.getBody());
			return 1;
		} else {
			throw new Exception(resp.getBody());
		}
		
		int requestId = resp.getBodyIntValueByJsonPath("Requests.id");
		boolean flag = false;
		try {
			clustersApi.trackStatus(clusterName, requestId);
			flag = true;
		} catch (Exception e) {
			if (e.getMessage().contains("tasks aborted")) {
				log.warn("tasks aborted, trying to restart all");
			} else {
				throw new Exception(e);
			}
		}
		if (flag == false) {
			ServiceApi serviceApi = this.getApi(ServiceApi.class);
			while (true) {
				try {
					serviceApi.startAll(clusterName);
					break;
				} catch (Exception e) {
					if (e.getMessage().contains("tasks aborted")) {
						log.warn("tasks aborted, trying to restart all");
					} else {
						throw new Exception(e);
					}
				}
			}
		}
		log.info("finished deployment");
		return 0;

	}
	/**
	 * update stack repo
	 * @param stackName
	 * @param stackVersion
	 * @param os
	 * @param repoName
	 * @param stackRepoURL
	 * @throws Exception
	 */
	public void updateStackRepo(String stackName, String stackVersion, String os, String repoName, String stackRepoURL) throws Exception {
		log.info("using " + stackRepoURL + " for " + repoName);
		StacksApi stacksApi = this.getApi(StacksApi.class);
		AmbariAutomationResponse resp = stacksApi.putStack(stackName, stackVersion, os, repoName, stackRepoURL);
		if (resp.getStatusCode() != 200) {
			throw new Exception(resp.getBody());
		}
	}
	/**
	 * get repo ids
	 * @param stackName
	 * @param stackVersion
	 * @param os
	 * @return list of repo ids
	 * @throws Exception
	 */
	public List<String> getRepoId(String stackName, String stackVersion, String os) throws Exception {
		log.info("getting repo ids for " + stackName + "-" + stackVersion);
		StacksApi stacksApi = this.getApi(StacksApi.class);
		AmbariAutomationResponse resp = stacksApi.getStack(stackName, stackVersion, os);
		if (resp.getStatusCode() != 200) {
			throw new Exception(resp.getBody());
		}
		return resp.getBodyListByJsonPath("items.Repositories.repo_id");
	}
	/**
	 * provision the agents
	 * @param sshKey
	 * @param agents
	 * @param sshUser
	 * @throws Exception
	 */
	public void provisionAgents(String sshKey, AmbariMachineGroup agents, String sshUser)
			throws Exception {
		log.info("provision agents on " + agents);
		BootStrapApi bootStrap = this.getApi(BootStrapApi.class);
		
		AmbariAutomationResponse resp = bootStrap.bootstrapPost(sshKey.replaceAll("\n", "\\\\n"), agents.toString(), sshUser, true);
			
		if (resp.getStatusCode() != 200) {
			throw new Exception(resp.getBody());
		}
		log.info(resp.getBody());
		int requestId = resp.getBodyIntValueByJsonPath("requestId");
		
		Map<String, Boolean> done = new HashMap<>();
		while (true) {
			resp = bootStrap.getBootStrapStatus(requestId);
			List<HashMap<String, String>> hosts = resp.getBodyListByJsonPath("hostsStatus");
			for (HashMap<String, String> host : hosts) {
				if ("DONE".equals(host.get("status")) && !done.containsKey(host.get("hostName"))) {
					done.put(host.get("hostName"), true);
					log.info(host.get("hostName") + ":" + "DONE");
				}
			}
			if ("ERROR".equals(resp
					.getBodyStringValueByJsonPath("status"))) {
				throw new Exception(resp.getBodyListByJsonPath("hostsStatus.log").toString());
			} else if ("SUCCESS".equals(resp
					.getBodyStringValueByJsonPath("status"))) {
				break;
			}
			Thread.sleep(2000);
		}
	}
	/**
	 * remove slave
	 * @param clusterName
	 * @param hostName
	 * @param topology
	 * @throws Exception
	 */
	public void removeSlave(String clusterName, String hostName, Topology topology) throws Exception {
		log.info("removing slave on " + hostName);
		HostComponentApi hostComponentApi = this.getApi(HostComponentApi.class);
		hostComponentApi.stopAllHostComponents(clusterName, hostName);
		hostComponentApi.removeAllHostComponents(clusterName, hostName);
		HostApi hostApi = this.getApi(HostApi.class);
		log.info("deleting " + hostName);
		AmbariAutomationResponse resp = hostApi.deleteHost(clusterName, hostName);
		if (resp.getStatusCode() != 200) {
			throw new Exception(resp.getBody());
		}
		topology.removeAgent(hostName);
		ServiceApi serviceApi = this.getApi(ServiceApi.class);
		serviceApi.stopAllServices(clusterName);
		serviceApi.startAllServices(clusterName);
	}
	/**
	 * add slave
	 * @param clusterName
	 * @param hostName
	 * @param components
	 * @param topology
	 * @throws Exception
	 */
	public void addSlave(String clusterName, String hostName, Set<String> components, Topology topology) throws Exception {
		log.info("adding slave on " + hostName);
		
		HostApi hostApi = this.getApi(HostApi.class);
		log.info("adding " + hostName);
		AmbariAutomationResponse resp = hostApi.createHost(clusterName, hostName);
		if (resp.getStatusCode() != 201) {
			throw new Exception(resp.getBody());
		}
		HostComponentApi hostComponentApi = this.getApi(HostComponentApi.class);
		for (String component : components) {
			hostComponentApi.addHostComponent(clusterName, hostName, component);
		}
		hostComponentApi.installAllHostComponent(clusterName, hostName);
		topology.addAgent(hostName);
		for (String component : components) {
			topology.addRole(hostName, component);
		}
		ServiceApi serviceApi = this.getApi(ServiceApi.class);
		serviceApi.stopAllServices(clusterName);
		serviceApi.startAllServices(clusterName);
	}
	/**
	 * wait until ambari server actually started
	 * @throws Exception
	 */
	public void waitUntilServerStarted() throws Exception {
		ClustersApi clusterApi = this.getApi(ClustersApi.class);
		int count = 0;
		while (true) {
			Thread.sleep(2000);
			try {
				clusterApi.listCluster();
				break;
			} catch (Exception e) {
				if (e.getMessage().contains("Connection refused") && count++ < 600) {
					continue;
				} else {
					throw new Exception(e);
				}
			}
		}
	}
}
