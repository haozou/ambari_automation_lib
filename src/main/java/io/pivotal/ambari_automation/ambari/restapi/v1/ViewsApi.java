package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class ViewsApi extends BaseApi{
	
	public ViewsApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}

	public AmbariAutomationResponse getViews(){
		String uri = "http://" + host + (port==0?"":":"+port) + "/api/v1/views";
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		return http.access(request, false);
	}

	public AmbariAutomationResponse getView(String viewName){
		String uri = "http://" + host + (port==0?"":":"+port) + "/api/v1/views/" + viewName;
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		return http.access(request, false);
	}

	public AmbariAutomationResponse getViewVersions(String viewName){
		String uri = "http://" + host + (port==0?"":":"+port) + "/api/v1/views/" + viewName + "/versions";
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		return http.access(request, false);
	}

	public AmbariAutomationResponse getViewVersion(String viewName, String version){
		String uri = "http://" + host + (port==0?"":":"+port) + "/api/v1/views/" + viewName + "/versions/" + version;
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		return http.access(request, false);
	}

}
