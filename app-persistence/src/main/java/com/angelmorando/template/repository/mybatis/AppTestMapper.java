package com.angelmorando.template.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.angelmorando.template.dto.AppTest;

@Mapper
public interface AppTestMapper {
    List<AppTest> findAll();
    AppTest findById(@Param("id") Integer id);
    int insert(AppTest appTest);
}
