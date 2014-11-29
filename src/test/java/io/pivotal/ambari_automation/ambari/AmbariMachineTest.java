package io.pivotal.ambari_automation.ambari;

import io.pivotal.ambari_automation.util.SSHUtil;
import mockit.Expectations;
import mockit.Mocked;

import org.testng.Assert;
import org.testng.annotations.Test;

import expect4j.Expect4j;

public class AmbariMachineTest {
	@Test
	public void testSetUpAmbariRepoPos(@Mocked final SSHUtil mockUtil) {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "0", "good", "" };
			}
		};

		Assert.assertEquals(
				new AmbariMachine("some host", 22, "user", "pass").setUpAmbariRepo("url"), true);
	}

	@Test
	public void testSetUpAmbariRepoNeg(@Mocked final SSHUtil mockUtil) {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "1", "bad", "bad" };
			}
		};
		try {
			new AmbariMachine("some host", 22, "user", "pass").setUpAmbariRepo("url");
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("bad"));
		}
	}


	@Test
	public void testSetupAmbariServerPos(
			@Mocked final SSHUtil mockUtil, @Mocked final Expect4j mockex)
			throws Exception {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "0", "good", "" };
			}
		};
		new AmbariMachine("some host", 22, "user", "pass").setupAmbariServer();
	}
	@Test
	public void testSetupAmbariServerNeg(
			@Mocked final SSHUtil mockUtil, @Mocked final Expect4j mockex)
			throws Exception {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "1", "good", "bad" };
			}
		};

		try {
			new AmbariMachine("some host", 22, "user", "pass").setupAmbariServer();
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("bad"));
		}
	}
		
	@Test
	public void testStartlAmbariServerPos(
			@Mocked final SSHUtil mockUtil, @Mocked final Expect4j mockex)
			throws Exception {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "0", "good", "" };
			}
		};
		new AmbariMachine("some host", 22, "user", "pass").startAmbariServer();
	}
	
	@Test
	public void testStartAmbariServerNeg(
			@Mocked final SSHUtil mockUtil, @Mocked final Expect4j mockex)
			throws Exception {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "1", "good", "bad" };
			}
		};

		try {
			new AmbariMachine("some host", 22, "user", "pass").startAmbariServer();
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("bad"));
		}
	}
	
	@Test
	public void testSetupAmbariAgentPos(
			@Mocked final SSHUtil mockUtil, @Mocked final Expect4j mockex)
			throws Exception {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "0", "good", "" };
			}
		};
		new AmbariMachine("some host", 22, "user", "pass").setupAmbariAgent("some url");
	}
	
	@Test
	public void testSetupAmbariAgentNeg(
			@Mocked final SSHUtil mockUtil, @Mocked final Expect4j mockex)
			throws Exception {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "1", "good", "bad" };
			}
		};

		try {
			new AmbariMachine("some host", 22, "user", "pass").setupAmbariAgent("url");
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("bad"));
		}
	}
	
	@Test
	public void testStartAmbariAgentPos(
			@Mocked final SSHUtil mockUtil, @Mocked final Expect4j mockex)
			throws Exception {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "0", "good", "" };
			}
		};
		new AmbariMachine("some host", 22, "user", "pass").startAmbariAgent();
	}
	
	@Test
	public void testStartAmbariAgentNeg(
			@Mocked final SSHUtil mockUtil, @Mocked final Expect4j mockex)
			throws Exception {
		new Expectations() {
			{
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, true);
				result = new String[] { "0", "good", "" };
				SSHUtil.executeRemoteCommand(anyString, anyInt, anyString,
						anyString, anyString, anyInt, false);
				result = new String[] { "1", "good", "bad" };
			}
		};

		try {
			new AmbariMachine("some host", 22, "user", "pass").startAmbariAgent();
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("bad"));
		}
	}
}

