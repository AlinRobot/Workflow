package com.coderman.ExclusiveGateWay;

import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 排他网关测试
 * Created by zhangyukang on 2019/11/21 21:13
 */
public class TestExclusiveGateWay {

    private ProcessEngine processEngine= ProcessUtil.getProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void deploy(){
        InputStream inputStream = this.getClass().getResourceAsStream("/process/ExclusiveGateWay.zip");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        repositoryService.createDeployment().name("员工申请加薪流程").addZipInputStream(zipInputStream).deploy();
        System.out.println("流程部署成功");
    }

    /**
     * 启动加薪流程
     */
    @Test
    public void startProcess(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("员工申请加薪流程");
    }

    /**
     * 查询我的代办任务
     */
    @Test
    public void queryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().taskAssignee("章宇康").list();
        if(null!=list&&!CollectionUtils.isEmpty(list)){
            for (Task task : list) {
                System.out.println("任务ID"+task.getId());
                System.out.println("流程实例ID:"+task.getProcessInstanceId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务办理人："+task.getAssignee());
                System.out.println("任务创建时间:"+task.getCreateTime());
                System.out.println("#########################");
            }
        }
    }

    /**
     * 完成任务(通过money的大小)
     */
    @Test
    public void doTask(){
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> map=new HashMap<>();
        map.put("money",2001);
        taskService.complete("2504",map);
        System.out.println("完成任务");
    }

    /**
     * 开发部大佬完成任务
     */
    @Test
    public void doTaskNext(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("10002");
        System.out.println("完成任务");
    }

}


