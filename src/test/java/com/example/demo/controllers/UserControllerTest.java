package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user1() throws Exception {
        when(bCryptPasswordEncoder.encode("123456789")).thenReturn("hashedPassword");

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("123456789");
        userRequest.setConfirmPassword("123456789");

        ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void create_user2() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("123456789");
        userRequest.setConfirmPassword("xxxxxxxxxx");

        ResponseEntity<User> response = userController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void find_by_username1() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("123456789");

        when(userRepository.findByUsername("test")).thenReturn(user);

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("123456789");
        userRequest.setConfirmPassword("123456789");

        userController.createUser(userRequest);
        ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", user.getUsername());
    }

    @Test
    public void find_by_username2() {
        ResponseEntity<User> user = userController.findByUserName("yaoi");
        assertNotNull(user);
        assertEquals(HttpStatus.NOT_FOUND, user.getStatusCode());
    }

    @Test
    public void find_by_id1() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("123456789");
        when(userRepository.findById((long) 1)).thenReturn(java.util.Optional.of(user));

        ResponseEntity<User> response = userController.findById((long)1);

        assertNotNull(response);
        assertEquals("test", response.getBody().getUsername());
    }

}
