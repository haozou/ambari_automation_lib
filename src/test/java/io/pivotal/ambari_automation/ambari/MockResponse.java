package io.pivotal.ambari_automation.ambari;

import java.io.InputStream;
import java.util.Map;

import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.jayway.restassured.mapper.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.json.config.JsonPathConfig;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.path.xml.XmlPath.CompatibilityMode;
import com.jayway.restassured.path.xml.config.XmlPathConfig;
import com.jayway.restassured.response.Cookie;
import com.jayway.restassured.response.Cookies;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.response.ValidatableResponse;

public class MockResponse implements Response{

	private int statusCode;
	private String body;

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String prettyPrint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response prettyPeek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T as(Class<T> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T as(Class<T> cls, ObjectMapperType mapperType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T as(Class<T> cls, ObjectMapper mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonPath jsonPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonPath jsonPath(JsonPathConfig config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlPath xmlPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlPath xmlPath(XmlPathConfig config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlPath xmlPath(CompatibilityMode compatibilityMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlPath htmlPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T path(String path, String... arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO this should've been setAsString
	public void setBody(String body) { this.body = body; }
	@Override public String asString() { return body; }

	@Override
	public byte[] asByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream asInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response andReturn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response thenReturn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseBody body() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseBody getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Headers headers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Headers getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String header(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> cookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookies detailedCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookies getDetailedCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie detailedCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie getDetailedCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String contentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String statusLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatusLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setStatusCode(int x){ statusCode = x;}
	@Override public int statusCode() { return statusCode; }

	@Override
	public int getStatusCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ValidatableResponse then() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
