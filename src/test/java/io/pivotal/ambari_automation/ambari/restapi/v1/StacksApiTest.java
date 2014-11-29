package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StacksApiTest {
	@Test
	public void testPutStacks(@Mocked final AmbariAutomationHttpClient mockedHttp) {
		final String body = "{\"Repositories\":{\"base_url\":\"http://localhost\",\"verify_base_url\":true}}";
		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(200, body, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		StacksApi api = new StacksApi(null, 0, null, null);
		AmbariAutomationResponse response = api.putStack("ddd", "hosts" , "ddd", "dd", "dd");
		
		Assert.assertEquals(response.getStatusCode(), 200);

	}
}