package com.endyary.springdatamongodb.repository;

import com.endyary.springdatamongodb.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, Long> {
}
