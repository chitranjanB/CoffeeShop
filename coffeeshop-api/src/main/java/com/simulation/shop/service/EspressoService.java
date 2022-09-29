package com.simulation.shop.service;

import com.coffee.shared.entity.StepTransactionId;
import com.coffee.shared.entity.TransactionSequence;
import com.coffee.shared.model.Coffee;
import com.coffee.shared.model.Step;
import com.machine.espresso.EspressoMachine;
import com.simulation.shop.repository.TransactionSequenceRepository;
import com.simulation.shop.util.CoffeeUtility;
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
    private TransactionSequenceRepository repository;

    @Autowired
    private List<EspressoMachine> espressoMachines;

    @Autowired
    private CoffeeUtility utility;

    public Coffee makeEspresso(String transactionId) {
        //fetch beans from inventory
        EspressoMachine machine = getAvailableEspressoMachine(espressoMachines);

        TransactionSequence transactionSequence = repository.findById(transactionId).orElseThrow(() -> new IllegalStateException("Unable to find the orderId " + transactionId));
        Coffee coffee = machine.concentrate(transactionId, transactionSequence.getCustomerId());

        StepTransactionId stepTransactionId = new StepTransactionId(Step.MAKE_ESPRESSO, transactionId);
        //TODO fix
        utility.auditLog(stepTransactionId, coffee.getMachineName(), coffee.getCustomerId(), coffee.getStart());

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
