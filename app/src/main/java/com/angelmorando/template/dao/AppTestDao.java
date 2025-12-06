package com.angelmorando.template.dao;

import com.angelmorando.template.dto.AppTest;
import com.angelmorando.template.repository.mybatis.AppTestMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppTestDao {
    private final AppTestMapper mapper;

    public AppTestDao(AppTestMapper mapper) {
        this.mapper = mapper;
    }

    public List<AppTest> findAll() {
        return mapper.findAll();
    }

    public AppTest findById(Integer id) {
        return mapper.findById(id);
    }

    public int insert(AppTest appTest) {
        return mapper.insert(appTest);
    }
}
