package com.coderman.ExclusiveGateWay;

import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排他网关
 * Created by zhangyukang on 2019/11/22 10:23
 */
public class TestExclusiveGateWay2 {

    private ProcessEngine processEngine= ProcessUtil.getProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void deploy(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().name("南昌大学共青学院请假流程")
                .addClasspathResource("process2/ExclusiveGateWay.bpmn")
                .addClasspathResource("process2/ExclusiveGateWay.png").deploy();
        System.out.println("南昌大学共青学院请假流程成功"+deploy);
    }

    /**
     * 开始请假流程(章宇康)
     */
    @Test
    public void start(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("myProcess_1");
        System.out.println("启动请假流程");
    }

    /**
     * 查询我的代办任务
     */
    @Test
    public void queryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("章宇康").list();
        if(null!=tasks&& !CollectionUtils.isEmpty(tasks)){
            for (Task task : tasks) {
                System.out.println("任务ID："+task.getId());
                System.out.println("任务创建时间："+task.getCreateTime());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
                System.out.println("任务办理人："+task.getAssignee());
                System.out.println("任务执行ID:"+task.getExecutionId());
                System.out.println("#################################");
            }
        }
    }

    /**
     * 办理任务
     */
    @Test
    public void doTask(){
        Map<String,Object> map=new HashMap<>();
        map.put("day",5);
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("10002",map);
        System.out.println("办理任务成功");
    }



}
