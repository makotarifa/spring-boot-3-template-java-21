package com.angelmorando.template.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public String sayHello() {
        return "Hello from Template Service";
    }
}
