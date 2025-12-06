package com.angelmorando.template.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelloService {
    public String sayHello() {
        return "Hello from Template Service";
    }
}
