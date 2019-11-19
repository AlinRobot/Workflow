package com.coderman.test;

import com.coderman.bean.User;
import com.coderman.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhangyukang on 2019/11/19 10:57
 */
public class TestGetConnection {

    private ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");

    @Test
    public void testGetConnection(){
        DataSource bean = ctx.getBean(DataSource.class);
        try {
            System.out.println(bean.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testFindAllUser(){
        SqlSessionFactory bean = ctx.getBean(SqlSessionFactory.class);
        SqlSession sqlSession = bean.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> all = mapper.findAll();
        System.out.println(all);
    }
}
