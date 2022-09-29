package com.simulation.shop.repository;

import com.coffee.shared.entity.CoffeeOrder;
import com.coffee.shared.entity.TransactionSequence;
import com.coffee.shared.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoffeeOrderRepository extends CrudRepository<CoffeeOrder, String> {
    //TODO find usage of this
    public List<CoffeeOrder> findByStatus(Status status);
    public List<TransactionSequence> findByTransactionSequences(String transactionSequence);
}
