package com.codelabtv.bankflow.transactionservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {

    private Long id;
    private String iban;
    private Long customerId;
    private BigDecimal balance;
    private String currency;
    private String status;
}
