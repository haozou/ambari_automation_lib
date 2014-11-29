package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpDelete;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

public class ClustersApi extends BaseApi{

	public ClustersApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}

	/**
	 * Create a cluster identified by ":clusterName". with hostMapping
	 * @param clusterName
	 * @param hostMapping
	 * @return response
	 */
	public AmbariAutomationResponse createClusterWithHostMapping(String clusterName, String hostMapping) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s", clusterName));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		StringEntity entity = null;
		try {
			entity = new StringEntity(hostMapping);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
		
	}
	/**
	 * Create a cluster identified by ":clusterName".
	 * @param clusterName
	 * @return response
	 */
	public AmbariAutomationResponse createCluster(String clusterName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s", clusterName));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Returns a collection of the currently configured clusters.
	 * @return response
	 */
	public AmbariAutomationResponse listCluster() {
		String uri = this.buildUri("/api/v1/clusters");
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Returns information for the specified cluster identified by ":clusterName"
	 * @param clusterName
	 * @return response
	 */
	public AmbariAutomationResponse viewClusterInfo(String clusterName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s", clusterName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Delete the cluster identified by ":clusterName".
	 * @param clusterName
	 * @return response
	 */
	public AmbariAutomationResponse deleteCluster(String clusterName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s", clusterName));
		AmbariAutomationHttpDelete request = new AmbariAutomationHttpDelete(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}

}
