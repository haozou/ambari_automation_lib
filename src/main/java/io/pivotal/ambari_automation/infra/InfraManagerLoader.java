package io.pivotal.ambari_automation.infra;

import io.pivotal.ambari_automation.AmbariAutomationRuntimeException;

public class InfraManagerLoader {
	public static InfraManager load(String className){
		try {
			Class<?> c = Class.forName(className);
			return (InfraManager) c.newInstance();
		} catch (ClassNotFoundException e) {
			throw new AmbariAutomationRuntimeException(e.getMessage(), e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new AmbariAutomationRuntimeException(e.getMessage(), e);	
		}

	}

}
