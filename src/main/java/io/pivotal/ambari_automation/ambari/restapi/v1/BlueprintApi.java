package io.pivotal.ambari_automation.ambari.restapi.v1;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class BlueprintApi extends BaseApi {
	
	public BlueprintApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	
	public AmbariAutomationResponse uploadBlueprint(String blueprintName, String blueprint) {
		String uri = this.buildUri(String.format("/api/v1/blueprints/%s", blueprintName));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		StringEntity entity = null;
		try {
			entity = new StringEntity(blueprint);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}

}
