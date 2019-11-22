package com.coderman.ParallelGateway;

import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.mock.MockServiceTask;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 并行网关
 * Created by zhangyukang on 2019/11/22 11:03
 */
public class TestParallelGateway2 {

    private ProcessEngine processEngine= ProcessUtil.getProcessEngine();


    @Test
    public void deploy(){
        InputStream inputStream = this.getClass().getResourceAsStream("/process2/ParallelGateway.zip");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment().name("三好学生评优流程").addZipInputStream(zipInputStream).deploy();
        System.out.println("流程部署成功："+deploy);
    }

    /**
     * 启动流程
     */
    @Test
    public void start(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        runtimeService.startProcessInstanceByKey("")
        runtimeService.startProcessInstanceByKey("三好学生评优流程");
    }

    /**
     * 学生办理流程
     */
    @Test
    public void doTask(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("17504");
    }

    /**
     * 查询代办流程
     */
    @Test
    public void queryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().list();
        if(list!=null&&list.size()>0){
            for (Task task : list) {
                System.out.println("任务ID："+task.getId());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务办理人："+task.getAssignee());
                System.out.println("##############################");
            }
        }
    }


}
