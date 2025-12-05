package com.codelabtv.bankflow.transactionservice.service;

import com.codelabtv.bankflow.transactionservice.client.AccountClient;
import com.codelabtv.bankflow.transactionservice.dto.*;
import com.codelabtv.bankflow.transactionservice.entity.Transaction;
import com.codelabtv.bankflow.transactionservice.entity.TransactionStatus;
import com.codelabtv.bankflow.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        log.info("Creating transaction from {} to {}, amount: {}",
                request.getSenderIban(), request.getReceiverIban(), request.getAmount());

        if (request.getSenderIban().equals(request.getReceiverIban())) {
            throw new RuntimeException("Cannot transfer to the same account!");
        }

        Transaction transaction = Transaction.builder()
                .senderIban(request.getSenderIban())
                .receiverIban(request.getReceiverIban())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(TransactionStatus.PENDING)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        try {
            log.info("Checking sender account: {}", request.getSenderIban());
            AccountResponse senderAccount = accountClient.getAccountByIban(request.getSenderIban());

            if (senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient balance!");
            }

            log.info("Checking receiver account: {}", request.getReceiverIban());
            AccountResponse receiverAccount = accountClient.getAccountByIban(request.getReceiverIban());

            log.info("Withdrawing {} from sender account", request.getAmount());
            UpdateBalanceRequest withdrawRequest = new UpdateBalanceRequest(
                    request.getAmount(),
                    UpdateBalanceRequest.OperationType.WITHDRAW
            );
            accountClient.updateBalance(request.getSenderIban(), withdrawRequest);

            log.info("Depositing {} to receiver account", request.getAmount());
            UpdateBalanceRequest depositRequest = new UpdateBalanceRequest(
                    request.getAmount(),
                    UpdateBalanceRequest.OperationType.DEPOSIT
            );
            accountClient.updateBalance(request.getReceiverIban(), depositRequest);

            savedTransaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(savedTransaction);

            log.info("Transaction completed successfully: {}", savedTransaction.getId());

        } catch (Exception e) {
            log.error("Transaction failed: {}", e.getMessage());
            savedTransaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(savedTransaction);
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }

        return mapToResponse(savedTransaction);
    }

    public List<TransactionResponse> getTransactionsByIban(String iban) {
        log.info("Fetching transactions for IBAN: {}", iban);
        return transactionRepository.findBySenderIbanOrReceiverIban(iban, iban)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .senderIban(transaction.getSenderIban())
                .receiverIban(transaction.getReceiverIban())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .description(transaction.getDescription())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}