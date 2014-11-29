package io.pivotal.ambari_automation.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import expect4j.Expect4j;
import expect4j.ExpectUtils;

/**
 * Utility class to run SSH commands.
 * 
 * @author Hao Zou
 */
public final class SSHUtil {

	/**
	 * Private constructor.
	 */
	private SSHUtil() {
	}

	/**
	 * Utility method to call remote command.
	 * 
	 * @param host
	 *            host to connect.
	 * @param port
	 *            port to connect.
	 * @param login
	 *            login to connect.
	 * @param password
	 *            password.
	 * @param command
	 *            command to execute.
	 * @param timeout
	 *            timeout in seconds.
	 * @param silent
	 *            silent or not.
	 * @return String[] output of command.
	 */
	public static String[] executeRemoteCommand(final String host,
			final Integer port, final String login, final String password,
			final String command, final int timeout, boolean silent) {

		String stdout = null;
		String stderr = null;
		int ret = -1;

		final SSHClient ssh = new SSHClient();

		try {

			ssh.addHostKeyVerifier(  
				    new HostKeyVerifier() {
						@Override
						public boolean verify(String hostname, int port,
								PublicKey key) {
							return true;	// don't bother verifying  
						}  
				    }  
				);
			ssh.connect(host, port);
			ssh.authPassword(login, password);

			final Session session = ssh.startSession();
			final Command cmd = session.exec(command);

			if (silent) {
				stdout = IOUtils.readFully(cmd.getInputStream()).toString();
				stderr = IOUtils.readFully(cmd.getErrorStream()).toString();
				cmd.join(timeout, TimeUnit.SECONDS);
				ret = cmd.getExitStatus();
			} else {
				System.out.println(command);
				InputStream in = cmd.getInputStream();

				byte[] tmp = new byte[1024];
				while (true) {
					while (in.available() > 0) {
						int i = in.read(tmp, 0, 1024);
						if (i < 0)
							break;
						String out = new String(tmp, 0, i);
						stdout += out;
						System.out.print(out);
					}
					if (!cmd.isOpen()) {
						if (in.available() > 0)
							continue;
						stderr = IOUtils.readFully(cmd.getErrorStream()).toString();
						int status = cmd.getExitStatus();
						ret = status;
						System.out.println("exit-status: " + status);
						break;
					}
				}
			}

			session.close();
			ssh.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new String[] { String.valueOf(ret), stdout, stderr };
	}

	/**
	 * upload file to the remote host
	 * 
	 * @param host
	 *            host to connect.
	 * @param port
	 *            port to connect.
	 * @param login
	 *            login to connect.
	 * @param password
	 *            password.
	 * @param src
	 *            src file to upload
	 * @param dest
	 *            dest location to store file
	 * @return boolean
	 */
	public static boolean upload(final String host, final int port,
			final String login, final String password, final String src,
			final String dest) {

		SSHClient ssh = new SSHClient();

		try {
			ssh.addHostKeyVerifier(  
				    new HostKeyVerifier() {
						@Override
						public boolean verify(String hostname, int port,
								PublicKey key) {
							return true;	// don't bother verifying  
						}  
				    }  
				);
			ssh.connect(host, port);
			ssh.authPassword(login, password);

			ssh.newSCPFileTransfer().upload(new FileSystemFile(src), dest);
			ssh.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
	/**
	 * 
	 * @param host
	 *            host to connect
	 * @param port
	 *            port to connect
	 * @param login
	 *            login connect
	 * @param password
	 *            password to login
	 * @param timeout
	 *            timeout in seconds
	 * @param silent
	 * @return Expect4j instance
	 */
	public static Expect4j interactCommand(final String host, final Integer port,
			final String login, final String password, final String command,
			final int timeout) {

		Expect4j interact = null;
		try {
			interact = ExpectUtils.SSH(host, login, password, port);
			interact.setDefaultTimeout(timeout*1000);
			interact.send(command);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return interact;
	}
}
