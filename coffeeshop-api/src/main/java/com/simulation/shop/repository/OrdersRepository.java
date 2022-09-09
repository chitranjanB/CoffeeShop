package com.simulation.shop.repository;

import com.coffee.shared.entity.OrdersTable;
import com.coffee.shared.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrdersRepository extends CrudRepository<OrdersTable, String> {

    public List<OrdersTable> findByStatus(Status status);
}
