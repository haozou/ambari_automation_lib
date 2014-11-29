package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class TasksApi extends BaseApi {

	public TasksApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	
	/**
	 * Returns a collection of all tasks' field for the request identified by ":requestId" and the cluster identified by ":clusterName".
	 * @param clusterName
	 * @param requestId
	 * @param filed the property of the task (cluster_name for example), all properties showing below: 
	 * "*" means all properties
	 * Properties
	 * Property	Description	
	 * Tasks/id	The task id
	 * Tasks/request_id	The parent request id
	 * Tasks/cluster_name	The name of the parent cluster
	 * Tasks/attempt_cnt	The number of attempts at completing this task
	 * Tasks/command	The task command
	 * Tasks/exit_code	The exit code
	 * Tasks/host_name	The name of the host
	 * Tasks/role	The role
	 * Tasks/stage_id	The stage id
	 * Tasks/start_time	The task start time
	 * Tasks/status	The task status
	 * Tasks/stderr	The stderr from running the taks
	 * Tasks/stdout	The stdout from running the task
	 * @return response
	 */
	public AmbariAutomationResponse listTasks(String clusterName, int requestId, String field) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/requests/%d/tasks?fields=Tasks/%s", 
				clusterName, requestId, field));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	
	/**
	 * Returns task's field for the task identified by ":taskId" and 
	 * request identified by":requestId" and the cluster identified by ":clusterName".
	 * @param clusterName
	 * @param requestId
	 * @param taskId
	 * @param filed the property of the task (cluster_name for example), all properties showing below: 
	 * "*" means all properties
	 * Properties
	 * Property	Description	
	 * Tasks/id	The task id
	 * Tasks/request_id	The parent request id
	 * Tasks/cluster_name	The name of the parent cluster
	 * Tasks/attempt_cnt	The number of attempts at completing this task
	 * Tasks/command	The task command
	 * Tasks/exit_code	The exit code
	 * Tasks/host_name	The name of the host
	 * Tasks/role	The role
	 * Tasks/stage_id	The stage id
	 * Tasks/start_time	The task start time
	 * Tasks/status	The task status
	 * Tasks/stderr	The stderr from running the taks
	 * Tasks/stdout	The stdout from running the task
	 * @return response
	 */
	public AmbariAutomationResponse viewTaskInfo(String clusterName, int requestId, int taskId, String field) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/requests/%d/tasks/%d?fields=Tasks/%s", 
				clusterName, requestId, taskId, field));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}

}
