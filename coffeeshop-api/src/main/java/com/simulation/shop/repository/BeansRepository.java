package com.simulation.shop.repository;

import com.coffee.shared.entity.BeanStock;
import org.springframework.data.repository.CrudRepository;

public interface BeansRepository extends CrudRepository<BeanStock, String> {

}
