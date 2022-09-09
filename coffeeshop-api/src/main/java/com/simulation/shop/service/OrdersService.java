package com.simulation.shop.service;

import com.coffee.shared.entity.OrdersTable;
import com.coffee.shared.model.Status;
import com.simulation.shop.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    public List<OrdersTable> findOrderByStatus(String status) {
        Status statusEnum = Status.valueOf(status);
        List<OrdersTable> orders = ordersRepository.findByStatus(statusEnum);
        return orders;
    }

}
