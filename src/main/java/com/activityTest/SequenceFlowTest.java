package com.activityTest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class SequenceFlowTest {
	
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); 
	//流程部署
	@Test
	public void deploymentProcessDefinition(){
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("diagrams/SequenceFlow.bpmn");
		InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream("diagrams/SequenceFlow.png");
		Deployment deployment = processEngine.getRepositoryService()
		             .createDeployment()
		             .name("连线")
		             .addInputStream("SequenceFlow.bpmn", inputStream)
		             .addInputStream("SequenceFlow.png", inputStream1)
		             .deploy();
		System.out.println("流程部署id " + deployment.getId() );
		
	}
	
	//启动流程
    @Test
    public void startProcessInstance(){
	    String processKey = "sequenceFlow";
	    processEngine.getRuntimeService()
	                 .startProcessInstanceByKey(processKey);
	    System.out.println("流程启动成功----------------》");
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
	
	//完成任务
	@Test
	public void completeTask(){
		String taskId = "2203";
		//设置信息:重要/不重要 来指引流程方向
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("msessage", "重要");
		processEngine.getTaskService()
		             .complete(taskId, variables);
		System.out.println("任务完成-----------》");
	}
    
   
}
