package com.simulation.shop.service;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.entity.MilkStock;
import com.simulation.shop.entity.OrdersTable;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Grounds;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
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

        Lock steamerLock = null;
        SteamedMilk steamedMilk = null;
        try {
            SteamerMachine machine = getAvailableSteamerMachine(steamerMachines);
            steamerLock = machine.getSteamerLock();
            //fetch beans from inventory
            //TODO many customer gets the same stock, all stock are pending at beginning
            MilkStock stock = StreamSupport.stream(
                            milkRepository.findAll().spliterator(), false)
                    .filter(m -> Status.PENDING.equals(m.getStatus()))
                    .filter(b->b.getAssignedTo().equalsIgnoreCase(machine.getMachineName()))
                    .findAny().orElseThrow(() -> new OutOfIngredientsException("Milk Inventory is empty "+machine.getMachineName()));

            stock.setStatus(Status.COMPLETE);
            milkRepository.save(stock);

            //TODO grind using multithreading later
            OrdersTable order = ordersRepository.findById(transactionId).get();
            steamedMilk = machine.steam(transactionId, order.getCustomerId(), stock.getMilk());

            //consume the milk stock
            //TODO many customer gets the same milk stock, and deleting gives error
            milkRepository.delete(stock);
            LOGGER.debug("Steamed milk - Completed");
        }
        finally{
            steamerLock.unlock();
        }

        return steamedMilk;
    }


    private SteamerMachine getAvailableSteamerMachine(List<SteamerMachine> machines) {
        SteamerMachine machine = null;
        // block until lock available

        while (machine == null) {
            machine = new Random().ints(0, 4).filter(i -> {
                boolean isAvailable = false;
                try {
                    isAvailable = machines.get(i).getSteamerLock().tryLock(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOGGER.error("Error while getting available grinding machine " + e.getLocalizedMessage(), e);
                }
                System.out.println("value " + i + " " + isAvailable);
                return isAvailable;
            }).mapToObj(i -> machines.get(i)).findAny().orElse(null);
        }
        return machine;
    }

}
