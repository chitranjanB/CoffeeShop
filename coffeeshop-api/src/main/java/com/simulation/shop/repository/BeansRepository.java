package com.simulation.shop.repository;

import com.simulation.shop.entity.BeanStock;
import org.springframework.data.repository.CrudRepository;

public interface GrinderRepository extends CrudRepository<BeanStock, String> {

}
