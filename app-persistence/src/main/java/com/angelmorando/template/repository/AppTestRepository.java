package com.angelmorando.template.repository;

import java.util.List;

import com.angelmorando.template.dto.AppTest;

public interface AppTestRepository {
    List<AppTest> all();
    AppTest find(Integer id);
    AppTest save(AppTest appTest);
}
