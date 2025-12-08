package com.angelmorando.template.service.apptest;

import java.util.List;

import org.springframework.stereotype.Service;

import com.angelmorando.template.domain.apptest.model.AppTest;
import com.angelmorando.template.domain.apptest.repository.AppTestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppTestService {
    private final AppTestRepository repository;

    public List<AppTest> getAll() { return repository.all(); }

    public AppTest create(AppTest appTest) { return repository.save(appTest); }
}
