package com.codelabtv.bankflow.customer_service.repository;


import com.codelabtv.bankflow.customer_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByTcNo(String tcNo);
    Optional<Customer> findByEmail(String email);

    Boolean existsByTcNo(String tcNo);
    Boolean existsByEmail(String email);
}
