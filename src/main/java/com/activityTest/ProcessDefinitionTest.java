package com.activityTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * 流程定义管理
 * @author 苏志亮
 *
 */
public class ProcessDefinitionTest {
	
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
	 * zip包部署流程（部署方式二）
	 */
	@Test
    public void deployByZip(){
    	InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
    	ZipInputStream zin = new ZipInputStream(in);
    	Deployment deployment = processEngine.getRepositoryService()
    	             .createDeployment()
    	             .name("helloworld:压缩包部署")
    	             .addZipInputStream(zin)
    	             .deploy();
    	System.out.println("流程定义ID" + deployment.getId() + "  流程定义名" + deployment.getName() + "  流程定义时间"
				+ deployment.getDeploymentTime());

    }
	
	/**
	 * 查询流程定义
	 */
	@Test
	public void selectProcessDefinition(){
		List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService()
		             .createProcessDefinitionQuery()
		             .list();
		if(processDefinitions!=null && !processDefinitions.isEmpty()){
			for(ProcessDefinition pd:processDefinitions){
				System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
				System.out.println("流程定义的名称:"+pd.getName());//对应helloworld.bpmn文件中的name属性值
				System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中的id属性值
				System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
				System.out.println("资源名称bpmn文件:"+pd.getResourceName());
				System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
				System.out.println("部署对象ID："+pd.getDeploymentId());
				System.out.println("#########################################################");
			}
		}
	}
	
	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteProcessDefinition(){
		//使用部署ID删除流程定义
		String deploymentId = "1";
		/****不带级联的删除
		    *这种删除方式只能删除还没启动流程的，如果启动流程，则抛出异常。
		    *****/
		//processEngine.getRepositoryService()
		             //.deleteDeployment(deploymentId);
		/**级联删除，不管流程有没启动都能删除*/
		processEngine.getRepositoryService()
		             .deleteDeployment(deploymentId, true);
		System.out.println("##############################--删除成功");
	}
	
	/**
	 * 查看流程图 
	 * @throws IOException 
	 */
	@Test
	public void viwePic() throws IOException{
		//使用部署ID获取资源文件名称
		String deploymentId = "501";
		String outPath = "D:/学习资料/activiti工作流/testImage/";
		List<String> listName=processEngine.getRepositoryService()
		             .getDeploymentResourceNames(deploymentId);
		String pngName = "";//png文件名
		if(listName != null && !listName.isEmpty()){
			for(String name :listName){
				if(name.indexOf(".png") != -1){
					pngName = name;
				}
			}
			//根据部署id和png文件名获取图片文件输入流
		   InputStream in	= processEngine.getRepositoryService()
			             .getResourceAsStream(deploymentId, pngName);
		   FileUtils.copyInputStreamToFile(in, new File(outPath+pngName));
		}
	}
	
	
}
