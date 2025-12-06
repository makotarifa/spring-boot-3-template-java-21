package com.angelmorando.template.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angelmorando.template.dto.AppTest;
import com.angelmorando.template.service.AppTestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/app-tests")
@RequiredArgsConstructor
public class AppTestController {
    private final AppTestService service;

    @GetMapping
    public ResponseEntity<List<AppTest>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<AppTest> create(@RequestBody AppTest appTest) {
        AppTest saved = service.create(appTest);
        return ResponseEntity.created(URI.create("/api/app-tests/" + saved.getId())).body(saved);
    }
}
