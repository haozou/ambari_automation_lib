package io.pivotal.ambari_automation.ambari;

import org.testng.log4testng.Logger;

import io.pivotal.ambari_automation.util.SSHUtil;
import expect4j.Expect4j;

public class Machine {
	protected String internalAddress;
	protected String externalAddress;
	protected int port;
	protected String login;
	protected String password;
	protected static final int DEFAULT_TIMEOUT = 600;
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger.getLogger(Machine.class);

	public Machine(String externalAddress, int port, String login, String password) {
		super();
		
		this.externalAddress = externalAddress;
		this.port = port;
		this.login = login;
		this.password = password;
		this.internalAddress = this.obtainFQDN();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((externalAddress == null) ? 0 : externalAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Machine other = (Machine) obj;
		if (externalAddress == null) {
			if (other.externalAddress != null)
				return false;
		} else if (!externalAddress.equals(other.externalAddress))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Machine (externalAddress=" + externalAddress + ")";
	}

	public String getExternalAddress() { return externalAddress; }
	public void setExternalAddress(String externalAddress) { this.externalAddress = externalAddress; }

	public String getInternalAddress() {
		return this.internalAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * Sets up requirements to execute a command remotely.
	 * 
	 * @param command
	 *            Command to execute.
	 * @param timeout
	 *            command to timeout
	 * @param silent
	 *            silent or not
	 * @return String result in case of success and null otherwise.
	 */
	public String[] runCommand(final String command, int timeout, boolean silent) {
		String[] result = null;
		result = SSHUtil.executeRemoteCommand(externalAddress, port, login, password,
				command, timeout, silent);
		return result;
	}

	/**
	 * Sets up requirements to execute a command remotely.
	 * 
	 * @param command
	 *            Command to execute.
	 * @param silent
	 *            silent or not
	 * @return String result in case of success and null otherwise.
	 */
	public String[] runCommand(final String command, boolean silent) {
		String[] result = null;
		result = SSHUtil.executeRemoteCommand(externalAddress, port, login, password,
				command, DEFAULT_TIMEOUT, silent);
		return result;
	}

	/**
	 * upload file to the remote host
	 * 
	 * @param src
	 *            src file to be sent
	 * @param dest
	 *            dest location to receive the file
	 * @return boolean
	 */
	public boolean upload(final String src, final String dest) {
		return SSHUtil.upload(externalAddress, port, login, password, src, dest);
	}

	/**
	 * 
	 * @param command
	 *            command to execute
	 * @param timeout
	 *            command to timeout
	 * @return Expect4j instance
	 */
	public Expect4j runInteractiveCommand(final String command, int timeout) {
		Expect4j interact = SSHUtil.interactCommand(externalAddress, port, login,
				password, command, timeout);
		return interact;
	}

	/**
	 * 
	 * @param command
	 *            command to execute
	 * @return Expect4j instance
	 */
	public Expect4j runInteractiveCommand(final String command) {
		Expect4j interact = SSHUtil.interactCommand(externalAddress, port, login,
				password, command, DEFAULT_TIMEOUT);
		return interact;
	}
	
	/**
	 * disabling the tty required
	 * @return boolean
	 */
	public boolean disableSudoTtyRequire() {
		log.info("disabling the tty required...");
		String command = "sed -i -e 's/Defaults *requiretty/#Defaults   requiretty/g' /etc/sudoers";
		String[] result = this.runCommand(command, true);
		return result[0].equals("0") ? true : false;
	}
	
	/**
	 * enabling the tty required
	 * @return boolean
	 */
	public boolean enableSudoTtyRequire() {
		log.info("enabling the tty required...");
		String command = "sed -i -e 's/#Defaults *requiretty/Defaults   requiretty/g' /etc/sudoers";
		String[] result = this.runCommand(command, true);
		return result[0].equals("0") ? true : false;
	}

	/**
	 * 
	 * obtain the fqdn of the host, used for initializing
	 * @return fqdn for the host
	 */
	protected String obtainFQDN() {
		String command = "hostname -f";
		String[] ret = SSHUtil.executeRemoteCommand(externalAddress, port, login, password, command, DEFAULT_TIMEOUT, true);
		if (!ret[0].equals("0"))
			throw new RuntimeException(ret[1]);
		return ret[1].trim();
	}
	/**
	 * get private ssh key
	 * @return
	 */
	public String getSshKey() {
		String command = "cat ~/.ssh/id_rsa";
		String[] ret = SSHUtil.executeRemoteCommand(externalAddress, port, login, password, command, DEFAULT_TIMEOUT, true);
		if (!ret[0].equals("0"))
			throw new RuntimeException(ret[1]);
		return ret[1].trim();
		
	}

}
