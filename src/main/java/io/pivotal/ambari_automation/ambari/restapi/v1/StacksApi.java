package io.pivotal.ambari_automation.ambari.restapi.v1;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPut;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class StacksApi extends BaseApi {

	public StacksApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	/**
	 * update stack repo
	 * @param stackName
	 * @param stackVersion
	 * @param os
	 * @param stackRepoURL
	 * @return
	 */
	public AmbariAutomationResponse putStack(String stackName, String stackVersion, String os, String repoName, String stackRepoURL) {
		String uri = this.buildUri(String.format("/api/v1/stacks/%s/versions/%s/operating_systems/%s/repositories/%s",
				stackName, stackVersion, os, repoName));
		String body = String.format("{\"Repositories\":{\"base_url\":\"%s\",\"verify_base_url\":true}}", stackRepoURL);
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
	 * get stack repo
	 * @param stackName
	 * @param stackVersion
	 * @param os
	 * @return
	 */
	public AmbariAutomationResponse getStack(String stackName, String stackVersion, String os) {
		String uri = this.buildUri(String.format("/api/v1/stacks/%s/versions/%s/operating_systems/%s/repositories",
				stackName, stackVersion, os));

		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
}
