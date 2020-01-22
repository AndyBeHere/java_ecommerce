package com.example.demo.controllers;

import com.example.demo.SareetaApplicationTests;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private User user;
    private Cart cart;
    private Cart userCart;
    private Item item;

    private ModifyCartRequest request;

    private Optional<Item> optionalItem;

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();

        SareetaApplicationTests.TestUtils.injectObjects(cartController, "userRepository", userRepository);
        SareetaApplicationTests.TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        SareetaApplicationTests.TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        user = new User();
        cart = new Cart();
        userCart = new Cart();
        item = new Item();
        request = new ModifyCartRequest();

        userCart.setId((long) 1);

        user.setId(1);
        user.setUsername("test");
        user.setPassword("1234546789");
        user.setCart(userCart);

        item.setDescription("xxxxxxx");
        item.setId((long) (1));
        item.setName("apple");
        item.setPrice(BigDecimal.valueOf(100000));

    }

    @Test
    public void add_to_cart1() throws Exception {

        Optional<Item> optionalItem = Optional.of(item);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(Long.valueOf(1))).thenReturn(optionalItem);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void add_to_cart2() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1100);
        request.setUsername("yao");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}
