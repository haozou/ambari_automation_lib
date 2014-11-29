package io.pivotal.ambari_automation;

public class AmbariAutomationRuntimeException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AmbariAutomationRuntimeException(String message, Throwable cause){
		super(message, cause);
	}

}
