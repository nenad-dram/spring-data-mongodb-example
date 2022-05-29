package com.endyary.springdatamongodb;

import com.endyary.springdatamongodb.model.Customer;
import com.endyary.springdatamongodb.model.Order;
import com.endyary.springdatamongodb.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = MongoConfig.class)
public class MongoTemplateTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void insertData() {

        List<Product> mockedProducts = MockDataProvider.getProductList();
        Customer custInstance = new Customer();
        custInstance.setId(2L);
        custInstance.setEmail("customer@mail.com");
        custInstance.setAddress("customer address");
        custInstance.setName("New Customer");
        custInstance.setPhone("9876543210");

        Query queryCustomer = new Query();
        queryCustomer.addCriteria(Criteria.where("_id").is(2L));
        if (!mongoTemplate.exists(queryCustomer, Customer.class)) {
            mongoTemplate.save(custInstance);
        }

        Query queryProduct = new Query();
        queryProduct.addCriteria(Criteria.where("_id").is(3L));
        if (!mongoTemplate.exists(queryProduct, Product.class)) {
            Product mockedProduct = mockedProducts.get(2);
            mockedProduct.setStatus(Product.Status.AVAILABLE);
            mongoTemplate.save(mockedProducts.get(2));
        }

        Query queryOrder = new Query();
        queryOrder.addCriteria(Criteria.where("_id").is(2L));
        if (!mongoTemplate.exists(queryOrder, Order.class)) {
            mockedProducts.remove(0);
            Order mockedOrder = MockDataProvider.getOrderWithItems(mockedProducts);
            mockedOrder.setId(2L);
            mockedOrder.setCustomer(custInstance);
            mongoTemplate.save(mockedOrder);
        }
    }

    @Test
    void findCustomerByUsernameOrEmail() {
        Criteria criteria = new Criteria();
        criteria.orOperator((Criteria.where("username").is("customer")),
                Criteria.where("email").is("customer@mail.com"));

        Query query = new Query(criteria);
        Customer foundCustomer = mongoTemplate.findOne(query, Customer.class);
        Assertions.assertNotNull(foundCustomer);
    }

    @Test
    void updateOrderStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(2L));
        Update update = new Update();
        update.set("status", Order.Status.SHIPPED);
        update.set("modifiedDate", LocalDateTime.now());
        mongoTemplate.updateFirst(query, update, Order.class);

        Order foundOrder = mongoTemplate.findById(2L, Order.class);
        Assertions.assertEquals(Order.Status.SHIPPED, foundOrder.getStatus());
    }

    @Test
    void findOrderByCustomer() {
        Query query = new Query();
        query.addCriteria(Criteria.where("customer._id").is(2L));

        Order foundOrder = mongoTemplate.findOne(query, Order.class);
        Assertions.assertEquals(2L, foundOrder.getId());
    }

    @Test
    void findOrdersByProductId() {
        Query query = new Query();
        query.addCriteria(Criteria.where("items").elemMatch(Criteria.where("product._id").is(3L)));

        Order foundOrder = mongoTemplate.findOne(query, Order.class);
        Assertions.assertEquals(2L, foundOrder.getId());
    }

    @Test
    void findProductsByPriceRange() {
        Query query = new Query();
        query.addCriteria(Criteria.where("price").gte(250).lte(350));

        List<Product> foundProducts = mongoTemplate.find(query, Product.class);
        Assertions.assertEquals(1, foundProducts.size());
    }
    
}
