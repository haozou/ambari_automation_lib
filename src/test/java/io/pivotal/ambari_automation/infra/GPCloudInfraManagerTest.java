//package io.pivotal.ambari_automation.infra;
//
//import io.pivotal.ambari_automation.AmbariAutomationRuntimeException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import mockit.Expectations;
//import mockit.Mocked;
//
//import org.testng.Assert;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.gopivotal.vcloudcli.cli.CliResult;
//import com.gopivotal.vcloudcli.cli.StartVAppCli;
//import com.gopivotal.vcloudcli.cli.VCloudCliOrganizationNotSpecifiedException;
//import com.gopivotal.vcloudcli.cli.VCloudCliVdcNotExistException;
//import com.gopivotal.vcloudcli.cli.VCloudCliVdcNotSpecifiedException;
//
//public class GPCloudInfraManagerTest {
//
//	private CliResult showCliResult;
//
//	@BeforeClass
//	public void setup(){
//		List<String> showCliResultOutput;
//		showCliResultOutput = new ArrayList<String>();
//		showCliResultOutput.add("10.103.222.1 centos64-1");
//		showCliResultOutput.add("10.103.222.2 centos64-2");
//		showCliResultOutput.add("10.103.222.3 centos64-3");
//		showCliResultOutput.add("10.103.222.4 centos64-4");
//		showCliResultOutput.add("10.103.222.5 centos64-5");
//		showCliResult = new CliResult(true, showCliResultOutput);
//
//	}
//
//	@Test
//	public void testProvisionMachinesPositive(
//			@Mocked final VCloudCliHelper mockedVCloudCliHelper
//			) throws IOException,
//			VCloudCliOrganizationNotSpecifiedException,
//			VCloudCliVdcNotSpecifiedException, VCloudCliVdcNotExistException,
//			Exception {
//
//		new Expectations() {{
//			new VCloudCliHelper(); result = mockedVCloudCliHelper;
//			mockedVCloudCliHelper.login(anyString, anyString, anyString);
//			mockedVCloudCliHelper.provisionVapp(anyString, anyString, anyString, anyString);
//			mockedVCloudCliHelper.login(anyString, anyString, anyString);
//			mockedVCloudCliHelper.show(anyString, anyString, anyString); result = showCliResult;
//		}};
//
//		GPCloudInfraManager gpCloudInfraManager = new GPCloudInfraManager("hd", "hd_orgvdc1", "user",
//				"pass", "name", "template", "create:start", null);
//		Assert.assertTrue(gpCloudInfraManager.provisionMachines());
//		HashMap<String, String> mappedExternalIPs = gpCloudInfraManager.getMappedExternalIPs();
//		Assert.assertEquals(mappedExternalIPs.get("${machine[0]}"), "10.103.222.1");
//
//	}
//
//	@Test(dependsOnMethods={"testProvisionMachinesPositive"})
//	public void testProvisionMachinesStartPositive(
//			@Mocked final VCloudCliHelper mockedVCloudCliHelper,
//			@Mocked final StartVAppCli mockedStart
//			) throws IOException,
//			VCloudCliOrganizationNotSpecifiedException,
//			VCloudCliVdcNotSpecifiedException, VCloudCliVdcNotExistException,
//			Exception {
//
//		new Expectations() {{
//			new VCloudCliHelper(); result = mockedVCloudCliHelper;
//			mockedVCloudCliHelper.login(anyString, anyString, anyString);
//			new StartVAppCli(); result = mockedStart;
//			mockedStart.execute(anyString, anyString); result = showCliResult;
////			mockedVCloudCliHelper.show(anyString, anyString, anyString); result = showCliResult;
//		}};
//
//		Assert.assertTrue(new GPCloudInfraManager("hd", "hd_orgvdc1", "user",
//				"pass", "name", "template", "start", null).provisionMachines());
//	}
//
//	@Test
//	public void testProvisionMachinesNeg(
//			@Mocked final VCloudCliHelper mockedVCloudCliHelper
//			) throws IOException,
//			VCloudCliOrganizationNotSpecifiedException,
//			VCloudCliVdcNotSpecifiedException, VCloudCliVdcNotExistException,
//			Exception {
//
//		final CliResult res = new CliResult(false, null);
//
//		new Expectations() {{
//			new VCloudCliHelper(); result = mockedVCloudCliHelper;
//			mockedVCloudCliHelper.login(anyString, anyString, anyString); result = new AmbariAutomationRuntimeException("GPCloud authentication failure. (some)", null);
////			mockedVCloudCliHelper.provisionVapp(anyString, anyString, anyString, anyString);
////			mockedStart.execute(anyString, anyString); result = res;
//		}};
//
//		try {
//			new GPCloudInfraManager("hd", "hd_orgvdc1", "user", "pass", "name",
//					"template", "create:start", null).provisionMachines();
//			Assert.fail();
//		} catch (Exception e) {
//			Assert.assertNotNull(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testBeforeExecutionNoEventParameters(){
//		GPCloudInfraManager infraManager = new GPCloudInfraManager("hd", "hd_orgvdc1", "user", "pass", "name",
//				"template", null, null);
//		infraManager.beforeExecution("user","pass"); // should not do anything
//
//	}
//
//	@Test
//	public void testBeforeExecutionNoBeforeExecution(){
//		GPCloudInfraManager infraManager = new GPCloudInfraManager("hd", "hd_orgvdc1", "user", "pass", "name",
//				"template", null, null);
//		infraManager.beforeExecution("user","pass"); // should not do anything
//
//	}
//
//
//
//	@Test
//	public void testBeforeExecutionWrongAction(){
//		GPCloudInfraManager infraManager = new GPCloudInfraManager("hd", "hd_orgvdc1", "user", "pass", "name",
//				"template", "revertVApp:sss", null);
//
//		
//		infraManager.beforeExecution("user","pass");
//	}
//
//	@Test
//	public void testBeforeExecution1(@Mocked final VCloudCliHelper mockedVCloudCliHelper){
//		new Expectations() {{
//			new VCloudCliHelper(); result = mockedVCloudCliHelper;
//			mockedVCloudCliHelper.login(anyString, anyString, anyString);
//			mockedVCloudCliHelper.revertVApp(anyString, anyString, anyString);
//			mockedVCloudCliHelper.startVApp(anyString, anyString, anyString);
//			mockedVCloudCliHelper.checkSshConnection(anyString, anyString, anyString, anyString, anyString);
//		}};
//
//		
//		GPCloudInfraManager infraManager = new GPCloudInfraManager("hd", "hd_orgvdc1", "user", "pass", "name",
//				"template", "revertVApp:startvapp:checkSsh", null);
//		infraManager.beforeExecution("user","pass");
//	}
//
//	@Test
//	public void testBeforeExecution2(@Mocked final VCloudCliHelper mockedVCloudCliHelper){
//		new Expectations() {{
//			new VCloudCliHelper(); result = mockedVCloudCliHelper;
//			mockedVCloudCliHelper.login(anyString, anyString, anyString);
//			mockedVCloudCliHelper.revertVApp(anyString, anyString, anyString);
//			mockedVCloudCliHelper.startVApp(anyString, anyString, anyString);
//		}};
//
//		GPCloudInfraManager infraManager = new GPCloudInfraManager("hd", "hd_orgvdc1", "user", "pass", "name",
//				"template", "revertVApp:startvapp", null);
//		infraManager.beforeExecution("user","pass");
//	}
//
//	@Test
//	public void testBeforeExecutionStartOnly(@Mocked final VCloudCliHelper mockedVCloudCliHelper){
//		new Expectations() {{
//			new VCloudCliHelper(); result = mockedVCloudCliHelper;
//			mockedVCloudCliHelper.login(anyString, anyString, anyString);
//			mockedVCloudCliHelper.startVApp(anyString, anyString, anyString);
//		}};
//
//		GPCloudInfraManager infraManager = new GPCloudInfraManager("hd", "hd_orgvdc1", "user", "pass", "name",
//				"template", "startvapp", null);
//		infraManager.beforeExecution("user","pass");
//	}
//
//}
