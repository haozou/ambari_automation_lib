package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BootStrapApiTest {
	@Test
	public void testBootstrapPost(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{\"status\":\"OK\",\"log\":\"Running Bootstrap now.\",\"requestId\":4}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		BootStrapApi api = new BootStrapApi(null, 0, null, null);
		AmbariAutomationResponse response = api.bootstrapPost("ddd", "hosts" , "ddd", false);
		
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBodyStringValueByJsonPath("status"), "OK"); 

	}
	@Test
	public void testGetBootstrapStatus(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, null, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};
		BootStrapApi api = new BootStrapApi(null, 0, null, null);
		AmbariAutomationResponse response = api.getBootStrapStatus(1);
		Assert.assertEquals(response.getStatusCode(), 200);
	}
}