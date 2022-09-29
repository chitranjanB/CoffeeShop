package com.simulation.shop.controller;

import com.coffee.shared.model.CoffeeOrder;
import com.simulation.shop.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService service;

    @GetMapping
    public List<CoffeeOrder> findOrderByStatus(@RequestParam String status) {
        return service.findOrderByStatus(status);
    }

    @GetMapping("/order")
    public CoffeeOrder findOrderById(@RequestParam String orderId) {
        return service.findOrderById(orderId);
    }

    @GetMapping("/customer")
    public List<CoffeeOrder> findOrderByCustomerId(@RequestParam String customerId) {
        return service.findOrderByCustomerId(customerId);
    }

    @GetMapping("/all/ids")
    public List<String> fetchOrderIds() {
        return service.fetchOrders();
    }


}
