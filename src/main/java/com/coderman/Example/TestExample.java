package com.coderman.Example;

import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyukang on 2019/11/21 17:50
 */
public class TestExample {

    private ProcessEngine engine= ProcessUtil.getProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void deploy(){
        RepositoryService repositoryService = engine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().name("计本班规").addClasspathResource("process/Example.bpmn")
                .addClasspathResource("process/Example.png").deploy();
        System.out.println(deploy+"部署成功");
    }
    @Test
    public void startProcess(){
        RuntimeService runtimeService =engine.getRuntimeService();
        ProcessInstance example = runtimeService.startProcessInstanceByKey("myProcess_1");
        System.out.println("启动流程"+example);
    }

    /**
     * 查询我的代办任务
     */
    @Test
    public void queryTasks(){
        TaskService taskService = engine.getTaskService();
        List<Task> list= taskService.createTaskQuery().taskAssignee("熊消").list();
        if(null!=list&&list.size()>0){
            for (Task task : list) {
                System.out.println(task.getId());
                System.out.println(task.getName());
                System.out.println(task.getCreateTime());
                System.out.println("######################");
            }
        }
    }

    /**
     * 完成我的任务
     */
    @Test
    public void doTask(){
        TaskService taskService = engine.getTaskService();
        Map<String, Object> map=new HashMap<>();
        map.put("outcome","重要");
        taskService.complete("25003",map);
        System.out.println("完成任务");
    }

}
