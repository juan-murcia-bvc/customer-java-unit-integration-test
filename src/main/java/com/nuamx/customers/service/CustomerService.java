package com.nuamx.customers.service;

import com.nuamx.customers.model.Customer;
import com.nuamx.customers.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer saveCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + customer.getEmail());
        }
        return customerRepository.save(customer);
    }

}
