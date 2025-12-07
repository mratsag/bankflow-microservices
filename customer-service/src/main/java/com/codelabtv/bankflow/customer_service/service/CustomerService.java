package com.codelabtv.bankflow.customer_service.service;


import com.codelabtv.bankflow.customer_service.dto.CreateCustomerRequest;
import com.codelabtv.bankflow.customer_service.dto.CustomerResponse;
import com.codelabtv.bankflow.customer_service.entity.Customer;
import com.codelabtv.bankflow.customer_service.entity.CustomerStatus;
import com.codelabtv.bankflow.customer_service.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;


    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByTcNo(request.getTcNo())) {
            throw new RuntimeException("TC No already exists");
        }

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Customer customer = Customer.builder()
                .tcNo(request.getTcNo())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .birthDate(request.getBirthDate())
                .status(CustomerStatus.ACTIVE)
                .build();

        Customer saved =  customerRepository.save(customer);

        return mapToResponse(saved);

    }

    public CustomerResponse getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapToResponse(customer);
    }

    public CustomerResponse getCustomer(String tcNo) {
        log.info("Get customer with tcNo {}", tcNo);
        Customer customer = customerRepository.findByTcNo(tcNo)
                .orElseThrow(() -> new RuntimeException("Customer with tcNo not found"));

        return mapToResponse(customer);
    }

    public List<CustomerResponse> getAllCustomers() {
        log.info("Get all customers");
        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .tcNo(customer.getTcNo())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .birthDate(customer.getBirthDate())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}










