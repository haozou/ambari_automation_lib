package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpDelete;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPut;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.testng.log4testng.Logger;

public class HostComponentApi extends BaseApi {
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger.getLogger(HostComponentApi.class);
	public HostComponentApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	
	/**
	 * Returns a collection of components running on a given host.
	 * @param clusterName
	 * @param hostName
	 * @return
	 */
	public AmbariAutomationResponse listHostComponents(String clusterName, String hostName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s/host_components", clusterName, hostName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Returns information for a specific role on the given host.
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @return
	 */
	public AmbariAutomationResponse viewHostComponentInfo(String clusterName, String hostName, String componentName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s/host_components/%s", 
				clusterName, hostName, componentName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Create the host component named ":componentName" on the host named ":hostName".
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @return
	 */
	public AmbariAutomationResponse createHostComponent(String clusterName, String hostName, String componentName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s/host_components/%s", 
				clusterName, hostName, componentName));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Delete the host component named ":componentName" on the host named ":hostName".
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @return
	 */
	public AmbariAutomationResponse deleteHostComponent(String clusterName, String hostName, String componentName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s/host_components/%s", 
				clusterName, hostName, componentName));
		AmbariAutomationHttpDelete request = new AmbariAutomationHttpDelete(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Delete all components on the host named ":hostName".
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @return
	 */
	public AmbariAutomationResponse deleteHostComponents(String clusterName, String hostName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s/host_components", 
				clusterName, hostName));
		AmbariAutomationHttpDelete request = new AmbariAutomationHttpDelete(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Update a host component for a given host and component.
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @param newState
	 * @return
	 */
	public AmbariAutomationResponse updateHostComponent(String clusterName, String hostName, String componentName, String newState) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s/host_components/%s", 
				clusterName, hostName, componentName));
		String body = String.format("{\"HostRoles\":{\"state\":\"%s\"}}", newState);
		AmbariAutomationHttpPut request = new AmbariAutomationHttpPut(user, password, uri);
		StringEntity entity = null;
		try {
			entity = new StringEntity(body);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * update all the components on specified host
	 * @param clusterName
	 * @param hostName
	 * @param orgState
	 * @param newState
	 * @return
	 */
	public AmbariAutomationResponse updateHostComponents(String clusterName, String hostName, String orgState, String newState) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s/host_components?HostRoles/state=%s", 
				clusterName, hostName, orgState));
		String body = String.format("{\"HostRoles\":{\"state\":\"%s\"}}", newState);
		AmbariAutomationHttpPut request = new AmbariAutomationHttpPut(user, password, uri);
		StringEntity entity = null;
		try {
			entity = new StringEntity(body);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * start component in specified host 
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @throws Exception
	 */
	public void startHostComponent(String clusterName, String hostName, String componentName) throws Exception {
		log.info("starting the " + componentName + " on " + hostName + "...");
		AmbariAutomationResponse resp = this.updateHostComponent(clusterName, hostName, componentName, "STARTED");
		if (resp.getStatusCode() == 200) {
			log.info("component already started");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
	/**
	 * stop component in specified host 
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @throws Exception
	 */
	public void stopHostComponent(String clusterName, String hostName, String componentName) throws Exception {
		log.info("stopping the " + componentName + " on " + hostName + "...");
		AmbariAutomationResponse resp = this.updateHostComponent(clusterName, hostName, componentName, "INSTALLED");
		if (resp.getStatusCode() == 200) {
			log.info("component already stopped");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
	/**
	 * install component in specified host
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @throws Exception
	 */
	public void installHostComponent(String clusterName, String hostName, String componentName) throws Exception {
		log.info("installing the " + componentName + " on " + hostName + "...");
		AmbariAutomationResponse resp = this.updateHostComponent(clusterName, hostName, componentName, "INSTALLED");
		if (resp.getStatusCode() == 200) {
			log.info("component already installed");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
	/**
	 * install all components in specified host
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @throws Exception
	 */
	public void installAllHostComponent(String clusterName, String hostName) throws Exception {
		log.info("installing all the components on " + hostName + "...");
		AmbariAutomationResponse resp = this.updateHostComponents(clusterName, hostName, "INIT", "INSTALLED");
		if (resp.getStatusCode() == 200) {
			log.info("component already installed");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
	/**
	 * add component in specified host
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @throws Exception 
	 */
	public void addHostComponent(String clusterName, String hostName, String componentName) throws Exception {
		log.info("add the " + componentName + " on " + hostName + "...");
		AmbariAutomationResponse resp = this.createHostComponent(clusterName, hostName, componentName);
		if (resp.getStatusCode() != 201) {
			throw new Exception(resp.getBody());
		}
	}
	/**
	 * remove the component in specified host
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @throws Exception 
	 */
	public void removeHostComponent(String clusterName, String hostName, String componentName) throws Exception {
		log.info("removing the " + componentName + " on " + hostName + "...");
		AmbariAutomationResponse resp = this.deleteHostComponent(clusterName, hostName, componentName);
		if (resp.getStatusCode() != 200) {
			throw new Exception(resp.getBody());
		}
	}
	/**
	 * remove all components in specified host
	 * @param clusterName
	 * @param hostName
	 * @param componentName
	 * @throws Exception 
	 */
	public void removeAllHostComponents(String clusterName, String hostName) throws Exception {
		log.info("removing all components on " + hostName + "...");
		AmbariAutomationResponse resp = this.deleteHostComponents(clusterName, hostName);
		if (resp.getStatusCode() != 200) {
			throw new Exception(resp.getBody());
		}
	}
	/**
	 * stop all the components on specified host
	 * @param clusterName
	 * @param hostName
	 * @throws Exception
	 */
	public void stopAllHostComponents(String clusterName, String hostName) throws Exception {
		log.info("stopping all components on " + hostName + "...");
		AmbariAutomationResponse resp = this.updateHostComponents(clusterName, hostName, "STARTED", "INSTALLED");
		if (resp.getStatusCode() == 200) {
			log.info("all components already stops");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
}
