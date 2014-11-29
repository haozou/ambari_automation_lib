package io.pivotal.ambari_automation.conf;

import io.pivotal.ambari_automation.ambari.AmbariAPIManager;
import io.pivotal.ambari_automation.ambari.AmbariMachine;
import io.pivotal.ambari_automation.infra.InfraManager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.testng.log4testng.Logger;

/**
 * Project wide configuration class.
 * 
 * @author Hao Zou
 */
public final class AmbariConfiguration {
	/**
	 * cluster name keyword
	 */
	public static final String CLUSTER_NAME = "clustername";
	/**
	 * ambari username keyword
	 */
	public static final String AMBARI_USER = "ambari-username";
	/**
	 * ambari port key.
	 */
	public static final String AMBARI_PORT = "ambari-port";
	/**
	 * ambari password keyword
	 */
	public static final String AMBARI_PASSWORD = "ambari-password";
	/**
	 * blueprint name keyword
	 */
	public static final String BLUEPRINT_NAME = "blueprint-name";
	/**
	 * blueprint path keyword
	 */
	public static final String BLUEPRINT = "blueprint-path";
	/**
	 * hostmapping path keyword
	 */
	public static final String HOSTMAPPING = "hostmapping-path";
	/**
	 * stack name keyword
	 */
	public static final String STACK_NAME = "stack-name";
	/**
	 * stack version keyword
	 */
	public static final String STACK_VERSION = "stack-version";
	/**
	 * Attribute prefix.
	 */
	private static final String ATTRIBUTE_PREFIX = "[@";
	/**
	 * Colon String.
	 */
	private static final String COLON = ":";
	/**
	 * Test env attribute
	 */
	private static final String TEST_ENV = "[@testing-env]";
	/**
	 * Test vcloud org attribute
	 */
	private static final String VCLOUD_ORG = "[@vcloud-org]";
	/**
	 * Test vcloud vdc attribute
	 */
	private static final String VCLOUD_VDC = "[@vcloud-vdc]";
	/**
	 * Test vcloud user attribute
	 */
	private static final String VCLOUD_USER = "[@vcloud-user]";
	/**
	 * Test vcloud password attribute
	 */
	private static final String VCLOUD_PASS = "[@vcloud-password]";
	/**
	 * Test vapp template attribute
	 */
	private static final String VAPP_TEMPLATE = "[@vapp-template]";
	/**
	 * Test vapp name attribute
	 */
	private static final String VAPP_NAME = "[@vapp-name]";
	/**
	 * Test vapp action attribute
	 */
	private static final String VAPP_ACTION = "[@vapp-action]";
	/**
	 * Test vapp post action attribute
	 */
	private static final String VAPP_POST_ACTION = "[@vapp-post-action]";
	/**
	 * SSH TimeOut key on adminNode.
	 */
	public static final String SSH_TIMEOUT = "ssh-timeout";
	/**
	 * SSH username key on adminNode.
	 */
	public static final String SSH_USER = "ssh-user";
	/**
	 * SSH port for adminNode.
	 */
	public static final String SSH_PORT = "ssh-port";
	/**
	 * Root password attribute.
	 */
	private static final String SSH_PASSWORD = "ssh-password";
	/**
	 * SSH mode: silent or not
	 */
	public static final String SILENT = "ssh-silent";
	/**
	 * XPath for Machines under configuration.
	 */
	private static final String CONFIGURATION_MACHINES_PATH = "machines.";
	/**
	 * XPath for properties under configuration.
	 */
	private static final String CONFIGURATION_PROPERTIES_PATH = "properties.";
	/**
	 * server key.
	 */
	public static final String SERVER = "ambari-server";
	/**
	 * agents key
	 */
	public static final String AGENTS = "ambari-agents";
	/**
	 * Configuration holder.
	 */
	private Configuration config;
	/**
	 * InfraManager
	 */
	private InfraManager infraManager;
	/**
	 * api manager maintains all the rest api
	 */
	private AmbariAPIManager apiManager;
	/**
	 * stack component topology
	 */
	private Topology topology;
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger
			.getLogger(AmbariConfiguration.class);

