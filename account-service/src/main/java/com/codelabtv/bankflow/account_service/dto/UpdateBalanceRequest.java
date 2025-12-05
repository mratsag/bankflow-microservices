package com.codelabtv.bankflow.account_service.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jdk.dynalink.Operation;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBalanceRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Operation type is required")
    private OperationType operationType;

    public enum OperationType {
        DEPOSIT, WITHDRAW
    }
}
