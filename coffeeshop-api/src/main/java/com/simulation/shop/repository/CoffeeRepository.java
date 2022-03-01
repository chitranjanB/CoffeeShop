package com.simulation.shop.repository;


import com.coffee.shared.entity.CoffeeEntity;
import org.springframework.data.repository.CrudRepository;

public interface CoffeeRepository extends CrudRepository<CoffeeEntity, String> {
}
