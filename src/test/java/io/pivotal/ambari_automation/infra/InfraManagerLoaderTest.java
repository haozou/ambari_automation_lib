package io.pivotal.ambari_automation.infra;

import org.testng.Assert;
import org.testng.annotations.Test;

public class InfraManagerLoaderTest {
	@Test
	public void test1(){
		InfraManager im = InfraManagerLoader.load("io.pivotal.ambari_automation.infra.TestInfraManager");
		Assert.assertTrue(im instanceof TestInfraManager);
	}

}
