package io.pivotal.ambari_automation.ambari.restapi.v1;

import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationHttpGet;
import io.pivotal.ambari_automation.ambari.restapi.AmbariAutomationResponse;
import io.pivotal.ambari_automation.ambari.restapi.BaseApi;

public class RequestApi extends BaseApi {

	public RequestApi(String host, int port, String user, String password) {
		super(host, port, user, password);
	}
	/**
	 * Returns a collection of all requests for the cluster identified by ":clusterName" and the field.
	 * @param clusterName
	 * @param field
	 * filled the field you want to list:
	 *   "*" means all the fields
	 *   below is the qualified field you can provide
	 *   "aborted_task_count" : 0,
     *   "cluster_name" : "test",
     *   "completed_task_count" : 3,
     *   "create_time" : 1413933346118,
     *   "end_time" : -1,
     *   "exclusive" : false,
     *   "failed_task_count" : 0,
     *   "id" : 1,
     *   "inputs" : null,
     *   "operation_level" : null,
     *   "progress_percent" : 21.88235294117647,
     *   "queued_task_count" : 8,
     *   "request_context" : "Install and start all services",
     *   "request_schedule" : null,
     *   "request_status" : "PENDING",
     *   "resource_filters" : [ ],
     *   "start_time" : 1413933346355,
     *   "task_count" : 17,
     *   "timed_out_task_count" : 0,
     *   "type" : "INTERNAL_REQUEST"
	 * @return response
	 */
	public AmbariAutomationResponse listRequests(String clusterName, String field) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/requests?fields=Requests/%s", clusterName, field));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
	/**
	 * Returns a request info for the cluster identified by ":clusterName" , requestId and the field.
	 * @param clusterName
	 * @param requestId
	 * @param field
	 * filled the field you want to list:
	 *   "*" means all the fields
	 *   below is the qualified field you can provide
	 *   "aborted_task_count" : 0,
     *   "cluster_name" : "test",
     *   "completed_task_count" : 3,
     *   "create_time" : 1413933346118,
     *   "end_time" : -1,
     *   "exclusive" : false,
     *   "failed_task_count" : 0,
     *   "id" : 1,
     *   "inputs" : null,
     *   "operation_level" : null,
     *   "progress_percent" : 21.88235294117647,
     *   "queued_task_count" : 8,
     *   "request_context" : "Install and start all services",
     *   "request_schedule" : null,
     *   "request_status" : "PENDING",
     *   "resource_filters" : [ ],
     *   "start_time" : 1413933346355,
     *   "task_count" : 17,
     *   "timed_out_task_count" : 0,
     *   "type" : "INTERNAL_REQUEST"
	 * @return response
	 */
	public AmbariAutomationResponse viewRequestInfo(String clusterName, int requestId, String field) {
		String uri = this.buildUri(String.format("/api/v1/clusters/%s/requests/%d?fields=Requests/%s", clusterName, requestId, field));
		AmbariAutomationHttpGet request = new AmbariAutomationHttpGet(user, password, uri);
		AmbariAutomationResponse response = http.access(request, false);
		return response;
	}
}
