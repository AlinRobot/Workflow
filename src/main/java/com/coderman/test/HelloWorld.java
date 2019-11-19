package com.coderman.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by zhangyukang on 2019/11/19 12:18
 */
public class HelloWorld {
    private static ProcessEngine engine;


    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DataSource dataSource = context.getBean(DataSource.class);
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        System.out.println("获取流程引擎");
    }
    /**
     * 部署流程
     */
    @Test
    public void deployProcess(){
        RepositoryService repositoryService = engine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().name("请假流程01")
                .addClasspathResource("process/HelloWorld.bpmn")
                .addClasspathResource("process/HelloWorld.png").deploy();//创建一个部署的对象
        System.out.println("部署成功~："+deployment);
    }

    /**
     * 启动请假流程
     */
    @Test
    public void start(){
        RuntimeService runtimeService = engine.getRuntimeService();
        runtimeService.startProcessInstanceById("HelloWorld:1:4");//通过流程定义的id启动流程
        System.out.println("流程启动成功");
    }

    /**
     * 查询代办任务
     */
    @Test
    public void queryTask(){
        TaskService taskService = engine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("张三").list();
        if(null!=tasks&&!CollectionUtils.isEmpty(tasks)){
            System.out.println("张三的任务列表："+tasks);
            for (Task task : tasks) {
                System.out.println("任务ID："+task.getId());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
                System.out.println("流程执行ID："+task.getExecutionId());
                System.out.println("任务名称："+task.getName());
                System.out.println("办理人："+task.getAssignee());
                System.out.println("#######################################");
            }
        }else{
            System.out.println("张三暂无任务");
        }
    }

    /**
     * 办理任务
     */
    @Test
    public void doTask(){
        TaskService taskService = engine.getTaskService();
        taskService.complete("7502");
        System.out.println("完成任务");
    }
}
