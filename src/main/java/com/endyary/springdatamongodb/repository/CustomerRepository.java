package com.endyary.springdatamongodb.repository;

import com.endyary.springdatamongodb.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, Long> {

    Optional<Customer> findByUsernameOrEmail(String username, String email);
}
