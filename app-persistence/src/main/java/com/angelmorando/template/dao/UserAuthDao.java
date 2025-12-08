package com.angelmorando.template.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserAuthDao {
    Map<String, Object> selectUserByUsername(@Param("username") String username);
    List<String> selectAuthoritiesByUsername(@Param("username") String username);

    int insertUser(Map<String, Object> user);
    int insertAuthority(@Param("username") String username, @Param("authority") String authority);
}
