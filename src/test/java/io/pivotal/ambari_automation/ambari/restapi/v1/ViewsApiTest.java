package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;

public class ViewsApiTest {
	@Test
	public void testGetViewsTest(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String mockedBody = "{" +
				"  \"href\": \"http://10.103.218.120:8080/api/v1/views\"," +
				"  \"items\": [{" +
				"    \"href\": \"http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW\"," +
				"    \"ViewInfo\": {" +
				"      \"view_name\": \"ADMIN_VIEW\"" +
				"    }" +
				"  }]" +
				"}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, mockedBody, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ViewsApi api = new ViewsApi(null, 0, null, null);
		AmbariAutomationResponse response = api.getViews();

		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.218.120:8080/api/v1/views");
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("items[0].ViewInfo.view_name"), "ADMIN_VIEW");

	}

	@Test
	public void testGetViewTest(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String mockedBody = 
				"{" +
				"  \"href\": \"http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW\"," +
				"  \"ViewInfo\": {" +
				"    \"view_name\": \"ADMIN_VIEW\"" +
				"  }," +
				"  \"versions\": [{" +
				"    \"href\": \"http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW/versions/1.0.0\"," +
				"    \"ViewVersionInfo\": {" +
				"      \"version\": \"1.0.0\"," +
				"      \"view_name\": \"ADMIN_VIEW\"" +
				"    }" +
				"  }]" +
				"}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, mockedBody, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ViewsApi api = new ViewsApi(null, 0, null, null);
		AmbariAutomationResponse response = api.getView("ADMIN_VIEW");

		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW");
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("ViewInfo.view_name"), "ADMIN_VIEW");

	}

	@Test
	public void testGetViewVersionsTest(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String mockedBody = 
				"{" +
				"  \"href\": \"http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW/versions\"," +
				"  \"items\": [{" +
				"    \"href\": \"http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW/versions/1.0.0\"," +
				"    \"ViewVersionInfo\": {" +
				"      \"version\": \"1.0.0\"," +
				"      \"view_name\": \"ADMIN_VIEW\"" +
				"    }" +
				"  }]" +
				"}";

		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, mockedBody, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ViewsApi api = new ViewsApi(null, 0, null, null);
		AmbariAutomationResponse response = api.getViewVersions("ADMIN_VIEW");

		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW/versions");
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("items[0].ViewVersionInfo.version"), "1.0.0");

	}

	@Test
	public void testGetViewVersionTest(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String mockedBody = 
				"{" +
				"  \"href\": \"http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW/versions/1.0.0\"," +
				"  \"ViewVersionInfo\": {" +
				"    \"archive\": \"/var/lib/ambari-server/resources/views/work/ADMIN_VIEW{1.0.0}\"," +
				"    \"description\": null," +
				"    \"label\": \"Ambari Admin View\"," +
				"    \"masker_class\": null," +
				"    \"parameters\": [ ]," +
				"    \"status\": \"DEPLOYED\"," +
				"    \"status_detail\": \"Deployed /var/lib/ambari-server/resources/views/work/ADMIN_VIEW{1.0.0}.\"," +
				"    \"system\": true," +
				"    \"version\": \"1.0.0\"," +
				"    \"view_name\": \"ADMIN_VIEW\"" +
				"  }," +
				"  \"permissions\": [{" +
				"    \"href\": \"http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW/versions/1.0.0/permissions/4\"," +
				"    \"PermissionInfo\": {" +
				"      \"permission_id\": 4," +
				"      \"version\": \"1.0.0\"," +
				"      \"view_name\": \"ADMIN_VIEW\"" +
				"    }" +
				"  }]," +
				"  \"instances\": [{" +
				"    \"href\": \"http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW/versions/1.0.0/instances/INSTANCE\"," +
				"    \"ViewInstanceInfo\": {" +
				"    \"instance_name\": \"INSTANCE\"," +
				"    \"version\": \"1.0.0\"," +
				"    \"view_name\": \"ADMIN_VIEW\"" +
				"    }" +
				"  }]" +
				"}";

		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, mockedBody, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		ViewsApi api = new ViewsApi(null, 0, null, null);
		AmbariAutomationResponse response = api.getViewVersion("ADMIN_VIEW", "1.0.0");

		Assert.assertEquals(JsonPath.from(response.getBody()).getString("href"), "http://10.103.218.120:8080/api/v1/views/ADMIN_VIEW/versions/1.0.0");
		Assert.assertEquals(JsonPath.from(response.getBody()).getString("ViewVersionInfo.version"), "1.0.0");

	}

}

