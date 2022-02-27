package com.simulation.shop.service;

import com.coffee.shared.entity.BeanStock;
import com.coffee.shared.entity.OrdersTable;
import com.coffee.shared.entity.StepTransactionId;
import com.coffee.shared.model.Grounds;
import com.coffee.shared.model.Status;
import com.coffee.shared.model.Step;
import com.machine.grinder.GrinderMachine;
import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.repository.BeansRepository;
import com.simulation.shop.repository.OrdersRepository;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.StreamSupport;

@Service
public class GrindingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrindingService.class);

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private BeansRepository beanInventory;

    @Autowired
    private List<GrinderMachine> grinderMachines;

    @Autowired
    private CoffeeUtility utility;

    public Grounds grind(String transactionId) {
        Lock grinderLock = null;
        Grounds grounds = null;
        try {
            GrinderMachine machine = getAvailableGrinderMachine(grinderMachines);
            grinderLock = machine.getGrinderLock();

            //fetch beans from inventory
            //TODO issue multiple thread(customers) gets the same stock, as all stock are pending status
            BeanStock stock = StreamSupport.stream(
                            beanInventory.findAll().spliterator(), false)
                    .filter(b -> Status.PENDING.equals(b.getStatus()))
                    .filter(b -> b.getAssignedTo().equalsIgnoreCase(machine.getMachineName()))
                    .findAny().orElseThrow(() -> new OutOfIngredientsException("Beans Inventory is empty "+machine.getMachineName()));

            stock.setStatus(Status.COMPLETE);
            beanInventory.save(stock);

            //TODO grind using multithreading later
            OrdersTable order = ordersRepository.findById(transactionId)
                    .orElseThrow(() -> new IllegalStateException("Unable to find the orderId " + transactionId));
            grounds = machine.grind(transactionId, order.getCustomerId(), stock.getBeans());

            StepTransactionId stepTransactionId = new StepTransactionId(Step.GRIND_COFFEE, transactionId);
            utility.auditLog(stepTransactionId, grounds.getMachineName(), grounds.getCustomerId(), grounds.getStart());

            //consume the bean stock
            //TODO issue when multiple thread(customers) gets the same stock
            beanInventory.deleteById(stock.getStockId());

            LOGGER.debug("Grinding Coffee - Completed");
        } finally {
            grinderLock.unlock();
        }
        return grounds;
    }


    private GrinderMachine getAvailableGrinderMachine(List<GrinderMachine> machines) {
        GrinderMachine machine = null;

        while (machine == null) {
            machine = new Random().ints(0, 4).filter(i -> {
                boolean isAvailable = false;
                try {
                    isAvailable = machines.get(i).getGrinderLock().tryLock(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOGGER.error("Error while getting available grinding machine " + e.getLocalizedMessage(), e);
                }
                return isAvailable;
            }).mapToObj(i -> machines.get(i)).findAny().orElse(null);
        }
        return machine;
    }

}
