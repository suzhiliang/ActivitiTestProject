package com.activityTest;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

/**
 * 流程历史查询
 * @author 苏志亮
 *
 */
public class ProcessHistoryQueryTest {
	
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
   //历史流程实例查询（历史流程可以查询到还没走完流程实例的记录，但是记录的结束时间是null）
	@Test
   public void queryHistoryProcessInstances(){
	   String processKey = "helloworld";
	   //根据流程定义的KEY获取历史流程实例
	   List<HistoricProcessInstance> list = processEngine.getHistoryService()
	                .createHistoricProcessInstanceQuery()
	                .processDefinitionKey(processKey)
	                .list();
	   if(list!=null&&!list.isEmpty()){
		   for(HistoricProcessInstance recode:list){
			   System.out.println("主键ID："+recode.getId()+"  流程定义ID："+ recode.getProcessDefinitionId()
			   + " 流程开始时间:" + recode.getStartTime() + " 流程结束时间:" + recode.getEndTime() + " 流程历时"+ recode.getDurationInMillis());
		   }
	   }
   }
  
  //启动一个新的流程，判断未完成的流程，是否在历史流程中能被查询
	//验证结果------------>历史流程可以查询到还没走完流程实例的记录，但是记录的结束时间是null
   @Test
   public void startProcessInstance(){
	   String processKey = "helloworld";
	   processEngine.getRuntimeService()
	                .startProcessInstanceByKey(processKey);
	   System.out.println("流程启动成功----------------》");
	                
   }
   
   //查询历史活动（某个流程的执行一共经历多少个任务活动）
   @Test
   public void getHsitoricActivityInstance(){
	   String processInstanceId = "1301";
	   List<HistoricActivityInstance> list= processEngine.getHistoryService()
	                 .createHistoricActivityInstanceQuery()
	                  .processInstanceId(processInstanceId)
	                 .list();
	   if(list!=null&&!list.isEmpty()){
		   for(HistoricActivityInstance recode:list){
			   System.out.println("活动节点ID："+recode.getId()+"  activitiId："+ recode.getActivityId() 
			   + "   activitiName:"+recode.getActivityName() + "  activitiType：" + recode.getActivityType() + " assignee:"+ recode.getAssignee()
			   + " 活动节点开始时间:" + recode.getStartTime() + " 活动节点结束时间:" + recode.getEndTime() + " 活动节点历时"+ recode.getDurationInMillis());
		   }
	   }
   }
   
   //查询历史任务
   @Test
   public void getHsitoricTasksInstance(){
	   String processInstanceId = "1701";
	   List<HistoricTaskInstance> list= processEngine.getHistoryService()
	                 .createHistoricTaskInstanceQuery()
	                  .processInstanceId(processInstanceId)
	                 .list();
	   if(list!=null&&!list.isEmpty()){
		   for(HistoricTaskInstance recode:list){
			   System.out.println("taskId："+recode.getId()+"  name："+ recode.getName() 
			   +  " assignee:"+ recode.getAssignee()
			   + " 任务开始时间:" + recode.getStartTime() + " 任务结束时间:" + recode.getEndTime() + " 任务历时"+ recode.getDurationInMillis());
		   }
	   }
   }
   
   
   //查询历史流程变量
   @Test
   public void getHsitoricVariables(){
	   String processInstanceId = "1301";
	   List<HistoricVariableInstance> list= processEngine.getHistoryService()
	                 .createHistoricVariableInstanceQuery()
	                  .processInstanceId(processInstanceId)
	                 .list();
	   if(list!=null&&!list.isEmpty()){
		   for(HistoricVariableInstance recode:list){
			   System.out.println("流程实例ID："+recode.getProcessInstanceId()+"  variablesName："+ recode.getVariableName() 
			   + " variables:" + recode.getValue() );
		   }
	   }
   }
   
   //总结:
   //由于数据库中保存着历史信息以及正在运行的流程实例信息，在实际项目中对已完成任务的查看频率远不及对代办和可接任务的查看，
   //所以在activiti采用分开管理，把正在运行的交给RuntimeService
   //、TaskService管理，而历史数据交给HistoryService来管理。
   //这样做的好处在于，加快流程执行的速度，因为正在执行的流程的表中数据不会很大
}
