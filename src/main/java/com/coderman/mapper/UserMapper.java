package com.coderman.mapper;

import com.coderman.bean.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhangyukang on 2019/11/19 11:05
 */
public interface UserMapper {

    @Select(value = "select * from user")
    List<User> findAll();
}
