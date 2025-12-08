package com.angelmorando.template.persistence.apptest.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.angelmorando.template.domain.apptest.model.AppTest;

@Mapper
public interface AppTestDao {
    List<AppTest> findAll();
    AppTest findById(@Param("id") Integer id);
    int insert(AppTest appTest);
}