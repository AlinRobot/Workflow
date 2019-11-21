package com.coderman.SchoolProcess;

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
import java.util.zip.ZipInputStream;

/**
 *
 *  #RepositoryService
 SELECT * FROM act_ge_bytearray;#二进制文件存储表
 SELECT * FROM act_re_deployment;#流程部署表
 SELECT * FROM act_re_procdef;#流程定义表

 #RuntimeService TaskService
 SELECT * FROM act_ru_execution;#工作流启动一次，只要没有执行完会有一条数据
 SELECT * FROM act_ru_task;#可能会有多条数据
 SELECT * FROM act_ru_variable;#记录流程运行时的流程变量
 SELECT * FROM act_ru_identitylink;#记录流程办理人的信息
 #HistoryService历史流程表
 SELECT * FROM act_hi_procinst#历史流程实例
 SELECT * FROM act_hi_taskinst#历史任务实例
 SELECT * FROM act_hi_actinst# 历史活动节点
 SELECT * FROM act_hi_varinst# 历史流程变量
 SELECT * form act_hi_comment# 历史批注表
 SELECT * FROM act_hi_attachment# 附件表

 * 南昌大学共青学院请假流程图
 * Created by zhangyukang on 2019/11/21 19:00
 */
public class TestSchoolProcess {

    private ProcessEngine processEngine= ProcessUtil.getProcessEngine();

    /**
     * 部署流程
     */
    @Test
    public void deploy(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        InputStream inputStream = this.getClass().getResourceAsStream("/process/SchoolProcess.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment().name("南昌大学共青学院请假流程")
                .addZipInputStream(zipInputStream).deploy();
        System.out.println("部署成功 "+deploy);
    }

    /**
     * 章宇康开始一个请假流程
     */
    @Test
    public void start(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("myProcess_1");
    }

    /**
     * 完成我的任务(提交申请，并且携带请假信息)
     */
    @Test
    public void doTask(){
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> map=new HashMap<>();
        map.put("message","我要请假4天");
        taskService.complete("12504",map);
    }

    /**
     * 熊消查询代办任务
     */
    @Test
    public void testQueryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().taskAssignee("熊消").list();
        if(list!=null&& !CollectionUtils.isEmpty(list)){
            for (Task task : list) {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务实例ID:"+task.getProcessInstanceId());
                System.out.println("创建时间:"+task.getCreateTime());
                System.out.println("任务名称:"+task.getName());
                System.out.println("办理人："+task.getAssignee());
                System.out.println("##########################");
            }
        }
        //拿到消息（流程变量message）
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String message = (String) runtimeService.getVariable("12501", "message");
        System.out.println("收到章宇康（message）的流程变量:"+message);
    }

    /**
     * 班长根据天数完成下一步的任务执行
     */
    @Test
    public void doNextTask(){
        TaskService taskService = processEngine.getTaskService();
        Map<String, Object> map=new HashMap<>();
        map.put("time",24*4);
        taskService.complete("15003",map);
    }

    /**
     * 任课老师同意。请假成功
     */
    @Test
    public void RKteach(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("7503");
    }

    /**
     * 班主任审核
     */
    @Test
    public void BZRdoTask(){
        TaskService taskService = processEngine.getTaskService();
        taskService.complete("20002");
    }

}
