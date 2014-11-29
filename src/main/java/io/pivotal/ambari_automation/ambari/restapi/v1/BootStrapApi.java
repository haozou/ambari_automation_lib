package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.StringEntity;
import org.testng.log4testng.Logger;

public class BootStrapApi extends BaseApi {
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger.getLogger(BootStrapApi.class);

	public BootStrapApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}

	/**
	 * bootstrap api to provision the agents
	 * 
	 * @param sshKey
	 * @param hosts
	 * @param verbose
	 * @return
	 */
	public AmbariAutomationResponse bootstrapPost(String sshKey,
			String hosts, String sshUser, boolean verbose) {
		String uri = this.buildUri("/api/v1/bootstrap/");

		String body = String.format("{\"sshKey\":\"%s\"," + "\"hosts\":%s,"
				+ "\"user\":\"%s\"," + "\"verbose\":\"%s\"}", sshKey, hosts,
				sshUser, String.valueOf(verbose));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user,
				password, uri);
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
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
	 * get boot strap status
	 * @param requestId
	 * @return
	 */
	public AmbariAutomationResponse getBootStrapStatus(int requestId) {
		String uri = this.buildUri("/api/v1/bootstrap/"
				+ requestId);
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user,
				password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}

}
