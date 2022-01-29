package com.simulation.shop.repository;

import com.simulation.shop.entity.MilkStock;
import org.springframework.data.repository.CrudRepository;

public interface MilkRepository extends CrudRepository<MilkStock, String> {

}