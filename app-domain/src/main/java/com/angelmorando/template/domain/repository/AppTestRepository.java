package com.angelmorando.template.domain.repository;

import java.util.List;

import com.angelmorando.template.domain.model.AppTest;

public interface AppTestRepository {
    List<AppTest> all();
    AppTest find(Integer id);
    AppTest save(AppTest appTest);
}
