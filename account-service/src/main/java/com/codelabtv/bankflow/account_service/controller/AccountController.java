package com.codelabtv.bankflow.account_service.controller;


import com.codelabtv.bankflow.account_service.dto.AccountResponse;
import com.codelabtv.bankflow.account_service.dto.CreateAccountRequest;
import com.codelabtv.bankflow.account_service.dto.UpdateBalanceRequest;
import com.codelabtv.bankflow.account_service.entity.Account;
import com.codelabtv.bankflow.account_service.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        log.info("REST request to create account for customer: {}", request.getCustomerId());
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/iban/{iban}")
    public ResponseEntity<AccountResponse> getAccountByIban(@PathVariable String iban) {
        log.info("REST request to get account by IBAN: {}", iban);
        AccountResponse response = accountService.getAccountByIban(iban);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByCustomerId(
            @PathVariable Long customerId) {
        log.info("REST request to get accounts for customer: {}", customerId);
        List<AccountResponse> responses = accountService.getAccountByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }

    // HEM PATCH HEM POST KABUL ET! (Feign uyumluluğu için)
    @PostMapping("/{iban}/balance")  // ✅ POST EKLENDI!
    @PatchMapping("/{iban}/balance") // ✅ PATCH DA VAR!
    public ResponseEntity<AccountResponse> updateBalance(
            @PathVariable String iban,
            @Valid @RequestBody UpdateBalanceRequest request) {
        log.info("REST request to update balance for IBAN: {}, Operation: {}",
                iban, request.getOperationType());
        AccountResponse response = accountService.updateBalance(iban, request);
        return ResponseEntity.ok(response);
    }
}