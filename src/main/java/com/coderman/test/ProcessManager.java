package com.coderman.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程的管理
 * Created by zhangyukang on 2019/11/19 16:50
 */
public class ProcessManager {

    private static ProcessEngine engine;

    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DataSource dataSource = context.getBean(DataSource.class);
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
    }

    /**
     * 部署流程
     */
    @Test
    public void test1(){
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/process/HelloWorld.zip");
        RepositoryService repositoryService = engine.getRepositoryService();
//        repositoryService.de
        ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);
        Deployment deploy = repositoryService.createDeployment().name("请假流程").addZipInputStream(zipInputStream).deploy();
        System.out.println("部署成功："+deploy);
    }

    /**
     * 查询流程部署信息
     */
    @Test
    public void queryDeploymentInfo(){
        RepositoryService repositoryService = engine.getRepositoryService();
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        System.out.println(list);

        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId("1").singleResult();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
        System.out.println(deployment.getDeploymentTime());

    }

    /**
     * 查询流程定义信息
     */
    @Test
    public void queryProcessDef(){
        RepositoryService repositoryService = engine.getRepositoryService();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        ProcessDefinitionQuery he = repositoryService.createProcessDefinitionQuery().processDefinitionNameLike("He");
        System.out.println(he);
        System.out.println(list);
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteProcessDef(){
        RepositoryService repositoryService = engine.getRepositoryService();
//        repositoryService.deleteDeployment();
//        repositoryService.deleteDeployment();
//        repositoryService.delete
        repositoryService.deleteDeployment("5001",true);
    }

    /**
     * 查看流程图(根据流程定义id)
     */
    @Test
    public void processImage(){
        RepositoryService repositoryService = engine.getRepositoryService();
        InputStream inputStream = repositoryService.getProcessDiagram("HelloWorld:1:4");
        System.out.println(inputStream);
        File file=new File("d:/HelloWorld.png");
        BufferedOutputStream bufferedOutputStream=null;
        WriteImage(inputStream, file, bufferedOutputStream);
    }

    /**
     * 查看流程图(根据流程部署id)
     */
    @Test
    public void processImage2(){
        RepositoryService repositoryService = engine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId("1").singleResult();
        InputStream inputStream = repositoryService.getProcessDiagram(processDefinition.getId());
        System.out.println(inputStream);
        File file=new File("d:/"+processDefinition.getDiagramResourceName()+"");
        BufferedOutputStream bufferedOutputStream=null;
        WriteImage(inputStream, file, bufferedOutputStream);
    }

    private void WriteImage(InputStream inputStream, File file, BufferedOutputStream bufferedOutputStream) {
        try {
            bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(file));
            System.out.println(bufferedOutputStream);
            int len=0;
            byte[] b=new byte[1024];
            while ((len=inputStream.read(b))!=-1){
                bufferedOutputStream.write(b,0,len);
                bufferedOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(bufferedOutputStream!=null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 查询最新版本的流程定义
     */
    @Test
    public void queryLastestProcessDef(){
        RepositoryService repositoryService = engine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().latestVersion().singleResult();
        System.out.println(processDefinition);
        System.out.println(processDefinition.getId());
        System.out.println(processDefinition.getDeploymentId());
    }

}
