package com.simulation.shop.service;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.entity.BeanStock;
import com.simulation.shop.entity.OrdersTable;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.repository.BeansRepository;
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

    public Grounds grind(String transactionId) {
        //fetch beans from inventory
        BeanStock stock = StreamSupport.stream(
                beanInventory.findAll().spliterator(), false)
                .findAny().orElseThrow(() -> new OutOfIngredientsException("Beans Inventory is empty"));
        GrinderMachine machine = getAvailableGrinderMachine(grinderMachines);

        //TODO grind using multithreading later
        OrdersTable order = ordersRepository.findById(transactionId).get();
        Grounds grounds = machine.grind(transactionId, order.getCustomerId(), stock.getBeans());
        //consume the bean stock
        beanInventory.delete(stock);

        LOGGER.debug("Grinding Coffee - Completed");
        return grounds;
    }


    private GrinderMachine getAvailableGrinderMachine(List<GrinderMachine> machines) {
        GrinderMachine machine = null;
        // block until lock available
        while (machine == null) {
            Optional<GrinderMachine> optional = machines.stream().filter(m -> {
                boolean isAvailable = false;
                try {
                    isAvailable = m.getGrinderLock().tryLock(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOGGER.error("Error while getting available grinding machine " + e.getLocalizedMessage(), e);
                }
                return isAvailable;
            }).findAny();
            machine = optional.get();
        }
        return machine;
    }

}
