package com.endyary.springdatamongodb.repository;

import com.endyary.springdatamongodb.model.Customer;
import com.endyary.springdatamongodb.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, Long> {

    Optional<List<Order>> findByStatus(Order.Status status);

    Optional<List<Order>> findByCustomer(Customer customer);
}
