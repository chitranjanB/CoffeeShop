package com.simulation.shop.repository;

import com.simulation.shop.service.OrdersTable;
import org.springframework.data.repository.CrudRepository;

public interface ProcessRepository extends CrudRepository<OrdersTable, String> {

}
