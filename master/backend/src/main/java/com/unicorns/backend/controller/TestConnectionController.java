package com.unicorns.backend.controller;


import com.unicorns.backend.model.Prize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestConnectionController {

    @GetMapping()
    public ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("Successfully connected to the server");
    }
}
