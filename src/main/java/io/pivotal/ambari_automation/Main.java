package io.pivotal.ambari_automation;

import io.pivotal.ambari_automation.conf.AmbariConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.TestNG;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlMethodSelector;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlScript;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

//TODO: 1. set threadcount to maximum
// 		2. The exception in beforeclass doen't show up in the terminal, need to revisit and fix
/**
 * The Main class for test
 * @author hzou
 *
 */
public class Main {
	private static final String GROUPS = "testng-groups";
	private static final String EXGROUPS = "excludegroups";
	private static final String METHODS = "testng-methods";
	private static final Pattern pattern = Pattern.compile("(.*xml)(\\(?)(.*\\))");
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger.getLogger(Main.class);
	public static final String SUITE_NAME = "suiteName";
	 
	public static HashMap<String, AmbariConfiguration> cache = new HashMap<String, AmbariConfiguration>();
	
	public static void main(String [ ] args) throws Exception {
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		Matcher matcher;
		int count = 0;
		for (String value : System.getProperty("testng.configure", "machine.xml").split(",")) {
			count++;
			value = value.trim();
			String fileName;
			String suiteName;
			String properties = null;
			matcher = pattern.matcher(value);
			if (value.contains("(")) {
				if (matcher.find()) {
					fileName = matcher.group(1);
					properties = matcher.group(3).substring(0, matcher.group(3).length()-1);
				} else {
					throw new RuntimeException("not correct input");
				}
			} else {
				fileName = value;
			}
			AmbariConfiguration configure = new AmbariConfiguration(fileName);
			
			if (properties != null) {
				for (String property : properties.split(";")) {
					configure.setProperty(property.substring(0, property.indexOf("=")),
							property.substring(property.indexOf("=")+2, property.length()-1));
				}
			}
			
			log.info("using configure file " + fileName);
			
			List<String> groups = new ArrayList<String>();
			for (String group : configure.getProperty(GROUPS, "smoke").split(" "))
				groups.add(group.trim());
			List<String> exgroups = null; 
			if (configure.getProperty(EXGROUPS, null) != null) {
				exgroups = new ArrayList<String>();
				for (String group : configure.getProperty(GROUPS).split(" "))
					exgroups.add(group.trim());
			}
	
			suiteName = fileName.split("\\.")[0] + count;
			cache.put(suiteName, configure);
			XmlSuite suite = new XmlSuite();
			suite.setName(suiteName);
			suite.setDataProviderThreadCount(configure.getPropertyInt("dataProviderThreadCount", 20));
			//TODO: add listener
			//suite.addListener("io.pivotal.ambari_automation.testng.AmbariTestListener");
	        Map<String, String> parameters = new HashMap<String, String>();
	        parameters.put(SUITE_NAME, suiteName);
	        suite.setParameters(parameters);

			XmlTest test = new XmlTest(suite);
	        test.setName(suiteName);
	        test.setPreserveOrder("true");
	        test.setParallel("methods");
	        test.setThreadCount(configure.getPropertyInt("threadCount", 20));

	        ArrayList<XmlPackage> packages = new ArrayList<XmlPackage>();
	        packages.add(new XmlPackage("io.pivotal.ambari_automation.testcases.*"));
	        test.setPackages(packages);
	        
	        // support running only the specified test method, will ignore the included or excluded groups
			if (configure.getProperty(METHODS) != null) {
				String beanShellScript = "false";
				String methods = "";
				for (String method : configure.getProperty(METHODS).split(" ")) {
					method = String.format("(testngMethod.getTestClass().getName().contains(\"%s\") && testngMethod.getMethodName().matches(\"%s\"))", 
							method.contains("#") ? method.split("#")[0] : "", method.contains("#") ? method.split("#")[1] : method.split("#")[0]);
					methods += " || " + method;
				}
				beanShellScript += methods;
				ArrayList<XmlMethodSelector> selectors = new ArrayList<>();
		        XmlMethodSelector methodSelector = new XmlMethodSelector();
		        XmlScript script = new XmlScript();
		        script.setLanguage("beanshell");
		        script.setScript(beanShellScript);
		        methodSelector.setScript(script);
		        selectors.add(methodSelector);
		        test.setMethodSelectors(selectors);
			}
	        
	        test.setIncludedGroups(groups);
	        if (exgroups != null)
	        	test.setExcludedGroups(exgroups);

	        suites.add(suite); 
	        System.out.println(suite.toXml());
	        
		}
		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.setSuiteThreadPoolSize(Integer.parseInt(System.getProperty("suiteThreadSize", String.valueOf(suites.size()))));
		tng.run();
	}
}
