package com.angelmorando.template.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.angelmorando.template.dao.AppTestDao;
import com.angelmorando.template.dto.AppTest;
import com.angelmorando.template.repository.AppTestRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AppTestRepositoryImpl implements AppTestRepository {
    private final AppTestDao dao;

    @Override
    public List<AppTest> all() {
        return dao.findAll();
    }

    @Override
    public AppTest find(Integer id) {
        return dao.findById(id);
    }

    @Override
    public AppTest save(AppTest appTest) {
        dao.insert(appTest);
        return appTest;
    }
}