	/**
	 * Initializes properties only once.
	 * 
	 */
	public void initialize() {
		log.info("Provision machines...");
		if ("vcloud".equals(config.getString(CONFIGURATION_MACHINES_PATH + TEST_ENV))) {
//			this.infraManager = new GPCloudInfraManager(
//					config.getString(CONFIGURATION_MACHINES_PATH + VCLOUD_ORG),
//					config.getString(CONFIGURATION_MACHINES_PATH + VCLOUD_VDC),
//					config.getString(CONFIGURATION_MACHINES_PATH + VCLOUD_USER),
//					config.getString(CONFIGURATION_MACHINES_PATH + VCLOUD_PASS),
//					config.getString(CONFIGURATION_MACHINES_PATH + VAPP_NAME),
//					config.getString(CONFIGURATION_MACHINES_PATH + VAPP_TEMPLATE),
//					config.getString(CONFIGURATION_MACHINES_PATH + VAPP_ACTION),
//					config.getString(CONFIGURATION_MACHINES_PATH + VAPP_POST_ACTION));

		} else if("dcloud".equals(config.getString(CONFIGURATION_MACHINES_PATH + TEST_ENV))) {
			//this.infraManager = new DCloudInfraManager("src/test/resources/dcloud.json", true);
		} else {
			log.warn("no infrastructure available for " + 
					config.getString(CONFIGURATION_MACHINES_PATH + TEST_ENV) + " using the machines in configuration file");
		}
		Map<String, AmbariMachine> machines = this.provisionVMs();
		AmbariMachine server = machines.get(config.getString(CONFIGURATION_MACHINES_PATH + SERVER));
		this.apiManager = new AmbariAPIManager(server.getExternalAddress(),
				this.getPropertyInt(AmbariConfiguration.AMBARI_PORT),
				this.getProperty(AmbariConfiguration.AMBARI_USER),
				this.getProperty(AmbariConfiguration.AMBARI_PASSWORD)
				);
		Set<AmbariMachine> machineSet = new HashSet<AmbariMachine>();
		machineSet.addAll(machines.values());
		this.topology = new Topology(server, machineSet, 
				this.getProperty(BLUEPRINT), this.getProperty(HOSTMAPPING));

		// print out the config property
		Iterator<String> it = config.getKeys();
		while (it.hasNext()) {
			String key = it.next();
			List<Object> value = config.getList(key);

			log.info(key + COLON + value);
		}
		log.info("Machines " + machines.keySet());
		log.info("Server" + server);
		log.info("RolesMachines " + this.topology.getRolesMachines());
		log.info("MachineRoles " + this.topology.getMachineRoles());
	}

	/**
	 * 
	 * using vcloudcli to provision vms
	 */
	private Map<String, AmbariMachine> provisionVMs() {
		Map<String, AmbariMachine> machines = new TreeMap<String, AmbariMachine>();
		Integer port = this.getPropertyInt(SSH_PORT);
		String login = this.getProperty(SSH_USER);
		String password = this.getProperty(SSH_PASSWORD);
		if (this.infraManager == null) {
			String server = config.getString(CONFIGURATION_MACHINES_PATH + SERVER);
			machines.put(server, new AmbariMachine(server, port, login, password));
			for (Object host : config.getList(CONFIGURATION_MACHINES_PATH + AGENTS)) {
				if (!machines.containsKey(host)) {
					machines.put((String) host, new AmbariMachine((String) host, port, login,
							password));
				}
			}
		} else {
			try {
				int count = 1;
				this.infraManager.beforeExecution(this.getProperty(SSH_USER), this.getProperty(SSH_PASSWORD));
				for (String host : infraManager.getExternalIPsOfProvisionedVMs()) {
					String m = String.format("machine%d", count++);
					config.setProperty(m, host);
					if (!machines.containsKey(host)) {
						machines.put(host, new AmbariMachine(host, port, login,
								password));
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return machines;
	}

	/**
	 * Default constructor.
	 */
	public AmbariConfiguration(String fileName) {
		super();
		try {
			config = new XMLConfiguration(fileName);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns given property.
	 * 
	 * @param key
	 *            property key
	 * @return value of the property. NULL if not present.
	 */
	public String getProperty(final String key) {
		return config.getString(CONFIGURATION_PROPERTIES_PATH + key);
	}
	/**
	 * Returns given property.
	 * 
	 * @param key
	 *            property key
	 * @param defaut_value
	 *            property default value
	 * @return value of the property. default if not present.
	 */
	public String getProperty(final String key, final String default_value) {
		return config.getString(CONFIGURATION_PROPERTIES_PATH + key, default_value);
	}
	/**
	 * set property.
	 * 
	 * @param key
	 *            property key
	 */
	public void setProperty(final String key, final String value) {
		this.config.setProperty(CONFIGURATION_PROPERTIES_PATH + key, value);
	}
	/**
	 * Returns given property as Boolean.
	 * 
	 * @param key
	 *            property key
	 * @return Boolean value of the property. False if not present.
	 */
	public boolean getPropertyBoolean(final String key) {
		return config.getBoolean(CONFIGURATION_PROPERTIES_PATH + key, false);
	}

	/**
	 * Returns given property as Int.
	 * 
	 * @param key
	 *            property key
	 * @return Integer value of the property. NULL if not present.
	 */
	public Integer getPropertyInt(final String key) {
		return config.getInt(CONFIGURATION_PROPERTIES_PATH + key);
	}
	/**
	 * Returns given property as Int.
	 * 
	 * @param key
	 *            property key
	 * @return Integer value of the property. default value if not present.
	 */
	public Integer getPropertyInt(final String key, final int default_value) {
		return config.getInt(CONFIGURATION_PROPERTIES_PATH + key, default_value );
	}

	/**
	 * Returns given property as List.
	 * 
	 * @param key
	 *            property key
	 * @return List of the properties. NULL if not present.
	 */
	public List<Object> getPropertyList(final String key) {
		return config.getList(CONFIGURATION_PROPERTIES_PATH + key);
	}

	/**
	 * Returns given property as StringArray.
	 * 
	 * @param key
	 *            property key
	 * @return StringArray of the properties. NULL if not present.
	 */
	public String[] getPropertyStringArray(final String key) {
		return config.getStringArray(CONFIGURATION_PROPERTIES_PATH + key);
	}
	public AmbariAPIManager getAPIManager() {
		return this.apiManager;
	}
	public InfraManager getInfraManager() {
		return this.infraManager;
	}
	public Topology getTopology() {
		return this.topology;
	}
}
