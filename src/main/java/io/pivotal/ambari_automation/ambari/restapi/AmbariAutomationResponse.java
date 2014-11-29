package io.pivotal.ambari_automation.ambari.restapi;

import static com.jayway.restassured.path.json.JsonPath.from;

import java.util.List;
import java.util.Map;

import org.apache.http.Header;

public class AmbariAutomationResponse {
	private int statusCode;
	private String body;
	private Header[] headers;

	public AmbariAutomationResponse(int statusCode, String body, Header[] headers) {
		this.statusCode = statusCode;
		this.body = body;
		this.headers = headers;
	}

	public int getStatusCode() { return statusCode; }
	public String getBody() { return body; }
	public Header[] getHeaders() { return headers; }
	
	public String getBodyStringValueByJsonPath(String jsonPath) {
		return from(this.getBody()).getString(jsonPath);
	}
	public int getBodyIntValueByJsonPath(String jsonPath) {
		return from(this.getBody()).getInt(jsonPath);
	}
	public <T> List<T> getBodyListByJsonPath(String jsonPath) {
		return from(this.getBody()).getList(jsonPath);
	}
	public <K, V> Map<K, V> getBodyMapByJsonPath(String jsonPath) {
		return from(this.getBody()).getMap(jsonPath);
	}
}