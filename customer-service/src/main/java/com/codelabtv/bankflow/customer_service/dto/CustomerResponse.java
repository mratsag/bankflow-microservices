package com.codelabtv.bankflow.customer_service.dto;

import com.codelabtv.bankflow.customer_service.entity.CustomerStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {

    private Long id;
    private String tcNo;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate birthDate;
    private CustomerStatus status;
    private LocalDateTime createdAt;


}
