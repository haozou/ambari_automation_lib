package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RequestAPITest {
	@Test
	public void testListRequestsPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		String body = "{" +
				"\"href\" : \"http://ambari2:8080/api/v1/clusters/test/requests?fields=Requests/request_context\"," +
				"\"items\" : [" +
				"{" +
				"\"href\" : \"http://ambari2:8080/api/v1/clusters/test/requests/1T\"," +
				"\"Requests\" : {" +
				"\"cluster_name\" : \"test\"," +
				"\"id\" : 1," +
				"\"request_context\" : \"Install and start all services\"" + 
      			"}" +
				"}]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		RequestApi api = new RequestApi(null, 0, null, null);
		AmbariAutomationResponse response = api.listRequests("c1", "request_context");

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyListByJsonPath("items.Requests.request_context").get(0), "Install and start all services");
	}
	@Test
	public void testViewRequestInfoPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		String body = "{" +
				"\"href\" : \"http://ambari2:8080/api/v1/clusters/test/requests?fields=Requests/request_context\"," +
				"\"Requests\" : {" +
				"\"cluster_name\" : \"test\"," +
				"\"id\" : 1," +
				"\"request_context\" : \"Install and start all services\"" + 
				"}}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		RequestApi api = new RequestApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewRequestInfo("c1", 1, "request_context");

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyStringValueByJsonPath("Requests.request_context"), "Install and start all services");
	}
	
}
