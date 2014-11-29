package io.pivotal.ambari_automation.infra;

import java.util.HashMap;
import java.util.List;

public class TestInfraManager implements InfraManager {
	private HashMap<String, String> mappedExternalIPs;

	@Override
	public boolean provisionMachines() throws Exception {
		return false;
	}



	public void setExternalMappedIPs(HashMap<String, String> a) {
		mappedExternalIPs = a;
	}

	@Override
	public HashMap<String, String> getMappedExternalIPs() {
		return mappedExternalIPs;
	}

	@Override
	public void beforeExecution(String sshUser, String sshPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterExecution(String sshUser, String sshPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getExternalIPsOfProvisionedVMs() {
		// TODO Auto-generated method stub
		return null;
	}



}
