package com.endyary.springdatamongodb.repository;

import com.endyary.springdatamongodb.model.Customer;
import com.endyary.springdatamongodb.model.Order;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, Long> {

    Optional<List<Order>> findByStatus(Order.Status status);

    Optional<List<Order>> findByCustomer(Customer customer);

    @Query("{ 'items' : {$elemMatch: {'product._id' : ?0}}}")
    Optional<List<Order>> findByProductId(Long productId);

    @Aggregation(pipeline = {"{$match: {_id: ?0}}",
            """
                    {'$project': {'_id':0,
                    'total': {
                          '$sum': {
                            '$map': {
                              'input': '$items',
                              'in': { '$multiply': ['$$this.product.price', '$$this.quantity'] }
                            }
                          }
                        }
                    }
                    }"""})
    Integer getTotalPriceById(Long orderId);
}
