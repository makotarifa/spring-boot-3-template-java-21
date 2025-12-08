package com.angelmorando.template.persistence.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.angelmorando.template.persistence.auth.model.UserRow;

@Mapper
public interface UserAuthDao {
    UserRow selectUserByUsername(@Param("username") String username);
    List<String> selectAuthoritiesByUsername(@Param("username") String username);

    int insertUser(UserRow user);
    int insertAuthority(@Param("username") String username, @Param("authority") String authority);
}
