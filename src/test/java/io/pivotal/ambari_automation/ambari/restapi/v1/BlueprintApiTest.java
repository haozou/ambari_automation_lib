package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpClient;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import mockit.Expectations;
import mockit.Mocked;

import org.apache.http.client.methods.HttpRequestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BlueprintApiTest {
	@Test
	public void testUploadBlueprintPos(@Mocked final AmbariAutomationHttpClient mockedHttp) {

		final AmbariAutomationResponse mockResponse = new AmbariAutomationResponse(201, null, null);
		
		new Expectations() {{
			new AmbariAutomationHttpClient();
			mockedHttp.access((HttpRequestBase)any, anyBoolean); result = mockResponse;

		}};

		BlueprintApi api = new BlueprintApi(null, 0, null, null);
		AmbariAutomationResponse response = api.uploadBlueprint("blueprintName", "blueprint");
		
		Assert.assertEquals(response.getStatusCode(), 201);

	}
}