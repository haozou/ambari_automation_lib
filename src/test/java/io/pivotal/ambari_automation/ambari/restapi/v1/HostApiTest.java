package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HostApiTest {
	@Test
	public void testLisHostsPos(
			@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{"
				+ "\"href\": \"http://your.ambari.server/api/v1/clusters/c1/hosts/\","
				+ "\"items\": ["
				+ "{"
				+ "\"href\": \"http://your.ambari.server/api/v1/clusters/c1/hosts/host1\","
				+ "\"Hosts\": {"
				+ "\"cluster_name\": \"c1\","
				+ "\"host_name\": \"host1\""
				+ "}},"
				+ "{"
				+ "\"href\": \"http://your.ambari.server/api/v1/clusters/c1/hosts/host2\","
				+ "\"Hosts\": {" + "\"cluster_name\": \"c1\","
				+ "\"host_name\": \"host2\"" + "}}]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(
				200, body, null);

		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase) any, anyBoolean);
				result = mockResponse;

			}
		};

		HostApi api = new HostApi(null, 0, null, null);
		AmbariAutomationResponse response = api.listHosts("someCluster");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyListByJsonPath("items.Hosts.host_name").get(0), "host1");
		Assert.assertEquals(response.getBodyListByJsonPath("items.Hosts.host_name").get(1), "host2");
	}
	@Test
	public void testViewHostInfoPos(
			@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{"
				+ "\"href\": \"http://your.ambari.server/api/v1/clusters/c1/hosts/host1\","
				+ "\"metrics\": {\"key\":\"value\"},"
				+ "\"Hosts\": {\"host_name\":\"host1\"}}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(
				200, body, null);

		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase) any, anyBoolean);
				result = mockResponse;

			}
		};

		HostApi api = new HostApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewHostInfo("someCluster", "host1");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyStringValueByJsonPath("Hosts.host_name"), "host1");
	}
	@Test
	public void testCreateHostInfoPos(
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

		HostApi api = new HostApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewHostInfo("someCluster", "host1");
		
		Assert.assertEquals(response.getStatusCode(), 201);
	}
}
