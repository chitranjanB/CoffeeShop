package com.simulation.shop.service;

import com.simulation.shop.request.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProcessService {

    public boolean isBeanInventoryAvailable() {
        //TODO
        return true;
    }

    public void updateDBInventoryBeans() {

    }

    public void auditLogs() {

    }

    public String buildTransactionId() {
        return UUID.randomUUID().toString();
    }

    public List<Order> queueOrder(OrderRequest request) {
        //TODO insert rows = no.of orders
        int orders = request.getOrders();
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < orders; i++) {
            //TODO put orders in db orderQueue
            Order order = new Order(buildTransactionId(), request.getCustomerId(), false);
            //TODO entity OrderQueue(transactionId, customerId, completed)
            orderList.add(order);
        }
        return orderList;
    }
}
