package com.endyary.springdatamongodb;

import com.endyary.springdatamongodb.model.Customer;
import com.endyary.springdatamongodb.model.Order;
import com.endyary.springdatamongodb.model.Product;
import com.endyary.springdatamongodb.repository.CustomerRepository;
import com.endyary.springdatamongodb.repository.OrderRepository;
import com.endyary.springdatamongodb.repository.ProductRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = MongoConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShopDbTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @org.junit.jupiter.api.Order(1)
    void saveProducts() {
        List<Product> expectedProducts = MockDataProvider.getProductList();
        List<Product> actualProducts = productRepository.saveAll(expectedProducts);
        Assertions.assertEquals(expectedProducts.size(), actualProducts.size());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void saveCustomer() {
        Customer expectedCustomer = MockDataProvider.getCustomer();
        Customer actualCustomer = customerRepository.save(expectedCustomer);
        Assertions.assertEquals(expectedCustomer.getEmail(), actualCustomer.getEmail());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void saveOrder() {
        Order expectedOrder = MockDataProvider.getOrderWithItems(MockDataProvider.getProductList());
        expectedOrder.setCustomer(MockDataProvider.getCustomer());
        Order actualOrder = orderRepository.save(expectedOrder);
        Assertions.assertEquals(expectedOrder.getItems().size(), actualOrder.getItems().size());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void findAllOrders() {
        Order expectedOrder = MockDataProvider.getOrderWithItems(MockDataProvider.getProductList());
        Order actualOrder = orderRepository.findAll().get(0);
        Assertions.assertEquals(expectedOrder.getItems().get(0).getProduct().getId(),
                actualOrder.getItems().get(0).getProduct().getId());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void deleteProduct() {
        productRepository.deleteById(3L);
        List<Product> allProducts = productRepository.findAll();
        Assertions.assertEquals(2, allProducts.size());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void updateOrderStatus() {
        Order existingOrder = orderRepository.findById(1L).get();
        existingOrder.setStatus(Order.Status.SHIPPED);
        existingOrder.setModifiedDate(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(existingOrder);

        SoftAssertions orderAssert = new SoftAssertions();
        orderAssert.assertThat(Order.Status.SHIPPED).isEqualTo(updatedOrder.getStatus());
        orderAssert.assertThat(updatedOrder.getModifiedDate()).isNotNull();
        orderAssert.assertAll();
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void findCustomerByUsernameOrEmail() {
        Optional<Customer> foundCustomer = customerRepository.findByUsernameOrEmail("test", "test@mail.com");
        Assertions.assertTrue(foundCustomer.isPresent());
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void findOrderByStatus() {
        Optional<List<Order>> foundOrders = orderRepository.findByStatus(Order.Status.SHIPPED);

        SoftAssertions orderAssert = new SoftAssertions();
        orderAssert.assertThat(foundOrders.isPresent()).isTrue();
        orderAssert.assertThat(foundOrders.get().size()).isEqualTo(1);
        orderAssert.assertAll();
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    void findOrderByCustomer() {
        Optional<List<Order>> foundOrders = orderRepository.findByCustomer(MockDataProvider.getCustomer());

        SoftAssertions orderAssert = new SoftAssertions();
        orderAssert.assertThat(foundOrders.isPresent()).isTrue();
        orderAssert.assertThat(foundOrders.get().size()).isEqualTo(1);
        orderAssert.assertAll();
    }
}
