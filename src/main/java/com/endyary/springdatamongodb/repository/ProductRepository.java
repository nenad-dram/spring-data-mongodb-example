package com.endyary.springdatamongodb.repository;

import com.endyary.springdatamongodb.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, Long> {
    Optional<List<Product>> more(float priceGT, float priceLT);
}
