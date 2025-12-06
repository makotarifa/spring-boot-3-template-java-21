package com.angelmorando.template.service;

import com.angelmorando.template.dto.AppTest;
import com.angelmorando.template.repository.AppTestRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppTestService {
    private final AppTestRepository repository;

    public List<AppTest> getAll() { return repository.all(); }

    public AppTest create(AppTest appTest) { return repository.save(appTest); }
}
