package io.pivotal.ambari_automation.infra;

import java.util.HashMap;
import java.util.List;

/**
 * interface for infra access
 * @author Hao Zou
 *
 */
public interface InfraManager {


	// TODO let's return ProvisionResult instead of a boolean value
	public boolean provisionMachines() throws Exception;

	/**
	 * Define infra specific behavior before an execution (TestNG's suite) starts
	 */
	public void beforeExecution(String sshUser, String sshPassword);

	/**
	 * Define infra specific behavior after an execution (TestNG's suite) ends
	 */
	public void afterExecution(String sshUser, String sshPassword);


	/**
	 * return the external IPs
	 * @return
	 */
	public HashMap<String, String> getMappedExternalIPs();
	public List<String> getExternalIPsOfProvisionedVMs();

}
