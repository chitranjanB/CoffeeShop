package com.simulation.shop.util;

import com.simulation.shop.config.CoffeeShopPropConfig;
import com.simulation.shop.config.Constants;
import com.simulation.shop.entity.AuditLog;
import com.simulation.shop.entity.BeanStock;
import com.simulation.shop.entity.MilkStock;
import com.simulation.shop.entity.StepTransactionId;
import com.simulation.shop.model.Status;
import com.simulation.shop.repository.BeansRepository;
import com.simulation.shop.repository.MilkRepository;
import com.simulation.shop.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

@Component
public class CoffeeUtility {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private BeansRepository beansRepository;

    @Autowired
    private MilkRepository milkRepository;

    @Autowired
    private CoffeeShopPropConfig config;

    private static final Logger LOGGER = LoggerFactory.getLogger(CoffeeUtility.class);

    public void auditLog(StepTransactionId stepTransactionId, String machineName, String customerId, Instant start) {
        AuditLog auditLog = buildAuditLog(stepTransactionId, machineName, customerId, start);
        auditLogService.audit(auditLog);
    }

    private AuditLog buildAuditLog(StepTransactionId stepTransactionId, String machineName, String customerId, Instant start) {
        AuditLog auditLog = new AuditLog(stepTransactionId);
        auditLog.setCustomerId(customerId);
        auditLog.setMachineName(machineName);
        auditLog.setThreadName(Thread.currentThread().getName());
        Instant end = Instant.now();
        auditLog.setTimeElapsed(Duration.between(start, end).toMillis());
        auditLog.setTimeStarted(Date.from(start));
        auditLog.setTimeEnded(Date.from(end));
        return auditLog;
    }

    public void loadInventory() {
        int limit = config.getInventory().getBeans().getLimit();
        long count = StreamSupport.stream(
                beansRepository.findAll().spliterator(), false)
                .count();

        if (count >= limit) {
            LOGGER.debug("Skipping inventory load, Count :" + count);
        } else {
            LOGGER.info("Loading inventory - current count :" + count);
            for (int i = 1; i <= limit; i++) {
                //TODO change this to bulk insert
                beansRepository.save(generateBeanStock(1));
                beansRepository.save(generateBeanStock(2));
                beansRepository.save(generateBeanStock(3));
                beansRepository.save(generateBeanStock(4));

                milkRepository.save(generateMilkStock(1));
                milkRepository.save(generateMilkStock(2));
                milkRepository.save(generateMilkStock(3));
                milkRepository.save(generateMilkStock(4));
            }
        }
    }

    private BeanStock generateBeanStock(int machineId) {
        String stockId = UUID.randomUUID().toString();
        BeanStock beanStock = new BeanStock();
        beanStock.setStockId("bean-" + stockId);
        beanStock.setBeans("**********");
        beanStock.setStatus(Status.PENDING);
        beanStock.setAssignedTo(String.format(Constants.MACHINEID_FORMAT, Constants.GRINDER_PREFIX, machineId));
        return beanStock;
    }

    private MilkStock generateMilkStock(int machineId) {
        String stockId = UUID.randomUUID().toString();
        MilkStock milkStock = new MilkStock();
        milkStock.setStockId("milk-" + stockId);
        milkStock.setMilk("----------");
        milkStock.setStatus(Status.PENDING);
        milkStock.setAssignedTo(String.format(Constants.MACHINEID_FORMAT, Constants.STEAMER_PREFIX, machineId));
        return milkStock;
    }

    public int buildStepTimeWithJitter() {
       // Random random = new Random();
        int jitter = Constants.JITTER;
       // jitter = jitter > 0 ? random.nextInt(jitter) : jitter;
        return jitter;
    }

    /**
     * Handles consumers which throws checked exception
     */
    public <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
            ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    LOGGER.error(
                            "Exception occured : " + exCast.getMessage(), exCast);
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

}
