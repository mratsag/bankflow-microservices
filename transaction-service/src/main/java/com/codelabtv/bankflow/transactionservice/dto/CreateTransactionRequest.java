package com.codelabtv.bankflow.transactionservice.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {

    @NotBlank(message = "Sender IBAN is required")
    @Size(min = 26, max = 26, message = "IBAN must be 26 characters")
    private String senderIban;

    @NotBlank(message = "Sender IBAN is required")
    @Size(min = 26, max = 26, message = "IBAN must be 26 characters")
    private String receiverIban;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    private String description;


}
