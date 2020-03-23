package com.sjr.dao;

import com.sjr.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> selAll();

    void insertByBatch(List<User> users);

    void updateByBatch(List<User> list);

    List<User> selUserByUserName(@Param("userName") String userName);

    @Select("select * from ${table} where ${column} = #{value}")
    User findByColumn(@Param("table") String table,@Param("column") String column,@Param("value") String value);

    List<User> testInnerJoinUser();

    @Update(value = {
            "<script>" +
                    "update `user` set `userName` = #{userName} where `userName` = #{userName}" +
                    "</script>"

    })
    void updateListByUserName(@Param("userName") String userName);
    
}