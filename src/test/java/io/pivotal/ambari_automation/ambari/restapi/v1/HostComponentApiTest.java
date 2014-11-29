package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HostComponentApiTest {
	@Test
	public void testLisHostComponentsPos(
			@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{"
				+ "\"href\": \"http://your.ambari.server/api/v1/clusters/c1/hosts/\","
				+ "\"items\": ["
				+ "{"
				+ "\"href\": \"http://your.ambari.server/api/v1/clusters/c1/hosts/host1\","
				+ "\"HostRoles\": {"
				+ "\"cluster_name\": \"c1\","
				+ "\"component_name\": \"DATANODE\""
				+ "}},"
				+ "{"
				+ "\"href\": \"http://your.ambari.server/api/v1/clusters/c1/hosts/host1\","
				+ "\"HostRoles\": {" + "\"cluster_name\": \"c1\","
				+ "\"component_name\": \"HBASE_CLIENT\"" + "}}]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(
				200, body, null);

		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase) any, anyBoolean);
				result = mockResponse;

			}
		};

		HostComponentApi api = new HostComponentApi(null, 0, null, null);
		AmbariAutomationResponse response = api.listHostComponents("someCluster", "host1");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyListByJsonPath("items.HostRoles.component_name").get(0), "DATANODE");
		Assert.assertEquals(response.getBodyListByJsonPath("items.HostRoles.component_name").get(1), "HBASE_CLIENT");
	}
	@Test
	public void testViewHostComponentInfoPos(
			@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{"
				+ "\"href\": \"http://your.ambari.server/api/v1/clusters/c1/hosts/host1\","
				+ "\"metrics\": {\"key\":\"value\"},"
				+ "\"HostRoles\": {\"component_name\":\"DATANODE\"}}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(
				200, body, null);

		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase) any, anyBoolean);
				result = mockResponse;

			}
		};

		HostComponentApi api = new HostComponentApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewHostComponentInfo("someCluster", "host1", "DATANODE");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyStringValueByJsonPath("HostRoles.component_name"), "DATANODE");
	}
	@Test
	public void testCreateHostComponentPos(
			@Mocked final AmbariAutomationHttpClient mockedHttp) {
		
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(
				201, null, null);

		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase) any, anyBoolean);
				result = mockResponse;

			}
		};

		HostComponentApi api = new HostComponentApi(null, 0, null, null);
		AmbariAutomationResponse response = api.createHostComponent("someCluster", "host1", "DATANODE");
		
		Assert.assertEquals(response.getStatusCode(), 201);
	}
	@Test
	public void testUpdateHostComponentPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
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

		HostComponentApi api = new HostComponentApi(null, 0, null, null);
		AmbariAutomationResponse response = api.updateHostComponent("c1", "host1", "DATANODE", "STARTED");

		Assert.assertEquals(response.getStatusCode(), 202);
		Assert.assertEquals(response.getBodyStringValueByJsonPath("Requests.status"), "InProgress");
	}
}
