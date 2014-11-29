package io.pivotal.ambari_automation.ambari.restapi.v1;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpDelete;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class UsersApi extends BaseApi {

	public UsersApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}

	public AmbariAutomationResponse getUsers(){
		String uri = this.buildUri("/api/v1/users");
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}

	public AmbariAutomationResponse getUsers(String name){
		String uri = this.buildUri("/api/v1/users/?Users/user_name.matches(.*" + name + ".*)");
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}

	public AmbariAutomationResponse createUser(String newUserName, String newUserPassword, boolean isAdmin){
		String uri = this.buildUri("/api/v1/users");
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		StringEntity entity = null;
		try {
			entity = new StringEntity("{\"Users/user_name\":\"" + newUserName + "\",\"Users/password\":\"" + newUserPassword + "\",\"Users/active\":true,\"Users/admin\":" + isAdmin + "}");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}

	public AmbariAutomationResponse deleteUser(String userName){
		String uri = this.buildUri("/api/v1/users/" + userName);
		AmbariAutomationHttpDelete request = new AmbariAutomationHttpDelete(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}

}
