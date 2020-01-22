package com.example.demo.controllers;

import com.example.demo.SareetaApplicationTests;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    private User user;
    private Item item;
    private List<Item> items;
    private Cart cart;
    private UserOrder userOrder;

    @Before
    public void setup() {
        orderController = new OrderController();
        SareetaApplicationTests.TestUtils.injectObjects(orderController, "userRepository", userRepository);
        SareetaApplicationTests.TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        user = new User();
        user.setUsername("test");

        item = new Item();
        item.setId((long)1);
        item.setName("apple");
        item.setDescription("six");
        item.setPrice(BigDecimal.valueOf(100000));

        items = new ArrayList<>();
        items.add(item);

        cart = new Cart();
        cart.setId((long)1);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(1000000));
        user.setCart(cart);

        userOrder = new UserOrder();
        userOrder.setId((long)1);
        userOrder.setUser(user);
        userOrder.setTotal(BigDecimal.valueOf(1000000));
    }

    @Test
    public void submit_order1() {
        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(BigDecimal.valueOf(1000000), userOrder.getTotal());
    }

    @Test
    public void submit_order2() {
        ResponseEntity<UserOrder> response = orderController.submit("test 2");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void get_history1() {
        List<UserOrder> userOrderList = new ArrayList<>();
        userOrderList.add(userOrder);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
    }

    @Test
    public void get_history2() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
