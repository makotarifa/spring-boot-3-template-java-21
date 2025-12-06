package com.angelmorando.template.repository;

import com.angelmorando.template.dto.AppTest;

import java.util.List;

public interface AppTestRepository {
    List<AppTest> all();
    AppTest find(Integer id);
    AppTest save(AppTest appTest);
}
