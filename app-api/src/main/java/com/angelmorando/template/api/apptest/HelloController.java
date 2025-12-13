package com.angelmorando.template.api.apptest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.angelmorando.template.service.HelloService;
import com.angelmorando.template.api.ControllerUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ControllerUtils.API_BASE)
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping(ControllerUtils.HELLO)
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok(helloService.sayHello());
    }
}
