package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;

public class TasksApiTest {
	@Test
	public void testlistTasksPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.222.14:8080/api/v1/clusters\"," +
		        "  \"items\" : [{" +
		        "    \"href\" : \"http://10.103.222.14:8080/api/v1/clusters\"," +
		        "    \"Tasks\" : {" + 
		        "	 \"cluster_name\" : \"c1\"," +
		        "    \"id\" : 2," + 
		        "    \"request_id\" : 1," + 
		        "    \"role\" : \"DATANODE\"" + 
		        "	 }" +
		        "  }" +
		        "]}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		TasksApi api = new TasksApi(null, 0, null, null);
		AmbariAutomationResponse response = api.listTasks("c1", 1, "role");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.222.14:8080/api/v1/clusters");
		Assert.assertEquals(JsonPath.from(response.getBody()).getList("items.Tasks.cluster_name").get(0), "c1");
		Assert.assertEquals(JsonPath.from(response.getBody()).getList("items.Tasks.id").get(0), 2);
		Assert.assertEquals(JsonPath.from(response.getBody()).getList("items.Tasks.request_id").get(0), 1);
		Assert.assertEquals(JsonPath.from(response.getBody()).getList("items.Tasks.role").get(0), "DATANODE");

	}
	@Test
	public void testviewTasksPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{" +
		        "  \"href\" : \"http://10.103.222.14:8080/api/v1/clusters\"," +
		        "  \"Tasks\" : {" +
		        "	 \"cluster_name\" : \"c1\"," +
		        "    \"id\" : 2," + 
		        "    \"request_id\" : 1," + 
		        "    \"status\" : \"COMPLETED\"" + 
		        "	 }" +
		        "}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		TasksApi api = new TasksApi(null, 0, null, null);
		AmbariAutomationResponse response = api.viewTaskInfo("c1", 1, 96, "status");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.222.14:8080/api/v1/clusters");
		Assert.assertEquals(response.getBodyStringValueByJsonPath("Tasks.status"), "COMPLETED");

	}
}