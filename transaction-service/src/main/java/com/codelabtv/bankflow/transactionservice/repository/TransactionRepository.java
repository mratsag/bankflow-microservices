package com.codelabtv.bankflow.transactionservice.repository;


import com.codelabtv.bankflow.transactionservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderIban(String senderIban);
    List<Transaction> findByReceiverIban(String receiverIban);
    List<Transaction> findBySenderIbanOrReceiverIban(String senderIban, String receiverIban);

}
