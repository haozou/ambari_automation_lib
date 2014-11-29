package io.pivotal.ambari_automation.conf;

import io.pivotal.ambari_automation.ambari.AmbariMachine;
import io.pivotal.ambari_automation.ambari.AmbariMachineGroup;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.jayway.restassured.path.json.JsonPath;

/**
 * Cluster topology
 * @author hzou
 *
 */
public class Topology {
	/**
	 * Map holding roles to machines mapping.
	 */
	private Map<String, Set<AmbariMachine>> rolesMachines = new HashMap<>();
	private Map<AmbariMachine, Set<String>> machineRoles = new HashMap<>();
	private Map<String, AmbariMachine> machineMap = new HashMap<>();
	private AmbariMachine server;
	
	private JsonPath blueprintJson;
	private JsonPath hostmappingJson;

	/**
	 * The constructor that generate he topology of the cluster using blueprintfile and 
	 * hostmapping file
	 * @param server 
	 * 			server machine
	 * @param machineSet 
	 * 			all the machines
	 * @param blueprintFilePath
	 * 			blueprint file path
	 * @param hostmappingFilePath
	 * 			hostmapping file path
	 */
	public Topology(AmbariMachine server, Set<AmbariMachine> machineSet, String blueprintFilePath,
			String hostmappingFilePath) {
		
		this.server = server;
		blueprintJson = new JsonPath(new File(blueprintFilePath));
		hostmappingJson = new JsonPath(new File(hostmappingFilePath));

		
		for (AmbariMachine machine : machineSet) {
			machineMap.put(machine.getInternalAddress(), machine);
		}
		Map<String, Set<AmbariMachine>> hostMapping = new HashMap<>();

		for (int i = 0; i < hostmappingJson.getList("host_groups").size(); i++) {
			List<String> fqdns = (List<String>) hostmappingJson.getList(
					"host_groups.hosts.fqdn").get(i); 
			String name = (String) hostmappingJson.getList("host_groups.name")
					.get(i);
			for (int j = 0; j < fqdns.size(); j++) {
				if (hostMapping.containsKey(name)) {
					hostMapping.get(name).add(machineMap.get(fqdns.get(j)));
				} else {
					Set<AmbariMachine> machines = new HashSet<>();
					machines.add(machineMap.get((String) fqdns.get(j)));
					hostMapping.put(name, machines);
				}
			}
		}
		for (int i = 0; i < blueprintJson.getList("host_groups").size(); i++) {
			List<HashMap<String, String>> components = (List<HashMap<String, String>>) blueprintJson
					.getList("host_groups.components").get(i);
			String host = (String) blueprintJson.getList("host_groups.name")
					.get(i);
			for (int j = 0; j < components.size(); j++) {
				for (AmbariMachine machine : hostMapping.get(host)) {
					if (machineRoles.containsKey(machine))
						this.machineRoles.get(machine).add(components.get(j).get("name"));
					else {
						Set<String> roles = new HashSet<>();
						roles.add(components.get(j).get("name"));
						this.machineRoles.put(machine, roles);
					}
				}
				if (this.rolesMachines.containsKey(components.get(j)
						.get("name"))) {
					this.rolesMachines.get(components.get(j).get("name"))
							.addAll(hostMapping.get(host));
				} else {
					Set<AmbariMachine> machines = new HashSet<>();
					machines.addAll(hostMapping.get(host));
					this.rolesMachines.put(components.get(j).get("name"),
							machines);
				}
			}
		}
	}
	/**
	 * Returns set of machines by role.
	 * 
	 * @param role
	 *            String role
	 * @return HashSet of machines.
	 */
	public Set<AmbariMachine> getMachinesByRole(final String role) {
		if (rolesMachines == null || role == null) {
			return null;
		} else {
			return rolesMachines.get(role);
		}
	}
	/**
	 * Returns set of machines by role.
	 * 
	 * @param role
	 *            String role
	 * @return HashSet of machines.
	 */
	public AmbariMachineGroup getMachineGroupByRole(final String role) {
		if (rolesMachines == null || role == null) {
			return null;
		} else {
			return new AmbariMachineGroup(this.getMachinesByRole(role));
		}
	}
	/**
	 * whether the machine has the service role
	 * @param role String role
	 * @return
	 */
	public boolean doesMachineExistByRole(String role) {
		return this.getMachinesByRole(role) == null ? false: true;
	}
	/**
	 * Returns ambari server machine
	 * @return ambari server machine
	 */
	public AmbariMachine getServer() {
		return this.server;
	}
	/**
	 * Returns set of roles by Machine.
	 * 
	 * @param role
	 *            String role
	 * @return HashSet of roles.
	 */
	public Set<String> getRolesByMachine(final AmbariMachine machine) {
		if (machineRoles == null || machine == null) {
			return null;
		} else {
			return this.machineRoles.get(machine);
		}
	}
	/**
	 * Returns agents.
	 * 
	 * @return agents Machine
	 */
	public Set<AmbariMachine> getAgents() {
		return this.machineRoles.keySet();
	}
	/**
	 * Returns agents Group.
	 * 
	 * @return agents Group
	 */
	public AmbariMachineGroup getAgentsGroup() {
		return new AmbariMachineGroup(this.getAgents());
	}
	/**
	 * Return blueprint name
	 * @return
	 */
	public String getBlueprintName() {
		return this.blueprintJson.getString("Blueprints.blueprint_name");
	}
	/**
	 * Return blueprint format string
	 * @return
	 */
	public String getBlueprint() {
		return this.blueprintJson.prettify();
	}
	/**
	 * Return hostmapping format string
	 * @return
	 */
	public String getHostmapping() {
		return this.hostmappingJson.prettify();
	}
	/**
	 * Returns stack name
	 * @return
	 */
	public String getStackName() {
		return this.blueprintJson.getString("Blueprints.stack_name");
	}
	/**
	 * Returns stack version
	 * @return
	 */
	public String getStackVersion() {
		return this.blueprintJson.getString("Blueprints.stack_version");
	}
	/**
	 * Return the rolesMachine TreeSet
	 * @return
	 */
	public Map<String, Set<AmbariMachine>> getRolesMachines() {
		return rolesMachines;
	}
	public void setRolesMachines(Map<String, Set<AmbariMachine>> rolesMachines) {
		this.rolesMachines = rolesMachines;
	}
	/**
	 * Return the machinesRole TreeSet
	 * @return
	 */
	public Map<AmbariMachine, Set<String>> getMachineRoles() {
		return machineRoles;
	}
	public void setMachineRoles(Map<AmbariMachine, Set<String>> machineRoles) {
		this.machineRoles = machineRoles;
	}
	/**
	 * Return all the machine in the infrastructure using the 
	 * internal address as the key
	 * @return
	 */
	public Map<String, AmbariMachine> getMachineMap() {
		return this.machineMap;
	}
	/**
	 * add new agent from the existing machine in 
	 * the infrastructure
	 * @param hostName
	 */
	public void addAgent(String hostName) {
		if (!this.machineMap.containsKey(hostName)) {
			throw new RuntimeException(hostName + " doesn't exist in the current infrastructure");
		}
		if (this.machineRoles.containsKey(this.machineMap.get(hostName))) {
			throw new RuntimeException(hostName + " is already an agent");
		}
		this.machineRoles.put(this.machineMap.get(hostName), new HashSet<String>());
	}
	/**
	 * remove the existing agent
	 * @param hostName
	 */
	public void removeAgent(String hostName) {
		if (!this.machineMap.containsKey(hostName)) {
			throw new RuntimeException(hostName + " doesn't exist in the current infrastructure");
		}
		if (!this.machineRoles.containsKey(this.machineMap.get(hostName))) {
			throw new RuntimeException(hostName + " is not exist in current agents");
		}
		this.machineRoles.remove(this.machineMap.get(hostName));
	}
	/**
	 * get the machine by hostName(internal address)
	 * @param hostName
	 * @return
	 */
	public AmbariMachine getMachineByHostName(String hostName) {
		if (!this.machineMap.containsKey(hostName)) {
			return null;
		}
		return this.machineMap.get(hostName);
	}
	/**
	 * add new role
	 * @param hostName
	 * @param role
	 */
	public void addRole(String hostName, String role) {
		if (!this.machineMap.containsKey(hostName)) {
			throw new RuntimeException(hostName + " doesn't exist in the current infrastructure");
		}
		if (!this.machineRoles.containsKey(this.machineMap.get(hostName))) {
			throw new RuntimeException(hostName + " is not exist in current agents");
		}
		if (this.rolesMachines.containsKey(role)) {
			this.rolesMachines.get(role).add(this.machineMap.get(hostName));
		} else {
			Set<AmbariMachine> machines = new HashSet<>();
			machines.add(this.machineMap.get(hostName));
			this.rolesMachines.put(role, machines);
		}
	}
}
