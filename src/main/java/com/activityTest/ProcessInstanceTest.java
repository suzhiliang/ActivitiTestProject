package com.activityTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * 流程实例，任务执行
 * @author 苏志亮
 *
 */
public class ProcessInstanceTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	// 部署流程
	@Test
	public void deploymentProcessDefinition_zip() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deploy = processEngine.getRepositoryService().createDeployment().name("流程实例")
				.addZipInputStream(zipInputStream).deploy();
		System.out.println("   部署流程ID" + deploy.getId() + "  部署流程名:" + deploy.getName() + "  部署流程Category"
				+ deploy.getCategory() + " 流程部署时间" + deploy.getDeploymentTime());
	}

	// 启动流程实例
	@Test
	public void startProcessInstance() {
		String processDefinitionKey = "helloworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);

		System.out.println(" 当前任务节点" + processInstance.getActivityId() + "   " + processInstance.getBusinessKey()
				+ "   " + processInstance.getProcessInstanceId());

	}

	// 任务挂起
	@Test
	public void suspendProcessInstance() {
		String processInstanceId = "701";
		processEngine.getRuntimeService().suspendProcessInstanceById(processInstanceId);

		// System.out.println(" 当前任务节点" + processInstance.getActivityId() + " "
		// + processInstance.getBusinessKey() + " " +
		// processInstance.getProcessInstanceId() );

	}

	// 删除流程实例
	@Test
	public void deleteProcessInstance() {
		String processInstanceId = "701";
		processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, "想删就删");
	}

	// 获取个人任务
	@Test
	public void queryPresonTask() {
		String assignee = "王五";
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee(assignee).list();
		if (tasks != null && !tasks.isEmpty()) {
			for (Task task : tasks) {
				System.out.println("任务Id  " + task.getId());
			}
		}
	}

	// 办理个人任务
	@Test
	public void comletePersonTask() {
		String taskId = "1002";
		processEngine.getTaskService().complete(taskId);

		System.out.println("完成流程节点");
	}

	// 判断整个流程是否走完
	@Test
	public void checkProcessIsComplete() {
		String processIntanceId = "801";
		ProcessInstance pi = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(processIntanceId).singleResult();
		if(pi == null){
			System.out.println("流程实例已经不存在，流程走完了。");
		}else{
			System.out.println("流程实例已经存在，流程没走完。当前流程的任务节点是："+pi.getActivityId());
		}
	}
	
	//查询历史流程实例
	@Test
	public void queryHistoryProcessInstance(){
		String processIntanceId = "701";
		List<HistoricProcessInstance>  list= processEngine.getHistoryService()
		             .createHistoricProcessInstanceQuery()
		             .processInstanceId(processIntanceId)
		             .list();
		for(HistoricProcessInstance hi :list){
			System.out.println("  流程开始时间:"+ hi.getStartTime() + " 流程结束时间:"+hi.getEndTime());
		}
	}
	
	//查询历史任务
	@Test
	public void queryHistoryTasks(){
		String assignee = "张三";
		List<HistoricTaskInstance>  list = processEngine.getHistoryService()
		             .createHistoricTaskInstanceQuery()
		             .taskAssignee(assignee)
		             .list();
		for(HistoricTaskInstance hi:list){
			System.out.println(" 任务节点名称" + hi.getName() + " 任务Id" + hi.getId() + " 流程实例Id:" + hi.getProcessInstanceId());
		}
	}

}
