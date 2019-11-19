package com.coderman.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

/**
 * Created by zhangyukang on 2019/11/19 11:21
 */
public class Init_table {

    private static ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");

    private DataSource dataSource= context.getBean(DataSource.class);


    @Test
    public void initTable1(){
        ProcessEngineConfiguration configuration=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
//        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setDatabaseSchemaUpdate("drop-create");
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);
    }
}
