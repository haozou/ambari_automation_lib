package io.pivotal.ambari_automation.ambari.restapi;

import io.pivotal.ambari_automation.ambari.restapi.v1.TasksApi;

import java.util.List;
import java.util.Map;

import org.testng.log4testng.Logger;

public abstract class BaseApi {
	/**
	 * Logger instance for this class.
	 */
	private static final Logger log = Logger.getLogger(BaseApi.class);
	public final static String HTTP = "http://";
	protected String host;
	protected int port;
	protected String user;
	protected String password;
	protected AmbariAutomationHttpClient http;
	
	public BaseApi(String host, int port, String user, String password){
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		http = new AmbariAutomationHttpClient();
	}
	public String buildUri(String path) {
		String uri = HTTP + host + (port==0?"":":"+port) + path;
		return uri;
	}
	/**
	 * get the finished tasks count
	 * @param taskStatuses list of task statuses
	 * @return the number of finished tasks, -1 if some taskes aborted
	 */
	private int finishedTasks(List<String> taskStatuses) {
		int count = 0;
		for (String status : taskStatuses) {
			if (status.equals("ABORTED") || status.equals("FAILED"))
				return -1;
			if (status.equals("COMPLETED"))
				count++;
		}
		return count;
	}
	/**
	 * print out the progress bar
	 * @param percent percentage 
	 * @param machine machine that running the deployment
	 */
	private void printProgBar(int percent) {
		StringBuilder bar = new StringBuilder("[");

		for (int i = 0; i < 50; i++) {
			if (i < (percent / 2)) {
				bar.append("=");
			} else if (i == (percent / 2)) {
				bar.append(">");
			} else {
				bar.append(" ");
			}
		}

		bar.append("]   " + percent + "%     ");
		System.out.print("\r" + this.host + bar.toString());
	}
	/**
	 * function to track the status of tasks
	 * @param clusterName
	 * @param requestId
	 * @throws Exception
	 */
	public void trackStatus(String clusterName, int requestId) throws Exception{
		TasksApi tasksApi = new TasksApi(host, port, user, password);
		AmbariAutomationResponse resp;
		while (true) {

			resp = tasksApi.listTasks(clusterName, requestId, "status");
			List<String> taskStatuses = resp.getBodyListByJsonPath("items.Tasks.status");
			int finished = finishedTasks(taskStatuses);
			if (finished == -1) {
				resp = tasksApi.listTasks(clusterName, requestId, "*");
				List<Map<String, String>> tasks = resp.getBodyListByJsonPath("items.Tasks");
				for (Map<String, String> task: tasks) {
					if (task.get("status").equals("FAILED")) {
						log.error(task.get("role") + " FAILED");
						throw new Exception(task.get("role") + " FAILED:\n" + task.get("stderr"));
					}
				}
				throw new Exception("tasks aborted");
			}
			int progress = finished * 100 / taskStatuses.size();

			printProgBar(progress);
			try {
				Thread.sleep(5000); // 1000 milliseconds is one second.
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			if (finished == taskStatuses.size()) {
				System.out.println();
				break;
			}
		}
	}
}
