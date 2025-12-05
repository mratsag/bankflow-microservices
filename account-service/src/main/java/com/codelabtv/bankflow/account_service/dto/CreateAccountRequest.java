package com.codelabtv.bankflow.account_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {

    @NotNull(message = "Customer ID is required")
    @Min(value = 1, message = "Customer ID must be positive")
    private Long customerId;

    @NotNull(message = "Currency is required")
    @Pattern(regexp = "TRY|USD|EUR", message = "Currency must be TRY, USD or EUR")
    private String currency;

    private BigDecimal initialBalance = BigDecimal.ZERO;
}
