package com.coderman.ParallelGateway;

import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 测试并行网关
 * Created by zhangyukang on 2019/11/21 22:06
 */
public class TestParallelGateway {

    private ProcessEngine processEngine= ProcessUtil.getProcessEngine();

    /**
     * 部署
     */
    @Test
    public void deploy(){
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/process/ParallelGateway.zip");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);
        Deployment deploy = repositoryService.createDeployment().name("顾客购物流程").addZipInputStream(zipInputStream).deploy();
        System.out.println("部署成功："+deploy);
    }

    /**
     * 启动购物流程
     */
    @Test
    public void startProcess(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("购物流程");
        System.out.println("启动购物流程");
    }

    /**
     * 查询买家和买家的任务
     */
    @Test
    public void testQueryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().taskAssignee("买家").list();
        if(list!=null&&list.size()>0){
            for (Task task : list) {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务创建时间："+task.getCreateTime());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行实例ID:"+task.getExecutionId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务执行人："+task.getAssignee());
                System.out.println("########################");
            }
        }
    }

    /**
     * 买家完成付款的操作
     */
    @Test
    public void payTask(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("5007");
    }

    /**
     * 卖家家完成收货操纵
     */
    @Test
    public void getMoney(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("7502");
    }

    /**
     * 卖家完成发货操作
     */
    @Test
    public void sendTask(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("5010");
    }

    /**
     * 卖家完成收货流程
     */
    @Test
    public void getGoods(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("10002");
    }
}
