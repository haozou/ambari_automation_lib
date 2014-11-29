package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpDelete;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class HostApi extends BaseApi {

	public HostApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	
	/**
	 * Returns a collection of all hosts for the cluster identified by ":clusterName"
	 * @param clusterName
	 * @return
	 */
	public AmbariAutomationResponse listHosts(String clusterName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts", clusterName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Returns information about a single host in the cluster identified by ":clusterName".
	 * @param clusterName
	 * @param hostname
	 * @return
	 */
	public AmbariAutomationResponse viewHostInfo(String clusterName, String hostname) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s", clusterName, hostname));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Create a host resource identified by ":hostName" in the cluster identified by ":clusterName".
	 * @param clusterName
	 * @param hostname
	 * @return
	 */
	public AmbariAutomationResponse createHost(String clusterName, String hostname) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s", clusterName, hostname));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Delete a host resource identified by ":hostName" in the cluster identified by ":clusterName".
	 * @param clusterName
	 * @param hostname
	 * @return
	 */
	public AmbariAutomationResponse deleteHost(String clusterName, String hostname) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/hosts/%s", clusterName, hostname));
		AmbariAutomationHttpDelete request = new AmbariAutomationHttpDelete(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
}
