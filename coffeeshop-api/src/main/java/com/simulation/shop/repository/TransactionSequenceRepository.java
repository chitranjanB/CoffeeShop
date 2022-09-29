package com.simulation.shop.repository;

import com.coffee.shared.entity.CoffeeOrder;
import com.coffee.shared.entity.TransactionSequence;
import com.coffee.shared.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionSequenceRepository extends CrudRepository<TransactionSequence, String> {
    public List<TransactionSequence> findByStatus(Status status);
    public List<TransactionSequence> findByOrder(CoffeeOrder order);
}
