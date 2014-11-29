//package io.pivotal.ambari_automation.infra;
//
//import io.pivotal.ambari_automation.AmbariAutomationRuntimeException;
//
//import java.net.SocketException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.testng.log4testng.Logger;
//
//import com.gopivotal.vcloudcli.cli.CliResult;
//import com.gopivotal.vcloudcli.cli.VCloudCliVdcNotExistException;
//import com.gopivotal.vcloudcli.ssh.SshClient;
//import com.gopivotal.vcloudcli.ssh.SshExecutionResult;
//import com.gopivotal.vcloudcli.vcloud.QueryApi;
//import com.gopivotal.vcloudcli.vcloud.VAppApi;
//import com.gopivotal.vcloudcli.vcloud.VCloudLoginApi;
//import com.gopivotal.vcloudcli.vcloud.VCloudLoginApi.LoginResult;
//import com.gopivotal.vcloudcli.vcloud.VdcApi;
//import com.gopivotal.vcloudcli.vcloud.model.VApp;
//import com.gopivotal.vcloudcli.vcloud.model.VM;
//import com.jcraft.jsch.JSchException;
//
///**
// * This class is a wrapper class of vcloudcli and provide thread safeness.
// */
//public class VCloudCliHelper {
//	private static final Logger log = Logger.getLogger(VCloudCliHelper.class);
//	
//	public VCloudCliHelper(){} // hidden constructor
//	private String authToken = null;
//	
//	public void login(String org, String userId, String password){
//		
//		if(authToken == null){
//			VCloudLoginApi api = new VCloudLoginApi();
//			LoginResult result = api.login(userId, password, org);
//	
//			if(!result.isSucceeded()){
//				throw new AmbariAutomationRuntimeException("GPCloud authentication failure. (" + userId + ")", null);
//			}
//
//			authToken = result.getvCloudAuthHeaderValue();
//
//		}
//	}
//
//	public void provisionVapp(String org, String vdc, String vappName, String vappTemplate){
//
//		VdcApi vdcApi = new VdcApi(authToken);
//
//		Map<String, String> orgNameUrlPairs = vdcApi.getOrgNameUrlPairs();
//		String orgUrl = orgNameUrlPairs.get(org);
//		Map<String, String> vdcNameUrlPairs = vdcApi.getVdcNameUrlPairs(orgUrl);
//
//		String vdcUrl = vdcNameUrlPairs.get(vdc);
//		if(vdcUrl == null){
//			throw new AmbariAutomationRuntimeException("vdc not found (" + vdc + ")", new VCloudCliVdcNotExistException(vdc));
//		}
//
//		String vAppTemplateUrl = vdcApi.getVAppTemplateUrl(vdcUrl, vappTemplate);
//		if(vAppTemplateUrl == null){
//			// In case user requesting a public template. It may not be accessible from vdc but public query.
//			QueryApi queryApi = new QueryApi(authToken);
//			vAppTemplateUrl = queryApi.getVAppTemplateUrl(vappTemplate);
//		}
//
//		VAppApi api = new VAppApi(authToken);
//
//		String vAppHref = api.createVApp(
//				vdcUrl
//				, vappName
//				, -1
//				, -1
//				, true
//				, ""
//				, vAppTemplateUrl);
//
//
//		// Wait until vapp status power on from "api.waitUntilVAppStatusPowerOn"
//		int count = 0;
//		while(!api.getVApp(vAppHref).isPowerOn()){
//			try {
//				Thread.sleep(20000);
//			} catch (InterruptedException e) {}
//			count++;
//			if(count > 30) break;
//		}
//
//	}
//
//	public CliResult show(String org, String vdc, String vappName){
//		
//		VdcApi vdcapi = new VdcApi(authToken, false);
//
//		Map<String, String> orgHrefs = vdcapi.getOrgNameUrlPairs();
//		String orgUrl = orgHrefs.get(org);
//		Map<String, String> vdcHrefs = vdcapi.getVdcNameUrlPairs(orgUrl);
//
//		String vdcUrl = vdcHrefs.get(vdc);
//		if(vdcUrl == null){
//			throw new AmbariAutomationRuntimeException("vdc not found (" + vdc + ")", new VCloudCliVdcNotExistException(vdc));
//		}
//
//
//		Map<String, String> vAppNameUrlPairs = vdcapi.getVAppNameUrlPairs(vdcUrl);
//		String vAppHref = vAppNameUrlPairs.get(vappName);
//
//		List<String> stdouts = new ArrayList<String>();
//
//		if(vAppHref == null){
//			stdouts.add("vApp " + vappName + " does not exist. Try list and find provisioned vApps.");
//			return new CliResult(false, stdouts);
//		}
//		
//
//		VAppApi api = new VAppApi(authToken);
//		VApp vApp = api.getVApp(vAppHref);
//		for(VM vm : vApp.getVMs()){
//			stdouts.add(vm.getExternalIp() + " " + vm.getName());
//		}
//		return new CliResult(true, stdouts);
//		
//	}
//	
//	public void revertVApp(String org, String vdc, String vAppName){
//		VdcApi vdcApi = new VdcApi(authToken);
//
//		/// THIS BLOCK TO GET vdcUrl. TODO it is repetitive, do something ///////////////
//		Map<String, String> orgHrefs = vdcApi.getOrgNameUrlPairs();
//		String orgUrl = orgHrefs.get(org);
//		Map<String, String> vdcHrefs = vdcApi.getVdcNameUrlPairs(orgUrl);
//
//		String vdcUrl = vdcHrefs.get(vdc);
//		if(vdcUrl == null){
//			throw new AmbariAutomationRuntimeException("vdc not found (" + vdc + ")", new VCloudCliVdcNotExistException(vdc));
//		}
//		//////////////////////////////////////////////////////////////////
//
//		Map<String, String> vAppNameUrlPair = vdcApi.getVAppNameUrlPairs(vdcUrl);
//
//
//		if(!vAppNameUrlPair.containsKey(vAppName)){
//			throw new AmbariAutomationRuntimeException(vAppName + " was not found", null);
//		}
//
//		VAppApi api = new VAppApi(authToken);
//		String vappUrl = vAppNameUrlPair.get(vAppName);
//		String revertTaskHref = api.revertVAppToCurrentSnapshot(vappUrl);
//		
//		int count = 0;
//		while(!api.getTask(revertTaskHref).isSuccess()){
//			try {
//				Thread.sleep(30000);
//			} catch (InterruptedException e) {}
//			count++;
//			if(count > 15) break;
//		}
//		
//	}
//
//	public void startVApp(String org, String vdc, String vAppName) {
//		VdcApi vdcApi = new VdcApi(authToken);
//
//		/// THIS BLOCK TO GET vdcUrl. TODO it is repetitive, do something ///////////////
//		Map<String, String> orgHrefs = vdcApi.getOrgNameUrlPairs();
//		String orgUrl = orgHrefs.get(org);
//		Map<String, String> vdcHrefs = vdcApi.getVdcNameUrlPairs(orgUrl);
//
//		String vdcUrl = vdcHrefs.get(vdc);
//		if(vdcUrl == null){
//			throw new AmbariAutomationRuntimeException("vdc not found (" + vdc + ")", new VCloudCliVdcNotExistException(vdc));
//		}
//		//////////////////////////////////////////////////////////////////
//
//		Map<String, String> vAppNameUrlPair = vdcApi.getVAppNameUrlPairs(vdcUrl);
//
//		if(!vAppNameUrlPair.containsKey(vAppName)){
//			throw new AmbariAutomationRuntimeException(vAppName + " was not found", null);
//		}
//
//		VAppApi api = new VAppApi(authToken);
//		String vappUrl = vAppNameUrlPair.get(vAppName);
//		api.poweronVApp(vappUrl);
//
//		int count = 0;
//		while(!api.getVApp(vappUrl).isPowerOn()){
//			try {
//				Thread.sleep(20000);
//			} catch (InterruptedException e) {}
//			count++;
//			if(count > 30) break;
//		}
//			
//	}
//
//	public boolean checkSshConnection(String org, String vdc, String vAppName, String user, String password) {
//		VdcApi vdcapi = new VdcApi(authToken, false);
//
//		/// THIS BLOCK TO GET vdcUrl. TODO it is repetitive, do something ///////////////
//		Map<String, String> orgHrefs = vdcapi.getOrgNameUrlPairs();
//		String orgUrl = orgHrefs.get(org);
//		Map<String, String> vdcHrefs = vdcapi.getVdcNameUrlPairs(orgUrl);
//
//		String vdcUrl = vdcHrefs.get(vdc);
//		if(vdcUrl == null){
//			throw new AmbariAutomationRuntimeException("vdc not found (" + vdc + ")", new VCloudCliVdcNotExistException(vdc));
//		}
//		//////////////////////////////////////////////////////////////////
//
//		Map<String, String> vAppHrefList = vdcapi.getVAppNameUrlPairs(vdcUrl);
//		String vAppHref = vAppHrefList.get(vAppName);
//		if(vAppHref == null){
//			throw new AmbariAutomationRuntimeException("vapp not found (" + vAppName + ")", null);
//		}
//
//		VAppApi api = new VAppApi(authToken);
//		VApp vApp = api.getVApp(vAppHref);
//
//		final int MAX = 120;
//		for(VM vm : vApp.getVMs()){
//			log.info("vm.getExternalIp(): " + vm.getExternalIp() + " user: " + user + " password: " + password);
//			SshClient client = new SshClient(vm.getExternalIp(), user, password, 22);
//			client.setCommand("ls /");
//
//			int count = 0;
//			while(count < MAX){
//				log.info(" accessing " + vm.getExternalIp());
//				
//				SshExecutionResult result = execute(client);
//				
//				if(result != null && result.getExitCode()==0){
//					log.info(vm.getExternalIp() + " Succeeded");
//					break;
//				}
//				log.info("trying again...");
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {}
//				count++;
//			}
//			if (count == MAX) 
//				log.info(vm.getExternalIp() + " Failed");
//		}
//
//		return false;
//
//	}
//
//	/**
//	 * Made this since Jsch might have a concurrent execution issue.
//	 * 
//	 * TODO fix to optimize this.
//	 * @throws JSchException 
//	 * 
//	 */
//	private  synchronized SshExecutionResult execute(SshClient client) {
//		try {
//			return client.call();
//		} 
//		catch (Exception e) {
//			if (e.getCause() != null && e.getCause().getCause() instanceof SocketException)
//				return null;
//			throw new AmbariAutomationRuntimeException(e.getMessage(), e);
//		}
//	}
//}
