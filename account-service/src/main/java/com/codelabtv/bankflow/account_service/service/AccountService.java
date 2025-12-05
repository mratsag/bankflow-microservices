package com.codelabtv.bankflow.account_service.service;


import com.codelabtv.bankflow.account_service.dto.AccountResponse;
import com.codelabtv.bankflow.account_service.dto.CreateAccountRequest;
import com.codelabtv.bankflow.account_service.dto.UpdateBalanceRequest;
import com.codelabtv.bankflow.account_service.entity.Account;
import com.codelabtv.bankflow.account_service.entity.AccountStatus;
import com.codelabtv.bankflow.account_service.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request){
        log.info("Create account for customer: {}", request.getCustomerId());

        String iban = generateIban();

        Account account = Account.builder()
                .iban(iban)
                .customerId(request.getCustomerId())
                .currency(request.getCurrency())
                .balance(request.getInitialBalance())
                .status(AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with IBAN: {}", iban);

        return mapToResponse(savedAccount);
    }

    public AccountResponse getAccountByIban(String iban){
        log.info("Get account by IBAN: {}", iban);
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found with iban: " + iban));

        return mapToResponse(account);
    }

    public List<AccountResponse> getAccountByCustomerId(Long customerId){
        log.info("Get account by customerId: {}", customerId);
        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public AccountResponse updateBalance(String iban, UpdateBalanceRequest request){
        log.info("Update balance for IBAN: {}, Operation: {}, Amount: {}",
                iban, request.getOperationType(), request.getAmount());

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found with iban: " + iban));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account status not active");
        }

        if (request.getOperationType() == UpdateBalanceRequest.OperationType.DEPOSIT) {
            account.setBalance(account.getBalance().add(request.getAmount()));
        } else if (request.getOperationType() == UpdateBalanceRequest.OperationType.WITHDRAW) {
            if (account.getBalance().compareTo(request.getAmount()) < 0){
                throw new RuntimeException("Insufficient balance");
            }
            account.setBalance(account.getBalance().subtract(request.getAmount()));
        }

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with new Balance: {}", updatedAccount.getBalance());

        return mapToResponse(updatedAccount);
    }

    private String generateIban(){
        String countryCode = "TR";
        String checkdigits = String.format("%02d", (new Random()).nextInt(100));
        String bankCode = "00001";
        String reserved = "0";
        String accountNumber = String.format("%016d", (new Random()).nextLong() & Long.MAX_VALUE).substring(0, 16);

        return countryCode + checkdigits + bankCode + reserved + accountNumber;
    }
    private AccountResponse mapToResponse(Account account){
        return AccountResponse.builder()
                .id(account.getId())
                .iban(account.getIban())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

}
















