package io.pivotal.ambari_automation.util;

import static com.jayway.restassured.RestAssured.preemptive;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;

public class RestAssuredHelper {
	/**
	 * Builds RestAssured Authentication for all subsequent API calls.
	 */
	public static void buildRestAssuredAuth(String user, String password) {
		RestAssured.authentication = preemptive().basic(user, password);
		RequestSpecBuilder request = new RequestSpecBuilder();
		request.addHeader("X-Requested-By", "PIVOTAL");
		RestAssured.requestSpecification = request.build();
	}

}
