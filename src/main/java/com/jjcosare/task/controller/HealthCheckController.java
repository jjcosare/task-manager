package com.jjcosare.task.controller;

import com.jjcosare.task.ApiVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/healthcheck")
public class HealthCheckController {

    @GetMapping(produces = {ApiVersion.MEDIA_TYPE_V1_API, ApiVersion.MEDIA_TYPE_LTS_API,
        ApiVersion.MEDIA_TYPE_V2_API, ApiVersion.MEDIA_TYPE_V3_API
    })
    public ResponseEntity<Map<String, String>> getHealthCheck() {
        return ResponseEntity.ok(Map.of("api.status", "up"));
    }

}
