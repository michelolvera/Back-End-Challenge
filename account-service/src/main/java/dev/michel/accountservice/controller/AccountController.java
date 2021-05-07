package dev.michel.accountservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AccountController {
    @GetMapping
    public ResponseEntity<String> listAllInvoices() {
        return ResponseEntity.ok("OK");
    }
}
