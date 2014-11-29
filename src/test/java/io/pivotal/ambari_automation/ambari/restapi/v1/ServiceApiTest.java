package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;

public class ServiceApiTest {

	@Test
	public void testListServicesPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.222.14:8080/api/v1/clusters\"," +
		        "  \"items\" : [{" +
		        "    \"href\" : \"http://10.103.222.14:8080/api/v1/clusters\"," +
		        "    \"ServiceInfo\" : {" + 
		        "	 \"cluster_name\" : \"c1\"," +
		        "    \"service_name\" : \"FALCON\"" + 
		        "	 }" +
		        "  }" +
		        "]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ServiceApi api = new ServiceApi(null, 0, null, null);
		AmbariAutomationResponse response = api.listServices("c1");

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.222.14:8080/api/v1/clusters");
		Assert.assertEquals(JsonPath.from(response.getBody()).getList("items.ServiceInfo.cluster_name").get(0), "c1");
		Assert.assertEquals(response.getBodyListByJsonPath("items.ServiceInfo.service_name").get(0), "FALCON");
	}
	
	@Test
	public void testViewServiceInfoPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		String body = "{" +
				"\"href\" : \"http://ambari2:8080/api/v1/clusters/test/services/FALCON\"," +
				"\"ServiceInfo\" : {"	+
				"\"cluster_name\" : \"test\"," +
				"\"maintenance_state\" : \"OFF\"," +
				"\"service_name\" : \"FALCON\"," +
				"\"state\" : \"STARTED\"" +
				"}," +
				"\"components\" : [" +
				"{" +
				"\"href\" : \"http://ambari2:8080/api/v1/clusters/test/services/FALCON/components/FALCON_CLIENT\"," +
				"\"ServiceComponentInfo\" : {" +
				"\"cluster_name\" : \"test\"," +
				"\"component_name\" : \"FALCON_CLIENT\"," +
				"\"service_name\" : \"FALCON\"" + 
      			"}" +
				"}]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ServiceApi api = new ServiceApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewServiceInfo("c1", "FALCON");

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyListByJsonPath("components.ServiceComponentInfo.component_name").get(0), "FALCON_CLIENT");
	}
	
	@Test
	public void testUpdateServicePos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		String body = "{" + 
				  "\"href\" : \"http://ambari2:8080/api/v1/clusters/test/requests/2\"," +
				  "\"Requests\" : {" +
				  "\"id\" : 2," +
				  "\"status\" : \"InProgress\"" +
				  "}}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(202, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ServiceApi api = new ServiceApi(null, 0, null, null);
		AmbariAutomationResponse response = api.updateService("c1", "FALCON", "STARTED");

		Assert.assertEquals(response.getStatusCode(), 202);
		Assert.assertEquals(response.getBodyStringValueByJsonPath("Requests.status"), "InProgress");
	}
	@Test
	public void testUpdateAllServicesPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {

		String body = "{" + 
				  "\"href\" : \"http://ambari2:8080/api/v1/clusters/test/requests/2\"," +
				  "\"Requests\" : {" +
				  "\"id\" : 2," +
				  "\"status\" : \"InProgress\"" +
				  "}}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(202, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ServiceApi api = new ServiceApi(null, 0, null, null);
		AmbariAutomationResponse response = api.updateServices("c1", "INSTALLED", "STARTED");

		Assert.assertEquals(response.getStatusCode(), 202);
		Assert.assertEquals(response.getBodyStringValueByJsonPath("Requests.status"), "InProgress");
	}
}
