package io.pivotal.ambari_automation.ambari.restapi;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class AmbariAutomationHttpClient {

    // This stops irritating INFO messages coming out
    static {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
    }

	private HttpClient client;

	public AmbariAutomationHttpClient(){
		// Install the all-trusting trust manager
		SSLContext sc;
	    try {
			sc = SSLContext.getInstance("SSL");
		    sc.init(new KeyManager[0], new TrustManager[]{ new SomeX509TrustManager() }, new SecureRandom());
//		    SSLContext.setDefault(sc);
//		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//		    HttpsURLConnection.setDefaultHostnameVerifier(new SomeHostnameVerifier());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
	      throw new RuntimeException(e);
	    }

		client = HttpClientBuilder
				.create()
				.setHostnameVerifier(new SomeX509HostnameVerifier())
				.setSslcontext(sc)
				.build();

	}
	
	// TODO log should use log4j?
	public AmbariAutomationResponse access(HttpRequestBase request, boolean showLog) {
		if(showLog){
			System.out.println("Request URL=" + request.getRequestLine().getUri());
			System.out.println("Headers:");
			for(Header header : request.getAllHeaders()){
				System.out.println(header.getName() + ":" + header.getValue());
			}
		}

		HttpResponse response;
		try {
			response = client.execute(request);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		int statusCode = response.getStatusLine().getStatusCode();

		Header[] headers = response.getAllHeaders();

		String responseBody;
		try {
			responseBody = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
	    }
		
		if(showLog){
			System.out.println("Response:");
			System.out.println("Status=" + statusCode);
			System.out.println(responseBody);
		}

		return new AmbariAutomationResponse(statusCode, responseBody, headers);
		
	}

	public static class SomeX509HostnameVerifier implements X509HostnameVerifier{
		@Override public boolean verify(String hostname, SSLSession session) { return true; }
		@Override public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException { }
		@Override public void verify(String host, X509Certificate cert) throws SSLException { }
		@Override public void verify(String host, SSLSocket ssl) throws IOException { }
	}
	
	public static class SomeX509TrustManager implements X509TrustManager{
	    @Override public X509Certificate[] getAcceptedIssuers(){ return new X509Certificate[]{}; }
	    @Override public void checkClientTrusted(X509Certificate[] certs, String authType){}
	    @Override public void checkServerTrusted(X509Certificate[] certs, String authType){}
	}
	
	public static class SomeHostnameVerifier implements HostnameVerifier{
		@Override public boolean verify(String hostname, SSLSession session) { return true; }
	}

}
