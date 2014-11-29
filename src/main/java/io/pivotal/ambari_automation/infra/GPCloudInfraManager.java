//package io.pivotal.ambari_automation.infra;
//
//import io.pivotal.ambari_automation.AmbariAutomationRuntimeException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.testng.log4testng.Logger;
//
//import com.gopivotal.vcloudcli.cli.CliResult;
//import com.gopivotal.vcloudcli.cli.StartVAppCli;
//import com.gopivotal.vcloudcli.cli.VCloudCliOrganizationNotSpecifiedException;
//import com.gopivotal.vcloudcli.cli.VCloudCliVdcNotExistException;
//import com.gopivotal.vcloudcli.cli.VCloudCliVdcNotSpecifiedException;
//
///**
// * Implementaion for GPCloud
// * 
// * @author Hao Zou
// *
// */
//public class GPCloudInfraManager implements InfraManager {
//	/**
//	 * vcloud org
//	 */
//	private String org;
//	/**
//	 * vcloud vdc
//	 */
//	private String vdc;
//	/**
//	 * vcloud user
//	 */
//	private String vcloud_user;
//	/**
//	 * vcloud password
//	 */
//	private String vcloud_password;
//	/**
//	 * vcloud action
//	 */
//	private String vapp_action;
//	/**
//	 * vcloud post action
//	 */
//	private String vapp_post_action;
//	/**
//	 * vapp name
//	 */
//	private String vapp_name;
//	/**
//	 * vapp template
//	 */
//	private String vapp_template;
//	
//
//	private List<String> externalIPsOfProvisionedVMs = null;
//	
//	public VCloudCliHelper vcloudCliHelper = new VCloudCliHelper();
//	
//	/**
//	 * Logger instance for this class.
//	 */
//	private static final Logger log = Logger.getLogger(GPCloudInfraManager.class);
//
//	// TODO This is required to be loaded by InfraManagerLoader. This should be smarter to instantiate without a default constructor.
//	public GPCloudInfraManager(){}
//
//	/**
//	 * constructor
//	 * 
//	 * @param vcloud_user
//	 * @param vcloud_password
//	 * @param vapp_action
//	 * @param vpp_name
//	 * @param vapp_template
//	 */
//	
//	public GPCloudInfraManager(String vcloud_org, String vcloud_vdc,
//			String vcloud_user, String vcloud_password, String vpp_name,
//			String vapp_template, String vapp_action, String vapp_post_action) {
//		super();
//		this.org = vcloud_org;
//		this.vdc = vcloud_vdc;
//		this.vcloud_user = vcloud_user;
//		this.vcloud_password = vcloud_password;
//		this.vapp_name = vpp_name;
//		this.vapp_template = vapp_template;
//		this.vapp_action = vapp_action;
//		this.vapp_post_action = vapp_post_action;
//	}
//
//
//	/**
//	 * @deprecated this method is currently not working.
//	 * provision the machines
//	 */
//	@Override
//	@Deprecated
//	public boolean provisionMachines()
//			throws VCloudCliOrganizationNotSpecifiedException,
//			VCloudCliVdcNotSpecifiedException, VCloudCliVdcNotExistException,
//			Exception {
//		CliResult result;
//		
//		vcloudCliHelper.login(this.org, this.vcloud_user, this.vcloud_password);
//
//		if ("create:start".equals(this.vapp_action)) {
//			vcloudCliHelper.provisionVapp(this.org, this.vdc, this.vapp_name, this.vapp_template);
//
//		} else if ("revert:start".equals(this.vapp_action)) {
//			// TODO: revert vm and start
//		} else if ("start".equals(this.vapp_action)) {
//			result = new StartVAppCli().execute("-n", this.vapp_name);
//			if (!result.isSucceeded())
//				throw new Exception(result.getStdouts().toString());
//		} else {
//			log.warn("no action defined");
//		}
//
//		return true;
//	}
//
//	public List<String> getExternalIPsOfProvisionedVMs(){
//		if(externalIPsOfProvisionedVMs == null){
//			// Cache external IPs of provisioned VMs
//			vcloudCliHelper.login(this.org, this.vcloud_user, this.vcloud_password);
//			CliResult result = vcloudCliHelper.show(this.org, this.vdc, this.vapp_name);
//			if (!result.isSucceeded())
//				throw new AmbariAutomationRuntimeException(result.getStdouts().toString(), null);
//
//			externalIPsOfProvisionedVMs = new ArrayList<>();
//			for (String machine : result.getStdouts()) {
//				externalIPsOfProvisionedVMs.add(machine.split(" ")[0]); // this extract an IP and adds the IP.
//			}
//		}
//		return externalIPsOfProvisionedVMs;
//		
//	}
//
//	@Override
//	public HashMap<String, String> getMappedExternalIPs() {
//		HashMap<String, String> ips = new HashMap<>();
//		for(int i = 0; i < getExternalIPsOfProvisionedVMs().size(); i++){
//			ips.put("${machine[" + i + "]}", getExternalIPsOfProvisionedVMs().get(i));
//		}
//		return ips;
//	}
//
//	/**
//	 * @param params beforeExecution
//	 * 
//	 * 
//     *  <vapp-action>createVapp or revertVApp:startVApp:checkSsh</vapp-action>
//     *  
//	 */
//	@Override
//	public void beforeExecution(String sshUser, String sshPassword) {
//		if(this.vapp_action == null) {
//			log.warn("no action defined");
//			return;
//		}
//
//		String[] params = this.vapp_action.split(":");
//
//		for (String param : params) {
//
//			if(!"createvapp".equalsIgnoreCase(param) && !"revertvapp".equalsIgnoreCase(param) && !"startvapp".equalsIgnoreCase(param) && !"checkssh".equalsIgnoreCase(param)){
//				log.warn("no supported action defined, skip beforeExecution! (only support createVapp or revertVApp:startVApp:checkSsh now)");
//				return;
//			}
//
//		}
//
//		vcloudCliHelper.login(this.org, this.vcloud_user, this.vcloud_password);
//
//		for (Object paramObject : params) {
//			String paramString = (String)paramObject;
//			
//			if ("createvapp".equalsIgnoreCase(paramString)) {
//				log.info("Creating vapp " + vapp_name);
//				vcloudCliHelper.provisionVapp(org, vdc, vapp_name, vapp_template);
//				log.info("Created vapp " + vapp_name);
//			} else if("revertvapp".equalsIgnoreCase(paramString)){
//				log.info("Reverting vapp " + vapp_name);
//				vcloudCliHelper.revertVApp(org, vdc, vapp_name);
//				log.info("Reverted vapp " + vapp_name);
//			}
//
//			if("startvapp".equalsIgnoreCase(paramString)){
//				log.info("Starting vapp " + vapp_name);
//				vcloudCliHelper.startVApp(org, vdc, vapp_name);
//				log.info("Started vapp " + vapp_name);
//			}
//
//			if("checkssh".equalsIgnoreCase(paramString)){
//				log.info("Ssh checking on " + vapp_name);
//				vcloudCliHelper.checkSshConnection(
//						org, 
//						vdc, 
//						vapp_name, 
//						sshUser, 
//						sshPassword);
//				log.info("Ssh checked on " + vapp_name);
//			}
//
//		}
//
//	}
//
//	@Override
//	public void afterExecution(String sshUser, String sshPassword) {
//		// TODO implement this
//		
//	}
//	
//}
