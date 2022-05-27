package com.endyary.springdatamongodb.repository;

import com.endyary.springdatamongodb.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, Long> {
}
