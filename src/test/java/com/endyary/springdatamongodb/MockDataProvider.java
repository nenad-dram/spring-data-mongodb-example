package com.endyary.springdatamongodb;

import com.endyary.springdatamongodb.model.Customer;
import com.endyary.springdatamongodb.model.Order;
import com.endyary.springdatamongodb.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockDataProvider {

    public static Customer getCustomer() {
        Customer custInstance = new Customer();
        custInstance.setId(1L);
        custInstance.setEmail("test@mail.com");
        custInstance.setAddress("address value");
        custInstance.setName("John Doe");
        custInstance.setPhone("0123456789");

        return custInstance;
    }

    public static List<Product> getProductList() {
        List<Product> prodList = new ArrayList<Product>();

        Product prod1 = new Product();
        prod1.setId(1L);
        prod1.setName("Product One");
        prod1.setDescription("Description for Product One");
        prod1.setPrice(100F);
        prod1.setStatus(Product.Status.AVAILABLE);

        Product prod2 = new Product();
        prod2.setId(2L);
        prod2.setName("Product Two");
        prod2.setDescription("Description for Product Two");
        prod2.setPrice(200F);
        prod2.setStatus(Product.Status.AVAILABLE);

        Product prod3 = new Product();
        prod3.setId(3L);
        prod3.setName("Product Three");
        prod3.setDescription("Description for Product Three");
        prod3.setPrice(300F);
        prod3.setStatus(Product.Status.UNAVAILABLE);

        prodList.add(prod1);
        prodList.add(prod2);
        prodList.add(prod3);

        return prodList;
    }

    public static Order getOrderWithItems(List<Product> productList) {
        Order order = new Order();
        order.setId(1L);
        order.setCreatedDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setItems(new ArrayList<Order.Item>());

        Order.Item orderItem1 = new Order.Item();
        orderItem1.setProduct(productList.get(0));
        orderItem1.setQuantity(2);
        order.getItems().add(orderItem1);

        Order.Item orderItem2 = new Order.Item();
        orderItem2.setProduct(productList.get(1));
        orderItem2.setQuantity(5);
        order.getItems().add(orderItem2);

        return order;
    }
}
