package com.example.wallet_service.controller;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService service;

    @PostMapping("/wallet")
    public ResponseEntity<?> update(@Valid @RequestBody WalletRequest req) {
        return ResponseEntity.ok(service.process(req));
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<?> get(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.getBalance(id));
    }
}
