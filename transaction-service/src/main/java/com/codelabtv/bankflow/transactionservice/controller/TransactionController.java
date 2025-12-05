package com.codelabtv.bankflow.transactionservice.controller;

import com.codelabtv.bankflow.transactionservice.dto.CreateTransactionRequest;
import com.codelabtv.bankflow.transactionservice.dto.TransactionResponse;
import com.codelabtv.bankflow.transactionservice.service.TransactionService;
import feign.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createtTransaction(
            @Valid @RequestBody CreateTransactionRequest request){
        log.info("REST request to create transaction");
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/iban/{iban}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByIban(
            @PathVariable String iban){
        log.info("REST request to get transactions by IBAN: {}", iban);
        List<TransactionResponse> responses = transactionService.getTransactionsByIban(iban);
        return ResponseEntity.ok(responses);
    }

}
