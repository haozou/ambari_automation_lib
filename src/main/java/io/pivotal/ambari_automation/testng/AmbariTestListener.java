package io.pivotal.ambari_automation.testng;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
/**
 * AmbariTestListener - cusomter testng listener.
 * @author Hao Zou
 *
 */
public class AmbariTestListener extends TestListenerAdapter {

	@Override
	public void onTestFailure(ITestResult tr) {
		
		System.out.println(tr.getInstanceName()+"."+tr.getMethod().getMethodName()+"\n"+"fail");
		tr.getThrowable().printStackTrace();
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		System.out.println(tr.getInstanceName()+"."+tr.getMethod().getMethodName()+"\n"+"skip");
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		System.out.println(tr.getInstanceName()+"."+tr.getMethod().getMethodName()+"\n"+"pass");
	}
//	@Override
//	public void onTestStart(ITestResult tr) {
//		try {
//	         BaseTestMethod bm = (BaseTestMethod)tr.getMethod();
//	         Field f = bm.getClass().getSuperclass().getDeclaredField("m_methodName");
//	         f.setAccessible(true);
//	         if (!bm.getMethodName().startsWith(tr.getTestContext().getName()))
//	        	 f.set(bm, tr.getTestContext().getName()+ "_"+ bm.getMethodName());        
//	      }catch (Exception ex) {
//	         System.out.println("ex" + ex.getMessage());
//	      }
//	}

}
