package com.example.demo.controllers;

import com.example.demo.SareetaApplicationTests;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private Item item;

    @Before
    public void setup() {
        itemController = new ItemController();
        SareetaApplicationTests.TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        item = new Item();
        item.setId((long) 1);
        item.setName("apple");
        item.setDescription("xxxxxxxxxxxx");
        item.setPrice(BigDecimal.valueOf(10000));
    }

    @Test
    public void get_item1() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Item> items1 = response.getBody();
        assertNotNull(items1);
    }

    @Test
    public void get_items2() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("apple");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
