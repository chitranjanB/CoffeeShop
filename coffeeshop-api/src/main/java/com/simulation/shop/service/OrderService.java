package com.simulation.shop.service;

import com.coffee.shared.model.CoffeeOrder;
import com.coffee.shared.model.Status;
import com.coffee.shared.model.Transaction;
import com.simulation.shop.repository.CoffeeOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public List<CoffeeOrder> findOrderByStatus(String status) {
        Status statusEnum = Status.valueOf(status);
        List<com.coffee.shared.entity.CoffeeOrder> coffeeOrders = coffeeOrderRepository.findByStatus(statusEnum);
        List<CoffeeOrder> orders = coffeeOrders.stream().map(o -> {
            CoffeeOrder order = new CoffeeOrder();
            order.setOrderId(o.getOrderId());
            order.setCustomerId(o.getCustomerId());
            order.setStatus(o.getStatus());
            order.setOrderCreatedDate(o.getOrderCreatedDate());
            order.setOrderEndedDate(o.getOrderEndedDate());
            order.setTransactions(o.getTransactionSequences().stream().map(e->{
                Transaction t = new Transaction();
                t.setCustomerId(e.getCustomerId());
                t.setTransactionId(e.getTransactionId());
                t.setStatus(e.getStatus());
                t.setTimeStarted(e.getTimeStarted());
                t.setTimeEnded(e.getTimeEnded());
                return t;
            }).collect(Collectors.toList()));
            return order;
        }).collect(Collectors.toList());
        return orders;
    }

    public List<String> fetchOrders() {
        Iterable<com.coffee.shared.entity.CoffeeOrder> all = coffeeOrderRepository.findAll();
        List<String> orderIds = Streamable.of(all).stream().map(a -> a.getOrderId()).collect(Collectors.toList());
        return orderIds;
    }

}
