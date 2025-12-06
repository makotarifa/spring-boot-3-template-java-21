package com.angelmorando.template.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.angelmorando.template.dto.AppTest;
import com.angelmorando.template.repository.mybatis.AppTestMapper;

@Component
@RequiredArgsConstructor
public class AppTestDao {
    private final AppTestMapper mapper;

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
