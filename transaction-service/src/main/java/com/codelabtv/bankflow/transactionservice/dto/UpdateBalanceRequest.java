package com.codelabtv.bankflow.transactionservice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceRequest {

    private BigDecimal amount;
    private OperationType operationType;

    public enum OperationType {
        DEPOSIT,
        WITHDRAW
    }
}
