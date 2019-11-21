package com.coderman.test;

import com.coderman.bean.User;
import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程变量相关
 * Created by zhangyukang on 2019/11/21 15:51
 */
public class TestProcessVariable {


    private ProcessEngine engine= ProcessUtil.getProcessEngine();

    /**
     * 流程部署
     */
    @Test
    public void deploy(){
        InputStream inputStream = this.getClass().getResourceAsStream("/process/HelloWorld.zip");
        RepositoryService repositoryService = engine.getRepositoryService();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment().name("请假流程").addZipInputStream(zipInputStream).deploy();
        System.out.println("流程部署成功"+deploy);
    }

    /**
     * 启动流程
     */
    @Test
    public void start(){
        RuntimeService runtimeService = engine.getRuntimeService();
        Map<String, Object> map=new HashMap<>();
        map.put("请假天数",3);
        map.put("原因描述","去约会女友");
        map.put("时间",new Date());
        runtimeService.startProcessInstanceByKey("HelloWorld",map);
        System.out.println("流程开始");
    }

    /**
     * 设置流程变量的的方法
     */
    @Test
    public void testSetVariable(){
        RuntimeService runtimeService = engine.getRuntimeService();
        runtimeService.setVariable("2501","申请人","章宇康2");
        runtimeService.setVariable("2501","用户对象",new User());
        System.out.println("设置流程变量成功");
    }
    @Test
    public void testSetVariable2(){
        TaskService taskService = engine.getTaskService();
        taskService.setVariable("2507","申请人2","章宇康2");
        taskService.setVariable("2507","用户对象2",new User());
        System.out.println("设置流程变量成功");
    }

    /**
     * 获取流程变量
     */
    @Test
    public void testGetVariable(){
        RuntimeService runtimeService = engine.getRuntimeService();
        String description = (String) runtimeService.getVariable("2501", "原因描述");
        System.out.println(description);
        User user = (User) runtimeService.getVariable("2501", "用户对象");
        System.out.println(user+":"+user.hashCode());
        User user2 = (User) runtimeService.getVariable("2501", "用户对象2");
        System.out.println(user2+":"+user2.hashCode());
    }


}
