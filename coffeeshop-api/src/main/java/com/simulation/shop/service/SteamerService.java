package com.simulation.shop.service;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.entity.MilkStock;
import com.simulation.shop.entity.OrdersTable;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Status;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.repository.MilkRepository;
import com.simulation.shop.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Service
public class SteamerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SteamerService.class);

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MilkRepository milkRepository;

    @Autowired
    private List<SteamerMachine> steamerMachines;

    public SteamedMilk steam(String transactionId) {
        //fetch beans from inventory
        //TODO many customer gets the same stock, all stock are pending at beginning
        MilkStock stock = StreamSupport.stream(
                milkRepository.findAll().spliterator(), false)
                .filter(m -> Status.PENDING.equals(m.getStatus()))
                .findAny().orElseThrow(() -> new OutOfIngredientsException("Milk Inventory is empty"));
        SteamerMachine machine = getAvailableSteamerMachine(steamerMachines);

        stock.setStatus(Status.COMPLETE);
        milkRepository.save(stock);

        //TODO grind using multithreading later
        OrdersTable order = ordersRepository.findById(transactionId).get();
        SteamedMilk grounds = machine.steam(transactionId, order.getCustomerId(), stock.getMilk());

        //consume the milk stock
        //TODO many customer gets the same milk stock, and deleting gives error
        milkRepository.delete(stock);

        LOGGER.debug("Steamed milk - Completed");
        return grounds;
    }


    private SteamerMachine getAvailableSteamerMachine(List<SteamerMachine> machines) {
        SteamerMachine machine = null;
        // block until lock available
        while (machine == null) {
            Optional<SteamerMachine> optional = machines.stream().filter(m -> {
                boolean isAvailable = false;
                try {
                    isAvailable = m.getSteamerLock().tryLock(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOGGER.error("Error while getting available steamer machine " + e.getLocalizedMessage(), e);
                }
                return isAvailable;
            }).findAny();
            machine = optional.orElse(null);
        }
        return machine;
    }

}
