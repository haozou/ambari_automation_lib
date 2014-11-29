package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class ServiceComponentApi extends BaseApi {
	
	public ServiceComponentApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	
	/**
	 * Returns a collection of all components for the service identified by ":serviceName" and the cluster identified by ":clusterName"
	 * @param serviceName
	 * @param clusterName
	 * @return
	 */
	public AmbariAutomationResponse listAllComponents(String serviceName, String clusterName) {
		String uri = this.buildUri(String.format("api/v1/clusters/%s/services/%s/compoents", serviceName, clusterName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Return the component identified by ":componentName" for for the service identified by ":serviceName".
	 * @param serviceName
	 * @param componentName
	 * @return
	 */
	public AmbariAutomationResponse viewComponentInformation(String serviceName, String componentName) {
		String uri = this.buildUri(String.format("api/v1/clusters/%s/services/%s/components", serviceName, componentName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Create the component identified by ":componentName" for for the service identified by ":serviceName" and the cluster identified by ":clusterName".
	 * @param clusterName
	 * @param serviceName
	 * @param componentName
	 * @return
	 */
	public AmbariAutomationResponse createComponent(String clusterName, String serviceName, String componentName) {
		String uri = this.buildUri(String.format("api/v1/clusters/%s/services/%s/components/%s", clusterName, serviceName, componentName));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	

}
