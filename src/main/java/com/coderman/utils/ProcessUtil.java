package com.coderman.utils;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

/**
 * Created by zhangyukang on 2019/11/19 20:51
 */
public class ProcessUtil {


    public static ProcessEngine getProcessEngine(){
            ProcessEngine engine;
            ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            DataSource dataSource = context.getBean(DataSource.class);
            ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
            configuration.setDataSource(dataSource);
            configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
            engine = configuration.buildProcessEngine();
            return engine;
    }
}
