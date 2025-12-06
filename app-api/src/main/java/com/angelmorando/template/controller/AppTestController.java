package com.angelmorando.template.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angelmorando.template.dto.AppTestDto;
import com.angelmorando.template.service.AppTestService;
import com.angelmorando.template.mapper.AppTestDtoMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/app-tests")
@RequiredArgsConstructor
public class AppTestController {
    private final AppTestService service;
    private final AppTestDtoMapper mapper;

    @GetMapping
    public ResponseEntity<List<AppTestDto>> list() {
        return ResponseEntity.ok(mapper.toDtoList(service.getAll()));
    }

    @PostMapping
    public ResponseEntity<AppTestDto> create(@RequestBody AppTestDto appTest) {
        var domain = mapper.toDomain(appTest);
        var saved = service.create(domain);
        return ResponseEntity.created(URI.create("/api/app-tests/" + saved.getId())).body(mapper.toDto(saved));
    }
}
