package io.pivotal.ambari_automation.ambari;

import java.io.File;

import io.pivotal.ambari_automation.conf.AmbariConfiguration;
import io.pivotal.ambari_automation.util.SSHUtil;

import org.testng.log4testng.Logger;

public class AmbariMachine extends Machine {
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger.getLogger(AmbariMachine.class);

	public AmbariMachine(String externalAddress, int port, String login, String password) {
		super(externalAddress, port, login, password);
	}

	/**
	 * setting up the amabri repo
	 * @param url
	 *            url to download ambari.repo
	 * @return true if successful
	 */
	public boolean setUpAmbariRepo(String url) {
		log.info("setting up ambari.repo on " + this);
		String command = String.format("wget %s -O /etc/yum.repos.d/ambari.repo\n", url);

		String[] ret = SSHUtil.executeRemoteCommand(this.externalAddress, this.port,
				this.login, this.password, command, DEFAULT_TIMEOUT, false);
		if (!ret[0].equals("0"))
			throw new RuntimeException(ret[2]);
		command = "echo -e \"gpgcheck=0\" >> /etc/yum.repos.d/ambari.repo";
		ret = SSHUtil.executeRemoteCommand(this.externalAddress, this.port,
				this.login, this.password, command, DEFAULT_TIMEOUT, false);
		if (!ret[0].equals("0"))
			throw new RuntimeException(ret[2]);
		return true;
	}
	/**
	 * get ambari server rpm
	 * @param src
	 * @param dest
	 */
	public void getAmbariServerRpm(String src, String dest) {
		log.info("getting ambari server rpm on " + this);
		SSHUtil.upload(this.externalAddress, this.port, this.login, this.password, src, dest);
	}

	/**
	 * install the ambari server
	 * @return true if installed successfully
	 */
	public void installAmbariServer(AmbariConfiguration config) {
		log.info("installing ambari server on " + this);
		
		if (config.getProperty("repoUrl") != null && (config.getProperty("serverRpm") != null || config.getProperty("agentRpm") != null))
			throw new RuntimeException("please provide either repo url or the local rpm path");
		String command;
		String[] ret;
		if (config.getProperty("repoUrl") != null) {
			this.setUpAmbariRepo(config.getProperty("repoUrl"));
			command = String.format("yum -y install ambari-server-%s.*", config.getProperty("ambari-version", "1.7"));
			ret = SSHUtil.executeRemoteCommand(this.externalAddress, this.port, this.login,
					this.password, command, DEFAULT_TIMEOUT, false);
			if (!ret[0].equals("0"))
				throw new RuntimeException(ret[2]);		
		} else if (config.getProperty("serverRpm") != null) {
			SSHUtil.upload(this.externalAddress, this.port, this.login, this.password, config.getProperty("serverRpm"), "/tmp");
			String ambariServer = new File(config.getProperty("serverRpm")).getName();
			command = "yum -y install /tmp/" + ambariServer;
			ret = SSHUtil.executeRemoteCommand(this.externalAddress, this.port, this.login,
					this.password, command, DEFAULT_TIMEOUT, false);
			if (!ret[0].equals("0"))
				throw new RuntimeException(ret[2]);	
		} else {
			throw new RuntimeException("please provide either repo url or the local server rpm path");
		}
	}
	/**
	 * setup ambari Server
	 */
	public void setupAmbariServer() {
		log.info("setting up ambari server on " + this);
		String command;
		String[] ret; 
		command = "ambari-server setup -s";
		ret = SSHUtil.executeRemoteCommand(this.externalAddress, this.port, this.login, 
				this.password, command, DEFAULT_TIMEOUT, false);
		if (!ret[0].equals("0"))
			throw new RuntimeException(ret[2]);
	}
	
	/**
	 * start ambari server
	 */
	public void startAmbariServer() {
		log.info("starting ambari server on " + this);
		String command;
		String[] ret;
		command = "ambari-server start";
		ret = SSHUtil.executeRemoteCommand(externalAddress, port, login, password,
				command, DEFAULT_TIMEOUT, false);
		if (!ret[0].equals("0"))
			throw new RuntimeException(ret[2]);
	}
	/**
	 * install the ambari agent
	 * @return true if installed successfully
	 */
	public void installAmbariAgent(AmbariConfiguration config) {
		log.info("installing ambari agent on " + this);
		if (config.getProperty("repoUrl") != null && (config.getProperty("serverRpm") != null || config.getProperty("agentRpm") != null))
			throw new RuntimeException("please provide either repo url or the local rpm");
		String command;
		String[] ret;
		if (config.getProperty("repoUrl") != null) {
			this.setUpAmbariRepo(config.getProperty("repoUrl"));
			command = String.format("yum -y install ambari-agent-%s.*", config.getProperty("ambari-version", "1.7"));
			ret = SSHUtil.executeRemoteCommand(this.externalAddress, this.port, this.login,
					this.password, command, DEFAULT_TIMEOUT, false);
			if (!ret[0].equals("0"))
				throw new RuntimeException(ret[2]);		
		} else if (config.getProperty("agentRpm") != null) {
			SSHUtil.upload(this.externalAddress, this.port, this.login, this.password, config.getProperty("agentRpm"), "/tmp");
			String ambariAgent = new File(config.getProperty("agentRpm")).getName();
			command = "yum -y install /tmp/" + ambariAgent;
			ret = SSHUtil.executeRemoteCommand(this.externalAddress, this.port, this.login,
					this.password, command, DEFAULT_TIMEOUT, false);
			if (!ret[0].equals("0"))
				throw new RuntimeException(ret[2]);	
		} else {
			throw new RuntimeException("please provide either repo url or the local agent rpm path");
		}
	}
	/**
	 * setting up ambari agent
	 * @param ambariServerAddress
	 */
	public void setupAmbariAgent(String ambariServerAddress) {
		log.info("setting up ambari agent on " + this);

		String command = "yum -y install ambari-agent";
		String[] ret;
		command = String
				.format("sed -i -e 's/hostname=localhost/hostname=%s/g' /etc/ambari-agent/conf/ambari-agent.ini",
						ambariServerAddress);

		ret = SSHUtil.executeRemoteCommand(externalAddress, port, login, password,
				command, DEFAULT_TIMEOUT, false);
		if (!ret[0].equals("0"))
			throw new RuntimeException(ret[2]);
	}

	/**
	 * start ambari agent
	 */
	public void startAmbariAgent() {
		log.info("starting ambari agent on " + this);

		String command = "yum -y install ambari-agent";
		String[] ret;
		command = "ambari-agent start";
		ret = SSHUtil.executeRemoteCommand(externalAddress, port, login, password,
				command, DEFAULT_TIMEOUT, false);
		if (!ret[0].equals("0"))
			throw new RuntimeException(ret[2]);
	}
	
}
