package com.coderman.review;

import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 *
 * Created by zhangyukang on 2019/11/20 16:37
 */
public class ReviewProcess2 {

    private ProcessEngine processEngine= ProcessUtil.getProcessEngine();

    @Test
    public void getProcessTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks= taskService.createTaskQuery().taskAssignee("张三").list();
        System.out.println(tasks);
    }
    /**
     * 判断流程是否结束
     */
    @Test
    public void isFishTask(){
        //已知流程实例id
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                                                        .processInstanceId("2501").singleResult();
        if(processInstance!=null){
            System.out.println("流程没有结束");
        }else {
            System.out.println("流程结束");
        }
        //已知任务id查询流程是否结束
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId("2504").singleResult();
        String processInstanceId = task.getProcessInstanceId();
        RuntimeService runtimeService1 = processEngine.getRuntimeService();
        ProcessInstance processInstance1 = runtimeService1.createProcessInstanceQuery()
                                                           .processInstanceId(processInstanceId).singleResult();
        if(null!=processInstance1){
            System.out.println("任务没有完成");
        }else{
            System.out.println("任务已经完成");
        }
    }

}
