package io.pivotal.ambari_automation.ambari.restapi;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpGet;

public class AmbariAutomationHttpGet extends HttpGet{
	public AmbariAutomationHttpGet(String user, String password, String uri) {
		super(uri);
		addHeader("X-Requested-By", "PIVOTAL");
		String basic_auth = new String(Base64.encodeBase64((user + ":" + password).getBytes()));
		addHeader("Authorization", "Basic " + basic_auth);
	}
}
