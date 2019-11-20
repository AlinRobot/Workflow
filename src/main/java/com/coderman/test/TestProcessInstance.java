package com.coderman.test;

import com.coderman.utils.ProcessUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipInputStream;

/**
 * Created by zhangyukang on 2019/11/19 20:53
 */
public class TestProcessInstance {

    private ProcessEngine processEngine= ProcessUtil.getProcessEngine();

    @Test
    public void test1(){
        System.out.println(processEngine);
    }

    /**
     * 部署流程
     */
    @Test
    public void deploy(){
        InputStream in = this.getClass().getResourceAsStream("/process/HelloWorld.zip");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = repositoryService.createDeployment().name("请假流程").addZipInputStream(zipInputStream).deploy();
        System.out.println("请假流程部署成功"+deployment);
    }

    /**
     * 启动请假流程
     */
    @Test
    public void startProcess(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        runtimeService.startProcessInstanceById("HelloWorld:4:30004");
        HashMap<String, Object> map = new HashMap<>();
        map.put("key","value="+ UUID.randomUUID().toString());//map用于在流程运行过程中传递信息，可能决定流程的走向
//        runtimeService.startProcessInstanceById("HelloWorld:1:4", map);
        /**
         * 第一个参数：流程定义ID
         * 第二个参数：把业务id和流程执行ID进行绑定的参数。
         */
//        runtimeService.startProcessInstanceById("HelloWorld:1:5004","bizKey");
        /**
         * 第一个参数：流程定义ID
         * 第二个参数：把业务id和流程执行ID进行绑定的参数。
         * 第三个参数：流程变量
         */
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("key2","value2="+UUID.randomUUID().toString().toUpperCase());
        runtimeService.startProcessInstanceById("HelloWorld:1:5004","bizKey2",map2);
        System.out.println("启动流程");
    }

    /**
     * 实际开发过程中使用这种启动流程的方式
     */
    @Test
    public void doStartProcess(){
        Map<String,Object> map=new HashMap<>();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        map.put("key","value:"+UUID.randomUUID().toString().toUpperCase());
        runtimeService.startProcessInstanceByKey("HelloWorld","BizkKey");

    }
    /**
     * 查询代办任务
     */
    @Test
    public void queryTasks(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("张三").list();
        if(null!=tasks&& !CollectionUtils.isEmpty(tasks)){
            for (Task task : tasks) {
                System.out.println("任务id："+task.getId());
                System.out.println("流程实例id："+task.getProcessInstanceId());
                System.out.println("流程定义id："+task.getProcessDefinitionId());
                System.out.println(task.getName());
                System.out.println("办理人："+task.getAssignee());
                System.out.println("######################");
            }
        }else{
            System.out.println("暂时还没有任务");
        }
    }

    /**
     * 完成代办任务
     */
    @Test
    public void doTask(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("22504");
    }

}
