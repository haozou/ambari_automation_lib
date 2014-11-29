package io.pivotal.ambari_automation.ambari.restapi.v1;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.testng.log4testng.Logger;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPut;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class ServiceApi extends BaseApi {
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger.getLogger(ServiceApi.class);
	public ServiceApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	
	/**
	 * Returns a collection of the services for the cluster identified by ":clusterName".
	 * @param clusterName
	 * @return response
	 */
	public AmbariAutomationResponse listServices(String clusterName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/services", clusterName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Gets the service information for the service identified by ":serviceName" for 
	 * cluster identified by ":clusterName".
	 * @param clusterName
	 * @param serviceName
	 * @return response
	 */
	public AmbariAutomationResponse viewServiceInfo(String clusterName, String serviceName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/services/%s", clusterName, serviceName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Create the service identified by ":serviceName" in the cluster identified by ":clusterName".
	 * @param clusterName
	 * @param serviceName
	 * @return response
	 */
	public AmbariAutomationResponse createService(String clusterName, String serviceName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/services/%s", clusterName, serviceName));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Update the service identified by ":serviceName" of the cluster identified by ":clusterName" 
	 * to new state
	 * @param clusterName
	 * @param serviceName
	 * @param newState either INSTALLED(means STOPED) or STARTED
	 * @return response
	 */
	public AmbariAutomationResponse updateService(String clusterName, String serviceName, String newState) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/services/%s", clusterName, serviceName));
		String body = String.format("{\"ServiceInfo\":{\"state\":\"%s\"}}", newState);
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
	 * start service 
	 * @param clusterName
	 * @param serviceName
	 * @throws Exception
	 */
	public void startService(String clusterName, String serviceName) throws Exception {
		log.info("starting " + serviceName + "...");
		AmbariAutomationResponse resp = this.updateService(clusterName, serviceName, "STARTED");
		if (resp.getStatusCode() == 200) {
			log.info("service already started");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
	/**
	 * stop service 
	 * @param clusterName
	 * @param serviceName
	 * @throws Exception
	 */
	public void stopService(String clusterName, String serviceName) throws Exception {
		log.info("stopping " + serviceName + "...");
		AmbariAutomationResponse resp = this.updateService(clusterName, serviceName, "INSTALLED");
		if (resp.getStatusCode() == 200) {
			log.info("service already stopped");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
	/**
	 * Update all services of the cluster identified by ":clusterName" from original state to
	 * new state
	 * @param clusterName
	 * @param orgState
	 * @param newState
	 * @return response
	 */
	public AmbariAutomationResponse updateServices(String clusterName, String orgState, String newState) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/services?ServiceInfo/state=%s", clusterName, orgState));
		String body = String.format("{\"ServiceInfo\":{\"state\":\"%s\"}}", newState);
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
	 * start all 
	 * @param clusterName
	 * @throws Exception 
	 */
	public void startAll(String clusterName) throws Exception {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/services?", clusterName));
		String body = "{\"ServiceInfo\":{\"state\":\"STARTED\"}}";
		AmbariAutomationHttpPut request = new AmbariAutomationHttpPut(user, password, uri);
		StringEntity entity = null;
		try {
			entity = new StringEntity(body);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
		AmbariAutomationResponse response = http.access(request, false);
		if (response.getStatusCode() == 200) {
			log.info("all services already started");
			return;
		}
		if (response.getStatusCode() != 202) {
			throw new Exception(response.getBody());
		}
		this.trackStatus(clusterName, response.getBodyIntValueByJsonPath("Requests.id"));
	}
	/**
	 * start all services
	 * @param clusterName
	 * @throws Exception
	 */
	public void startAllServices(String clusterName) throws Exception {
		log.info("starting all services...");
		AmbariAutomationResponse resp = this.updateServices(clusterName, "INSTALLED", "STARTED");
		if (resp.getStatusCode() == 200) {
			log.info("all services already started");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
	/**
	 * stop all services
	 * @param clusterName
	 * @throws Exception
	 */
	public void stopAllServices(String clusterName) throws Exception {
		log.info("stopping all services...");
		AmbariAutomationResponse resp = this.updateServices(clusterName, "STARTED", "INSTALLED");
		if (resp.getStatusCode() == 200) {
			log.info("all services already stopped");
			return;
		}
		if (resp.getStatusCode() != 202) {
			throw new Exception(resp.getBody());
		}
		this.trackStatus(clusterName, resp.getBodyIntValueByJsonPath("Requests.id"));
	}
}
