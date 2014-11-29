package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;

public class ClustersApiTest {
	@Test
	public void testCreateCLusterWithHostMappingPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.222.14:8080/api/v1/clusters/test/requests/1\"," +
		        "  \"Requests\" : {" +
		        "    \"id\" : 1," +
		        "    \"status\" : \"InProgress\"" +
		        "  }" +
		        "}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ClustersApi api = new ClustersApi(null, 0, null, null);
		AmbariAutomationResponse response = api.createClusterWithHostMapping("somecluster", "somemapping");

		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.222.14:8080/api/v1/clusters/test/requests/1");
		Assert.assertEquals(JsonPath.from(response.getBody()).getInt("Requests.id"), 1);
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("Requests.status"), "InProgress");

	}
	@Test
	public void testCreateCLusterPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
	
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(201, null, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ClustersApi api = new ClustersApi(null, 0, null, null);
		AmbariAutomationResponse response = api.createCluster("somecluster");

		Assert.assertEquals(response.getStatusCode(), 201);

	}
	@Test
	public void testListCLusterPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.222.14:8080/api/v1/clusters\"," +
		        "  \"items\" : [{" +
		        "    \"href\" : \"http://10.103.222.14:8080/api/v1/clusters\"," +
		        "    \"Clusters\" : {" + 
		        "	 \"cluster_name\" : \"c1\"," +
		        "    \"version\" : \"HDP-1.2.0\"" + 
		        "	 }" +
		        "  }" +
		        "]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ClustersApi api = new ClustersApi(null, 0, null, null);
		AmbariAutomationResponse response = api.listCluster();

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.222.14:8080/api/v1/clusters");
		Assert.assertEquals(JsonPath.from(response.getBody()).getList("items.Clusters.cluster_name").get(0), "c1");
		Assert.assertEquals(JsonPath.from(response.getBody()).getList("items.Clusters.version").get(0), "HDP-1.2.0");
	}
	
	@Test
	public void testViewCLusterInfoPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {

		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, null, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ClustersApi api = new ClustersApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewClusterInfo("somecluster");

		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test
	public void testDeleteClusterPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {

		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, null, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ClustersApi api = new ClustersApi(null, 0, null, null);
		AmbariAutomationResponse response = api.deleteCluster("somecluster");

		Assert.assertEquals(response.getStatusCode(), 200);
	}
}

