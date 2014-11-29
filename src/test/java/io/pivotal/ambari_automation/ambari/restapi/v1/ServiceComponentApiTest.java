package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ServiceComponentApiTest {
	
	@Test
	public void testListAllComponents(@Mocked final AmbariAutomationHttpClient mockedHttp ) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.222.14:8080/api/v1/clusters/test/services/HDFS/components\"," +
		        "  \"items\" : [{" +
		        "    \"href\" : \"http://10.103.222.14:8080/api/v1/clusters/test/services/HDFS/components/NAMENODE\"," +
		        "    \"ServiceComponentInfo\" : {" + 
		        "	 \"cluster_name\" : \"test\"," +
		        "    \"component_name\" : \"NAMENODE\"," +
		        "    \"service_name\" : \"HDFS\"" + 
		        "	 }" +
		        "  }" +
		        "]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase)any, anyBoolean);
				result = mockResponse;
			}
			
		};
		
		ServiceComponentApi api = new ServiceComponentApi(null, 0, null, null);
		AmbariAutomationResponse response = api.listAllComponents("HDFS", "test");
		
		Assert.assertEquals(response.getStatusCode(), 200);		
		Assert.assertEquals(response.getBodyListByJsonPath("items.ServiceComponentInfo.component_name").get(0), "NAMENODE");
	}
	
	@Test
	public void testViewComponentInformation(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.222.14:8080/api/v1/clusters/test/services/HDFS/components/DATANODE\"," +
				"  \"metrices\" : {" +
		        "        \"process\" : {" +
				" }," +
				"        \"network\" : {" +
				" },}," +
				 "    \"ServiceComponentInfo\" : {" + 
			        "	 \"cluster_name\" : \"test\"," +
			        "    \"component_name\" : \"DATANODE\"," +
			        "    \"service_name\" : \"HDFS\"," +
			        "    \"state\" : \"STARTED\"" + 
			        "	 }," +
		        "  \"host_components\" : [{" +
		        "    \"href\" : \"http://10.103.222.14:8080/api/v1/clusters/test/hosts/host1/host_components/DATANODE\"," +
		        "    \"HostRoles\" : {" + 
		        "	 \"cluster_name\" : \"test\"," +
		        "    \"component_name\" : \"DATANODE\"," +
		        "    \"host_name\" : \"host1\"" +
		        "	 }" +
		        "  }" +
		        "]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase)any, anyBoolean);
				result = mockResponse;
			}
		};
		
		ServiceComponentApi api = new ServiceComponentApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewComponentInformation("HDFS", "DATANODE");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyStringValueByJsonPath("ServiceComponentInfo.component_name"), "DATANODE");
		Assert.assertEquals(response.getBodyStringValueByJsonPath("ServiceComponentInfo.state"), "STARTED");
	}
	
	@Test
	public void testCreateComponent(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, null, null);
		
		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase)any, anyBoolean);
				result = mockResponse;
			}
		};
		
		ServiceComponentApi api = new ServiceComponentApi(null, 0, null, null);
		AmbariAutomationResponse response = api.createComponent("test", "HDFS", "NAMENODE");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
	}

}
