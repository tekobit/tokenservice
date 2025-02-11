package com.zufarov.tokenservice.controllers;

import com.zufarov.tokenservice.services.UniqueIdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokensController {
    private final UniqueIdService uniqueIdService;

    public TokensController(UniqueIdService uniqueIdService) {
        this.uniqueIdService = uniqueIdService;

    }

    @GetMapping("/get-unique-id")
    public ResponseEntity<String> getUniqueId() {
        String token = uniqueIdService.getNextToken();
        return ResponseEntity.ok().body(token);
    }
}
