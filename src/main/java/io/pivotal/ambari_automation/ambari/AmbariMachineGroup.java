package io.pivotal.ambari_automation.ambari;

import io.pivotal.ambari_automation.conf.AmbariConfiguration;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AmbariMachineGroup {
	private Set<AmbariMachine> machines;
	public AmbariMachineGroup(Set<AmbariMachine> machines) {
		super();
		this.machines = machines;
	}
	
	/**
	 * add machine to machine group
	 * @param machine
	 */
	public void addMachine(AmbariMachine machine) {
		this.machines.add(machine);
	}
	

	@Override
	public String toString() {
		String str = "[";
		int i = 0;
		for (AmbariMachine m : machines) {
			if (++i < machines.size())
				str += "\"" + m.getInternalAddress() + "\"" + ",";
			else
				str += "\"" + m.getInternalAddress() + "\"" + "]";
		}
		return str;
	}

	/**
	 * install the ambari agent
	 */
	public void installAmbariAgent(final AmbariConfiguration config) {
		ExecutorService executor = Executors.newFixedThreadPool(this.machines.size());
		for (final AmbariMachine machine : machines) {
				executor.execute(new Runnable() {
				@Override
				public void run() {
					machine.installAmbariAgent(config);
				}
			});
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
	}
	/**
	 * setup the ambari agent
	 */
	public void setupAmbariAgent(final String ambariServer) {
		ExecutorService executor = Executors.newFixedThreadPool(this.machines.size());
		for (final AmbariMachine machine : machines) {
				executor.execute(new Runnable() {
				@Override
				public void run() {
					machine.setupAmbariAgent(ambariServer);
				}
			});
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
	}
	/**
	 * start the ambari agent
	 */
	public void startAmbariAgent() {
		ExecutorService executor = Executors.newFixedThreadPool(this.machines.size());
		for (final AmbariMachine machine : machines) {
				executor.execute(new Runnable() {
				@Override
				public void run() {
					machine.startAmbariAgent();
				}
			});
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
	}
}
