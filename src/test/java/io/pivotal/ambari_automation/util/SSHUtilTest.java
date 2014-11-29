package io.pivotal.ambari_automation.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import mockit.Expectations;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.scp.SCPFileTransfer;

import org.junit.Assert;
import org.testng.annotations.Test;

import expect4j.Expect4j;
import expect4j.ExpectUtils;
import mockit.Mocked;

public class SSHUtilTest {
	@Test
	public void testExecuteCommandOnRemotePositive(
			@Mocked final SSHClient mockedSshClient,
			@Mocked final Session mockedSession, @Mocked final Command mockedCmd)
			throws IOException {
		new Expectations() {
			{
				new SSHClient();
				mockedSshClient.addHostKeyVerifier((HostKeyVerifier)any);
				mockedSshClient.connect(anyString, anyInt);
				mockedSshClient.authPassword(anyString, anyString);
				mockedSshClient.startSession();
				result = mockedSession;
				mockedSession.exec(anyString);
				result = mockedCmd;
				mockedCmd.getInputStream();
				IOUtils.readFully(new ByteArrayInputStream("good".getBytes()))
						.toString();
				result = "good";
				mockedCmd.getErrorStream();
				IOUtils.readFully(new ByteArrayInputStream("".getBytes()))
						.toString();
				result = "";
				mockedCmd.join(anyInt, TimeUnit.SECONDS);
				mockedCmd.getExitStatus();
				result = 0;
				mockedSession.close();
				
				mockedSshClient.close();
			}
		};

		String[] result = SSHUtil.executeRemoteCommand("real.host.com", 22,
				"root", "password", "some_command", 0, true);
		Assert.assertEquals(Integer.parseInt(result[0]), 0);
		Assert.assertEquals(result[1], "good");
		Assert.assertEquals(result[2], "");
	}

	@Test
	public void testExecuteCommandOnRemoteAuthFailure(
			@Mocked final SSHClient mockedSshClient) throws IOException {
		new Expectations() {
			{
				new SSHClient();
				mockedSshClient.addHostKeyVerifier((HostKeyVerifier)any);
				mockedSshClient.connect(anyString, anyInt);
				mockedSshClient.authPassword(anyString, anyString);
				result = new UserAuthException("some auth error");
			}
		};

		try {
			SSHUtil.executeRemoteCommand("real.host.com", 22, "root",
					"wrong_password", "some_command", 0, true);
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("some auth error"));
		}
	}

	@Test
	public void testExecuteCommandOnRemoteWrongHost(
			@Mocked final SSHClient mockedSshClient) throws IOException {
		new Expectations() {
			{
				new SSHClient();
				mockedSshClient.addHostKeyVerifier((HostKeyVerifier)any);
				mockedSshClient.connect(anyString, anyInt);
				result = new IOException("host not found");
			}
		};

		try {
			SSHUtil.executeRemoteCommand("fake.host.com", 22, "root",
					"password", "some_command", 0, true);
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("host not found"));
		}
	}

	@Test
	public void testUploadPositive(@Mocked final SSHClient mockedSshClient,
			@Mocked final SCPFileTransfer mockedScpFileTransfer)
			throws IOException {
		new Expectations() {
			{
				new SSHClient();
				mockedSshClient.addHostKeyVerifier((HostKeyVerifier)any);
				mockedSshClient.connect(anyString, anyInt);
				mockedSshClient.authPassword(anyString, anyString);
				new FileSystemFile(anyString);
				mockedSshClient.newSCPFileTransfer();
				result = mockedScpFileTransfer;
				mockedScpFileTransfer.upload((FileSystemFile) any, anyString);

				
				mockedSshClient.close();
			}
		};

		Assert.assertTrue(SSHUtil.upload("real.host.com", 80, "root",
				"password", "/some", "/some"));
	}

	@Test
	public void testUploadAuthFailure(@Mocked final SSHClient mockedSshClient)
			throws IOException {
		new Expectations() {
			{
				new SSHClient();
				mockedSshClient.addHostKeyVerifier((HostKeyVerifier)any);
				mockedSshClient.connect(anyString, anyInt);
				mockedSshClient.authPassword(anyString, anyString);
				result = new UserAuthException("some auth error");
			}
		};

		try {
			SSHUtil.upload("real.host.com", 80, "root", "wrong_password",
					"/some", "/some");
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("some auth error"));
		}
	}

	@Test
	public void testUploadWrongHost(@Mocked final SSHClient mockedSshClient)
			throws IOException {
		new Expectations() {
			{
				new SSHClient();
				mockedSshClient.addHostKeyVerifier((HostKeyVerifier)any);
				mockedSshClient.connect(anyString, anyInt);
				result = new IOException("host not found");
			}
		};

		try {
			SSHUtil.upload("fake.host.com", 80, "root", "password", "/some",
					"/some");
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("host not found"));
		}
	}

	@Test
	public void testUploadWrongFile(@Mocked final SSHClient mockedSshClient,
			@Mocked final SCPFileTransfer mockedScpFileTransfer)
			throws IOException {
		new Expectations() {
			{
				new SSHClient();
				mockedSshClient.addHostKeyVerifier((HostKeyVerifier)any);
				mockedSshClient.connect(anyString, anyInt);
				mockedSshClient.authPassword(anyString, anyString);
				new FileSystemFile(anyString);
				mockedSshClient.newSCPFileTransfer();
				result = mockedScpFileTransfer;
				mockedScpFileTransfer.upload((FileSystemFile) any, anyString);
				result = new IOException("file not found");
			}
		};

		try {
			SSHUtil.upload("real.host.com", 80, "root", "password", "/fake",
					"/some");
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("file not found"));
		}
	}

	@Test
	public void testInteractCommandPositive(
			@Mocked final Expect4j mockedExpect4j,
			@Mocked final ExpectUtils mockedUtil) throws Exception {
		new Expectations() {
			{

				ExpectUtils.SSH(anyString, anyString, anyString, anyInt);
				result = mockedExpect4j;
				mockedExpect4j.setDefaultTimeout(anyInt * 1000);
				mockedExpect4j.send(anyString);
			}
		};

		Assert.assertNotNull(SSHUtil.interactCommand("real.host.com", 22,
				"root", "password", "some_command", 0));
	}

	@Test
	public void testInteractCommandNoHost(
			@Mocked final Expect4j mockedExpect4j,
			@Mocked final ExpectUtils mockedUtil) throws Exception {
		new Expectations() {
			{

				ExpectUtils.SSH(anyString, anyString, anyString, anyInt);
				result = new Exception("no host");
			}
		};
		try {
			SSHUtil.interactCommand("fake.host.com", 22, "root", "password",
					"some_command", 0);
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("no host"));
		}
	}

	@Test
	public void testInteractCommandWrongCommand(
			@Mocked final Expect4j mockedExpect4j,
			@Mocked final ExpectUtils mockedUtil) throws Exception {
		new Expectations() {
			{

				ExpectUtils.SSH(anyString, anyString, anyString, anyInt);
				result = mockedExpect4j;
				mockedExpect4j.setDefaultTimeout(anyInt * 1000);
				mockedExpect4j.send(anyString);
				result = new IOException("wrong command");
			}
		};
		try {
			SSHUtil.interactCommand("real.host.com", 22, "root", "password",
					"some_command", 0);
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("wrong command"));
		}
	}
}
