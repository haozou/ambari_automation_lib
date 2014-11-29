package io.pivotal.ambari_automation.ambari.restapi.v1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.HashMap;

import org.apache.http.entity.StringEntity;
import org.testng.log4testng.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPost;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpPut;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class ConfigurationApi extends BaseApi{
	
	private static final Logger log = Logger.getLogger(ConfigurationApi.class);

	public ConfigurationApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	
	/**List all the configurations defined in a cluster.
	 * @param clusterName
	 * @return response
	 */
	public AmbariAutomationResponse getConfigurations(String clusterName) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/configurations", clusterName));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;	
	}
	
	/**
	 * To view the key value pairs, given a type and tag.e.g. http://<somehost>:8080/api/v1/clusters/test/configurations?type=zookeeper-env&tag=1
	 * @param clusterName
	 * @param type
	 * @param tag
	 * @return response
	 */
	public AmbariAutomationResponse viewConfigurationOfTypeTag(String clusterName, String type, String tag) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/configurations?type=%s&tag=%s", clusterName, type, tag));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
		
	}
	
	/**Create a new configuration.Creating a configuration is different than applying a configuration.This is only for creation
	 * of configuration.
	 * User need to provide the newConfiguration here.
	 * @param clusterName
	 * @param newConfiguration
	 * @return response
	 */
	public AmbariAutomationResponse createConfiguration(String clusterName, String newConfiguration) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/configurations", clusterName));
		AmbariAutomationHttpPost request = new AmbariAutomationHttpPost(user, password, uri);
		StringEntity entity = null;
		try {
			entity = new StringEntity(newConfiguration);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
		
	}
	
	/**create and apply a configuration on a cluster in single call.
	 * @param clusterName
	 * @param newConfiguration
	 * @return response
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public AmbariAutomationResponse createApplyConfiguration(String clusterName, String type, String tag, String[]... properties) throws JsonProcessingException, IOException {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s", clusterName));
		AmbariAutomationHttpPut request = new AmbariAutomationHttpPut(user, password, uri);
		AmbariAutomationResponse response1 = viewConfigurationOfTypeTag(clusterName, type, "1");
		List<HashMap<String, Object>> items = response1.getBodyListByJsonPath("items");
		
		HashMap<String, Object> map = (HashMap<String, Object>)items.get(0).get("properties");
		for(String[] property : properties) {
			map.put(property[0], property[1]);
		}
		ObjectMapper objectMapper1 = new ObjectMapper();
		
		String newConfiguration1 = String.format("{"
				  + "\"Clusters\": {"
				  + "\"desired_config\":{"
				  + "\"type\": \"%s\","
				  + "\"tag\": \"%s\","
				  + "\"properties\": \"none\"}}}",type,tag);
		
		HashMap<String, HashMap<String, Object>> map1 = objectMapper1.readValue(newConfiguration1, HashMap.class);
		((HashMap) map1.get("Clusters").get("desired_config")).put("properties", map);	
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(objectMapper1.writeValueAsString(map1));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.setEntity(entity);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
		
	}
	
}
