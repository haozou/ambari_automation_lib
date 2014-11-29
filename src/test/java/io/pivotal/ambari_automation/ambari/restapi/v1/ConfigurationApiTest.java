package io.pivotal.ambari_automation.ambari.restapi.v1;

import java.io.IOException;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ConfigurationApiTest {
	
	@Test
	public void testGetConfigurations(@Mocked final AmbariAutomationHttpClient mockedHttp ) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.221.112:8080/api/v1/clusters/test/configurations\"," +
		        "  \"items\" : [{" +
		        "    \"href\" : \"http://10.103.221.112:8080/api/v1/clusters/test/configurations?type=capacity-scheduler&tag=1\"," +
		        "     \"tag\" : \"1\"," +
		        "     \"type\" : \"cluster-env\"," +
		        "     \"version\" : \"1\"," +
		        "    \"Config\" : {" + 
		        "	 \"cluster_name\" : \"test\"," +
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
		
		ConfigurationApi api = new ConfigurationApi(null, 0, null, null);
		AmbariAutomationResponse response = api.getConfigurations("test");
		
		Assert.assertEquals(response.getStatusCode(), 200);		
		Assert.assertEquals(response.getBodyListByJsonPath("items.Config.cluster_name").get(0), "test");
	}
	
	@Test
	public void testViewConfigurationOfTypeTag(@Mocked final AmbariAutomationHttpClient mockedHttp ) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.221.112:8080/api/v1/clusters/test/configurations\"," +
		        "  \"items\" : [{" +
		        "    \"href\" : \"http://10.103.221.112:8080/api/v1/clusters/test/configurations?type=zookeeper-env&tag=1\"," +
		        "     \"tag\" : \"1\"," +
		        "     \"type\" : \"zookeeper-env\"," +
		        "     \"version\" : \"1\"," +
		        "    \"Config\" : {" + 
		        "	 \"cluster_name\" : \"test\"" +
		        "	 }," +
		        "    \"properties\" : {" + 
		        "	 \"clientPort\" : \"2181\"," +
		        "	 \"zk_user\" : \"zookeeper\"," +
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
		
		ConfigurationApi api = new ConfigurationApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewConfigurationOfTypeTag("test", "zookeeper-env", "1");
		
		Assert.assertEquals(response.getStatusCode(), 200);	
		Assert.assertEquals(response.getBodyListByJsonPath("items.Config.cluster_name").get(0), "test");
	}
	
	//@Test
	public void testCreateApplyConfiguration(@Mocked final AmbariAutomationHttpClient mockedHttp ) throws JsonProcessingException, IOException {
		final String body = "{" +
		       
		        "  \"Clusters\" : {" +
		        "    \"desired_config\" : {" + 
		        "	 \"type\" : \"core_site\"" +
		        "	 }," +
		        "    \"properties\" : {" + 
		        "	 \"tag\" : \"version2\"," +
		        "	 \"zk_user\" : \"zookeeper\"," +
		        "	 }" +
		        "  }" +
		        "}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {
			{
				new AmbariAutomationHttpClient();
				mockedHttp.access((HttpRequestBase)any, anyBoolean);
				result = mockResponse;
			}
			
		};
		
		ConfigurationApi api = new ConfigurationApi(null, 0, null, null);
		String[] property1 = {"hadoop_heapsize", "1024"};
		AmbariAutomationResponse response = api.createApplyConfiguration("test", body, "version12", property1);
		
		Assert.assertEquals(response.getStatusCode(), 200);	
		Assert.assertEquals(response.getBodyStringValueByJsonPath("Clusters.desired_config.type"), "core_site");
	}
}

