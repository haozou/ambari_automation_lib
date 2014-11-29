//package io.pivotal.ambari_automation.infra;
//
//import io.pivotal.ambari_automation.AmbariAutomationRuntimeException;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.testng.log4testng.Logger;
//
//import com.gopivotal.vcloudcli.cli.VCloudCliOrganizationNotSpecifiedException;
//import com.gopivotal.vcloudcli.cli.VCloudCliVdcNotExistException;
//import com.gopivotal.vcloudcli.cli.VCloudCliVdcNotSpecifiedException;
//
//public class DCloudInfraManager implements InfraManager {
//	private static final String DCLOUD_CLUSTER_ID = "ambari_automation_cluster1";
//
//
//	private String dcloudConfigFile;
//	private boolean doProvision;
//
//
//	public VCloudCliHelper vcloudCliHelper = new VCloudCliHelper();
//
//	private static final Logger log = Logger.getLogger(DCloudInfraManager.class);
//
////	// TODO This is required to be loaded by InfraManagerLoader. This should be smarter to instantiate without a default constructor.
////	public DCloudInfraManager(){}
//
//	public DCloudInfraManager(String dcloudConfigFile, boolean doProvision) {
//		super();
//		this.dcloudConfigFile = dcloudConfigFile;
//		this.doProvision = doProvision;
//	}
//
//
//	@Override
//	public boolean provisionMachines()
//			throws VCloudCliOrganizationNotSpecifiedException,
//			VCloudCliVdcNotSpecifiedException, VCloudCliVdcNotExistException,
//			Exception {
//
//		Process p = Runtime.getRuntime().exec(new String[]{"dcloud","destroy", DCLOUD_CLUSTER_ID});
//        p.waitFor();
//
//		p = Runtime.getRuntime().exec(new String[]{"dcloud","create", dcloudConfigFile});
//		if(p.waitFor() == 0)
//			return true;
//		else
//			return false;
//
//	}
//
//	public List<String> getExternalIPsOfProvisionedVMs(){
//		Process p;
//        List<String> stdout = new ArrayList<>();
//		try {
//			p = Runtime.getRuntime().exec(new String[]{"dcloud","list", DCLOUD_CLUSTER_ID});
//			// stdout
//			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			String line = null;
//			while ((line = br.readLine()) != null){
//				stdout.add(line);
//			}
//			p.waitFor();
//		} catch (IOException e) {
//			throw new AmbariAutomationRuntimeException(e.getMessage(), e);
//		} catch (InterruptedException e) {
//			throw new AmbariAutomationRuntimeException(e.getMessage(), e);
//		}
//
//		return stdout;
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
//	@Override
//	public void beforeExecution(String sshUser, String sshPassword) {
//		// TODO implement this
//		if(!doProvision){
//			return;
//		}
//
//		try {
//			this.provisionMachines();
//		} catch (Exception e) {
//			e.printStackTrace();
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
