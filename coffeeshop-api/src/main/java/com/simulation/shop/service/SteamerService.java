package com.simulation.shop.service;

import com.coffee.shared.entity.MilkStock;
import com.coffee.shared.entity.StepTransactionId;
import com.coffee.shared.entity.TransactionSequence;
import com.coffee.shared.exception.OutOfIngredientsException;
import com.coffee.shared.model.Status;
import com.coffee.shared.model.SteamedMilk;
import com.coffee.shared.model.Step;
import com.machine.steamer.SteamerMachine;
import com.simulation.shop.repository.MilkRepository;
import com.simulation.shop.repository.TransactionSequenceRepository;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private TransactionSequenceRepository transactionSequenceRepository;

    @Autowired
    private MilkRepository milkRepository;

    @Autowired
    private List<SteamerMachine> steamerMachines;

    @Autowired
    private CoffeeUtility utility;

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
            TransactionSequence order = transactionSequenceRepository.findById(transactionId).orElseThrow(() -> new IllegalStateException("Unable to find the orderId " + transactionId));
            steamedMilk = machine.steam(transactionId, order.getCustomerId(), stock.getMilk());

            StepTransactionId stepTransactionId = new StepTransactionId(Step.STEAM_MILK, transactionId);
            utility.auditLog(stepTransactionId, steamedMilk.getMachineName(), steamedMilk.getCustomerId(), steamedMilk.getStart());

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
                return isAvailable;
            }).mapToObj(i -> machines.get(i)).findAny().orElse(null);
        }
        return machine;
    }

}
