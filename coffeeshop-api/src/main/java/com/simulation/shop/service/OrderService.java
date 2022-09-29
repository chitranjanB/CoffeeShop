package com.simulation.shop.service;

import com.coffee.shared.model.CoffeeOrder;
import com.coffee.shared.model.Status;
import com.coffee.shared.model.Transaction;
import com.simulation.shop.CoffeeShopException;
import com.simulation.shop.repository.CoffeeOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    /**
     * This method uses ready-only Transaction to stream data from repository
     * Streaming Query Results - https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-streaming
     * @param status
     * @return
     */
    @Transactional(readOnly = true)
    public List<CoffeeOrder> findOrderByStatus(String status) {
        Status statusEnum = Status.valueOf(status);
        List<CoffeeOrder> orders = new ArrayList<>();

        try (Stream<com.coffee.shared.entity.CoffeeOrder> stream = coffeeOrderRepository.findByStatus(statusEnum)) {
            orders = stream.map(o -> {
                CoffeeOrder order = new CoffeeOrder();
                order.setOrderId(o.getOrderId());
                order.setCustomerId(o.getCustomerId());
                order.setStatus(o.getStatus());
                order.setOrderCreatedDate(o.getOrderCreatedDate());
                order.setOrderEndedDate(o.getOrderEndedDate());
                order.setTransactions(o.getTransactionSequences().stream().map(e -> {
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
        }

        return orders;
    }

    public List<String> fetchOrders() {
        Iterable<com.coffee.shared.entity.CoffeeOrder> all = coffeeOrderRepository.findAll();
        List<String> orderIds = Streamable.of(all).stream().map(a -> a.getOrderId()).collect(Collectors.toList());
        return orderIds;
    }

    /**
     * This method uses Future from CrudRepository to fetch orders by a given customerId
     *  Asynchronous Query Results - https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-async
     * @param customerId
     * @return
     */
    public List<CoffeeOrder> findOrderByCustomerId(String customerId) {

        Future<List<com.coffee.shared.entity.CoffeeOrder>> ordersByCustomerId = coffeeOrderRepository.findByCustomerId(customerId);
        List<com.coffee.shared.entity.CoffeeOrder> coffeeOrders = new ArrayList<>();
        try {
            coffeeOrders = ordersByCustomerId.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CoffeeOrder> collect = coffeeOrders.stream().map(c -> {
            CoffeeOrder o = new CoffeeOrder();
            o.setOrderId(c.getOrderId());
            o.setCustomerId(c.getCustomerId());
            o.setStatus(c.getStatus());
            o.setOrderCreatedDate(c.getOrderCreatedDate());
            o.setOrderEndedDate(c.getOrderEndedDate());
            o.setTransactions(c.getTransactionSequences().stream().map(e -> {
                Transaction t = new Transaction();
                t.setCustomerId(e.getCustomerId());
                t.setTransactionId(e.getTransactionId());
                t.setStatus(e.getStatus());
                t.setTimeStarted(e.getTimeStarted());
                t.setTimeEnded(e.getTimeEnded());
                return t;
            }).collect(Collectors.toList()));
            return o;
        }).collect(Collectors.toList());
        return collect;
    }

    public CoffeeOrder findOrderById(String orderId) {
        Optional<com.coffee.shared.entity.CoffeeOrder> optional = coffeeOrderRepository.findById(orderId);
        com.coffee.shared.entity.CoffeeOrder o = optional.orElseThrow(() -> new CoffeeShopException("No Order details available for order " + orderId));

        CoffeeOrder order = new CoffeeOrder();
        order.setOrderId(o.getOrderId());
        order.setCustomerId(o.getCustomerId());
        order.setStatus(o.getStatus());
        order.setOrderCreatedDate(o.getOrderCreatedDate());
        order.setOrderEndedDate(o.getOrderEndedDate());
        order.setTransactions(o.getTransactionSequences().stream().map(e -> {
            Transaction t = new Transaction();
            t.setCustomerId(e.getCustomerId());
            t.setTransactionId(e.getTransactionId());
            t.setStatus(e.getStatus());
            t.setTimeStarted(e.getTimeStarted());
            t.setTimeEnded(e.getTimeEnded());
            return t;
        }).collect(Collectors.toList()));

        return order;
    }
}
