package com.angelmorando.template.repository.mybatis;

import com.angelmorando.template.dto.AppTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppTestMapper {
    List<AppTest> findAll();
    AppTest findById(@Param("id") Integer id);
    int insert(AppTest appTest);
}
