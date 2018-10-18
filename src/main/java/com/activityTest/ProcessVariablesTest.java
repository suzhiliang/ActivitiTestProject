package com.activityTest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;


/**
 * 流程变量查询
 * @author 苏志亮
 *
 */
public class ProcessVariablesTest {
   
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine(); 
	
	//流程部署
	@Test
	public void deploymentProcessDefinition(){
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("diagrams/processVariables.bpmn");
		InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream("diagrams/processVariables.png");
		Deployment deployment = processEngine.getRepositoryService()
		             .createDeployment()
		             .name("流程变量")
		             .addInputStream("processVariables.bpmn", inputStream)
		             .addInputStream("processVariables.png", inputStream1)
		             .deploy();
		System.out.println("流程部署id " + deployment.getId() );
		
	}
	
	//启动流程
	@Test
	public void startProcessInstance(){
		String processKey = "processVariables";
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processKey);
		//启动流程时设置流程变量
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("bussinessId", "10002");
//		map.put("bussinessType", "02");
//		processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(processKey,map);
		System.out.println("  流程实例ID ------》"+processInstance.getProcessInstanceId());
	}
	
	
	//设置流程实例的变量值
	@Test
	public	void setProcessInstanceVariables(){
		String processInstanceId = "1301";
		processEngine.getRuntimeService().setVariable(processInstanceId, "bussinessId", "10001");
		processEngine.getRuntimeService().setVariable(processInstanceId, "bussinessType", "02");
		System.out.println("------------------->设置流程定义变量完成。");
	}
	
	@Test
	public void getProcessObjectByVariables(){
		//根据流程变量获取流程
		ProcessInstance processInstance = processEngine.getRuntimeService()
		              .createProcessInstanceQuery()
		             .variableValueEquals("bussinessId", "10003")
		             .variableValueEquals("bussinessType", "03")
		             .singleResult();
		System.out.println("流程实例ID --------------->" + processInstance.getId() );
		System.out.println( " 流程实例变量值 ---------->"+ processEngine.getRuntimeService().getVariables(processInstance.getId()));
		//根据流程变量获取执行对象
		List<Execution> executions = processEngine.getRuntimeService()
		             .createExecutionQuery()
		             .variableValueEquals("bussinessId", "10003")
		             .variableValueEquals("bussinessType", "03")
		             .list();
	    if(executions!=null && !executions.isEmpty()){
	    	for(Execution execution:executions){
	    		System.out.println("执行对象id："+execution.getId()+"  当前执行节点" + execution.getActivityId() + processInstance.getProcessVariables());
	    		System.out.println(" 通过执行对象id获取流程变量值---------->"+processEngine.getRuntimeService().getVariables(execution.getId()));
	    	}
	    }
		//根据流程变量获取任务对象
	    List<Task> tasks = processEngine.getTaskService()
	    		                        .createTaskQuery()
	    		                        .processInstanceId(processInstance.getId())
	    		                         .list();
	    if(tasks!=null && !tasks.isEmpty()){
	    	for(Task task:tasks){
	    		System.out.println("任务id："+task.getId()+"  任务执行人:" + task.getAssignee() + "  任务节点:"+task.getName());
	    	}
	    }
	}
	
	//设置流程任务变量值
	@Test
	public void setProcessTaskVariables(){
		String taskId = "1304";
		processEngine.getTaskService().setVariable(taskId,"bussinessId", "10003");
		processEngine.getTaskService().setVariable(taskId,"bussinessType", "03");
		System.out.println("流程变量设置完成------------>");
	}
	
	//获取任务变量值
	@Test
	public void getTaskVariables(){
		String processInstanceId = "1301";
		//根据流程变量获取任务对象
		 List<Task> tasks = processEngine.getTaskService()
                 .createTaskQuery()
                 .processInstanceId(processInstanceId)
                  .list();
	    if(tasks!=null && !tasks.isEmpty()){
	    	for(Task task:tasks){
	    		System.out.println("任务id："+task.getId()+"  任务执行人:" + task.getAssignee() + "  任务节点:"+task.getName() );
	    		System.out.println("任务变量值---------->"+processEngine.getTaskService().getVariables(task.getId()));
	    	}
	    }
	}
	
	///完成任务同时设置变量
	@Test
	public void completeTask(){
		String taskId = "1502";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bussinessId", "10002");
		map.put("bussinessType", "02");
		processEngine.getTaskService().complete(taskId, map);
	}
	
	//获取流程历史变量
	@Test
	public void getHisVariables(){
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
		             .createHistoricVariableInstanceQuery()
		             .variableName("bussinessId")//查询指定的变量名
		            .list();
		if(list!=null && !list.isEmpty()){
			for(HistoricVariableInstance recode:list){
				System.out.println("历史流程变量主键ID："+recode.getId() +"   任务ID："+ recode.getTaskId()  
				+ "  流程实例ID:"+ recode.getProcessInstanceId()
				+ "     变量名:" + recode.getVariableName() + "   变量值:"+ recode.getValue());
			}
		}
		
		             
	}
   
	
	 
}
