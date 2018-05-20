package com.helloworld;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class HelloworldProcess {
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程（部署方式一）
	 */
	@Test
	public void deploy() {
		Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署相关的Service
				.createDeployment()// 创建部署对象
				.name("helloworld:activiti")// 设置部署名称
				.addClasspathResource("diagrams/helloworld.bpmn").addClasspathResource("diagrams/helloworld.png")
				.deploy();// 完成部署
		System.out.println("流程定义ID" + deployment.getId() + "  流程定义名" + deployment.getName() + "  流程定义时间"
				+ deployment.getDeploymentTime());

	}
	
	/**
	 * 启动流程实例
	 */
	@Test
	public void startProcessInstance() {
		// 流程定义的KEY
		String processDefinitionKey = "helloworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()// 与执行的流程实例和执行对象相关的Service
				.startProcessInstanceByKey(processDefinitionKey);// 根据流程定义的key启动流程按流程的最新版本启动
		System.out.println(" 流程实例ID" + processInstance.getId() + " " + processInstance.getBusinessKey() + "  流程定义ID"
				+ processInstance.getProcessDefinitionId());
	}

	/**
	 * 查询某个人的任务实例
	 */
	@Test
	public void listMyTask() {
		String assignee = "李四";
		List<Task> tasks = processEngine.getTaskService()// 与任务管理相关的Service
				.createTaskQuery()// 创建任务查询对象
				.taskAssignee(assignee)// 指定要查询的办理人
				.orderByTaskCreateTime().asc().list();
		if (tasks != null && !tasks.isEmpty()) {
			for (Task task : tasks) {
				System.out.println("任务ID" + task.getId() + "  任务办理人" + task.getAssignee() + "  任务名称" + task.getName() + " 流程实例ID" + task.getProcessInstanceId()+" 流程执行ID" + task.getExecutionId());
			}
		}
	}
	
	/*
	 * 完成任务
	 */
	@Test
	public void completeTask(){
		String taskId = "302";//任务Id
		processEngine.getTaskService().complete(taskId);//完成任务
		System.out.println("完成任务");
	}
}
