package com.simulation.shop.service;

import com.simulation.shop.entity.OrdersTable;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class EspressoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EspressoService.class);

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private List<EspressoMachine> espressoMachines;

    public Coffee makeEspresso(String transactionId) {
        //fetch beans from inventory
        EspressoMachine machine = getAvailableEspressoMachine(espressoMachines);

        //TODO grind using multithreading later
        OrdersTable order = ordersRepository.findById(transactionId).get();
        Coffee coffee = machine.concentrate(transactionId, order.getCustomerId());

        LOGGER.debug("Espresso brew - Completed");
        return coffee;
    }

    private EspressoMachine getAvailableEspressoMachine(List<EspressoMachine> machines) {
        EspressoMachine machine = null;
        // block until lock available
        while (machine == null) {
            Optional<EspressoMachine> optional = machines.stream().filter(m -> {
                boolean isAvailable = false;
                try {
                    isAvailable = m.getEspressoLock().tryLock(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    LOGGER.error("Error while getting available espresso machine " + e.getLocalizedMessage(), e);
                }
                return isAvailable;
            }).findAny();
            machine = optional.orElse(null);
        }
        return machine;
    }
}
