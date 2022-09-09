package com.simulation.shop.service;

import com.coffee.shared.model.Status;
import com.simulation.shop.repository.BeansRepository;
import com.simulation.shop.repository.CoffeeRepository;
import com.simulation.shop.repository.MilkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private BeansRepository beansRepository;

    @Autowired
    private MilkRepository milkRepository;

    public long fetchBeansStock() {
        long beansStockCount = beansRepository.count();
        return beansStockCount;
    }

    public long fetchMilkStock() {
        long milkStockCount = milkRepository.count();
        return milkStockCount;
    }

}
