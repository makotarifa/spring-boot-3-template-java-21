package com.angelmorando.template.api.apptest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import com.angelmorando.template.api.ControllerUtils;

@RestController
@RequiredArgsConstructor
public class HealthController {

    @GetMapping(ControllerUtils.HEALTH)
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
