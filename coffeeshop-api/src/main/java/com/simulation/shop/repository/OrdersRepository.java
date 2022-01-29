package com.simulation.shop.repository;

import com.simulation.shop.entity.OrdersTable;
import org.springframework.data.repository.CrudRepository;

public interface OrdersRepository extends CrudRepository<OrdersTable, String> {

}
