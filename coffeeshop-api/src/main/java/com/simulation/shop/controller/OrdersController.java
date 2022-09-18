package com.simulation.shop.controller;

import com.coffee.shared.entity.OrdersTable;
import com.coffee.shared.model.Status;
import com.simulation.shop.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrdersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrdersService service;

    @GetMapping
    public List<String> fetchOrderIds() {
        return service.fetchOrders();
    }

}
