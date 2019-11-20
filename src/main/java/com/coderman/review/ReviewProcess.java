package com.coderman.review;

import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 *
 * 流程复习
 * Created by zhangyukang on 2019/11/20 13:43
 */
public class ReviewProcess {


    private static ProcessEngine processEngine= ProcessUtil.getProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void deployProcess(){
        InputStream in = this.getClass().getResourceAsStream("/process/HelloWorld.zip");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ZipInputStream zipInputStream = new ZipInputStream(in);
        repositoryService.createDeployment().name("请假流程").addZipInputStream(zipInputStream).deploy();
        System.out.println("流程部署成功");
    }

    /**
     * 启动流程
     */
    @Test
    public void startProcess(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorld", "业务ID");
        System.out.println("流程启动成功："+processInstance);
    }

    /**
     * 查询代办 任务
     */
    @Test
    public void queryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks= taskService.createTaskQuery().taskAssignee("张三").list();
        if(null!=tasks&& !CollectionUtils.isEmpty(tasks)){
            for (Task task : tasks) {
                System.out.println("任务ID："+task.getId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务办理人:"+task.getAssignee());
                System.out.println("￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥");
            }
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void doTask(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("2504");
        System.out.println("完成任务");
    }
    /**
     * 流程定义相关的API
     */
    @Test
    public void processDef(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        //获取所有的流程定义
        if(!CollectionUtils.isEmpty(list)){
            for (ProcessDefinition processDefinition : list) {
                System.out.println(processDefinition);
            }
        }
    }

    /**
     * 根据部署id获取流程定义
     */
    @Test
    public void getProcessDefByDeployId(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().deploymentId("1");
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        System.out.println(processDefinition);
    }

    /**
     * 根据流程部署id删除流程定义对象
     */
    @Test
    public void deleteProcessDefByDeployId(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.deleteDeployment("1");
        System.out.println("流程删除成功");
    }

    /**
     * 根据流程部署id删除流程定义id，级联删除
     * 删除之后任务表，以及流程执行表中的数据都会被删除
     */
    @Test
    public void deleteProcessDefByDeloyed2(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.deleteDeployment("1",true);
    }

    /**
     * 获取最新的流程定义
     */
    @Test
    public void getLastProcessDef(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().latestVersion();
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        if(processDefinition!=null){
            System.out.println("最新的流程定义对象是："+processDefinition);
        }
    }
    /**
     * 已知部署id获取流程图
     */
    @Test
    public void viewImageByDeployId(){
        BufferedOutputStream bufferedOutputStream=null;
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().deploymentId("1");
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        InputStream processDiagram = repositoryService.getProcessDiagram(processDefinition.getId());
        //将获取的流程图放到的盘
        File file=new File("d:/HelloWorld.PNG");
        createImage(bufferedOutputStream, processDiagram, file);
    }

    /**
     * 创建图片
     * @param bufferedOutputStream
     * @param processDiagram
     * @param file
     */
    private void createImage(BufferedOutputStream bufferedOutputStream, InputStream processDiagram, File file) {
        try {
            bufferedOutputStream =new BufferedOutputStream(new FileOutputStream(file));
            int len=0;
            byte[] b=new byte[1024];
            while ((len=processDiagram.read(b))!=-1){
                bufferedOutputStream.write(b,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(bufferedOutputStream!=null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(processDiagram!=null){
                try {
                    processDiagram.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 获取流程运行过程中的流程图
     */
    @Test
    public void readProcessImage(){
        BufferedOutputStream bufferedOutputStream=null;
        RepositoryService repositoryService = processEngine.getRepositoryService();
        InputStream processDiagram = repositoryService.getProcessDiagram("HelloWorld:1:4");
        //将获取的流程图放到的盘
        File file=new File("d:/HelloWorld.PNG");
        createImage(bufferedOutputStream, processDiagram, file);
    }
}
