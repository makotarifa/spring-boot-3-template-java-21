package com.angelmorando.template.controller;

import com.angelmorando.template.dto.AppTest;
import com.angelmorando.template.repository.AppTestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/app-tests")
public class AppTestController {
    private final AppTestRepository repository;

    public AppTestController(AppTestRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<AppTest> list() {
        return repository.all();
    }

    @PostMapping
    public ResponseEntity<AppTest> create(@RequestBody AppTest appTest) {
        AppTest saved = repository.save(appTest);
        return ResponseEntity.created(URI.create("/api/app-tests/" + saved.getId())).body(saved);
    }
}
