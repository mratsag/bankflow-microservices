package com.codelabtv.bankflow.transactionservice.dto;

import com.codelabtv.bankflow.transactionservice.entity.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String senderIban;
    private String receiverIban;
    private BigDecimal amount;
    private String currency;
    private String description;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}