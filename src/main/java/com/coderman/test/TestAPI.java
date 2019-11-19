package com.coderman.test;

import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.util.List;
/**
 * #RepositoryService
 SELECT * FROM act_ge_bytearray;#二进制文件存储表
 SELECT * FROM act_re_deployment;#流程部署表
 SELECT * FROM act_re_procdef;#流程定义表
 SELECT * FROM act_ge_property;#工作流的id算法和版本信息
 #RuntimeService TaskService
 SELECT * FROM act_ru_execution;#工作流启动一次，只要没有执行完会有一条数据
 SELECT * FROM act_ru_task;#可能会有多条数据
 SELECT * FROM act_ru_variable;#记录流程运行时的流程变量
 SELECT * FROM act_ru_identitylink;#记录流程办理人的信息
 */

/**
 * 流程核心API
 * Created by zhangyukang on 2019/11/19 11:53
 */
public class TestAPI {
    private ProcessEngine engine;


    {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DataSource dataSource = context.getBean(DataSource.class);
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        System.out.println(engine);
    }



    @Test
    public void TestAPIService(){
        System.out.println(engine);
        //获取service
        DynamicBpmnService dynamicBpmnService = engine.getDynamicBpmnService();
        //页面表单的服务
        FormService formService = engine.getFormService();
        //查询历史的服务
        HistoryService historyService = engine.getHistoryService();
        //对工作流用户操作的表
        IdentityService identityService = engine.getIdentityService();
        ManagementService managementService = engine.getManagementService();
        //流程图的部署，修改，删除的service(act_ge_bytearray , act_re_deployment,act_re_model,act_re_procdef)
        RepositoryService repositoryService = engine.getRepositoryService();
        //流程的运行`act_ru_event_subscr``act_ru_execution``act_ru_identitylink``act_ru_job``act_ru_task``act_ru_variable`
        RuntimeService runtimeService = engine.getRuntimeService();
        TaskService taskService = engine.getTaskService();
        List<Task> list = taskService.createTaskQuery().list();
        System.out.println("task="+list);

        //
        System.out.println(dynamicBpmnService);
        System.out.println(formService);
        System.out.println(historyService);
        System.out.println(identityService);
        System.out.println(managementService);
        System.out.println(repositoryService);
        System.out.println(runtimeService);
    }

}
