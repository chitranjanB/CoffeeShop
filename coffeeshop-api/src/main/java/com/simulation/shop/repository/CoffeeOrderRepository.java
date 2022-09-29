package com.simulation.shop.repository;

import com.coffee.shared.entity.CoffeeOrder;
import com.coffee.shared.entity.TransactionSequence;
import com.coffee.shared.model.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface CoffeeOrderRepository extends CrudRepository<CoffeeOrder, String> {
    // Streaming Query Results - https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-streaming
    public Stream<CoffeeOrder> findByStatus(Status status);

    // Asynchronous Query Results - https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-async
    @Async
    public Future<List<CoffeeOrder>> findByCustomerId(String customerId);
}
