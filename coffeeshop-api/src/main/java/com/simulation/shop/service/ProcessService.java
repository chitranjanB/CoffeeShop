package com.simulation.shop.service;

import com.simulation.shop.CoffeeShopException;
import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.entity.OrdersTable;
import com.simulation.shop.repository.OrdersRepository;
import com.simulation.shop.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProcessService {

    @Autowired
    private OrdersRepository repository;

    public List<String> queueOrder(OrderRequest request) {
        int orders = request.getOrders();
        List<String> transactionList = new ArrayList<>();
        for (int i = 0; i < orders; i++) {
            OrdersTable ordersTable = new OrdersTable();
            ordersTable.setCustomerId(request.getCustomerId());
            String transactionId = UUID.randomUUID().toString();
            transactionList.add(transactionId);
            ordersTable.setTransactionId(transactionId);
            ordersTable.setCompleted(false);

            repository.save(ordersTable);
        }
        return transactionList;
    }

    public void archiveOrder(String transactionId) {
        Optional<OrdersTable> optional = repository.findById(transactionId);
        OrdersTable order = optional.orElseThrow(() -> new CoffeeShopException("No order details available"));
        order.setCompleted(true);
        repository.save(order);
    }
}
