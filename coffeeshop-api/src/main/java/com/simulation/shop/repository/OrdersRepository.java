package com.simulation.shop.repository;

import com.coffee.shared.entity.OrdersTable;
import org.springframework.data.repository.CrudRepository;

public interface OrdersRepository extends CrudRepository<OrdersTable, String> {

}
