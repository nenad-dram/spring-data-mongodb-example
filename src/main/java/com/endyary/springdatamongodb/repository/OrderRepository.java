package com.endyary.springdatamongodb.repository;

import com.endyary.springdatamongodb.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, Long> {
}
