package com.simulation.shop.util;

import com.simulation.shop.config.CoffeeShopPropConfig;
import com.simulation.shop.config.Constants;
import com.simulation.shop.entity.AuditLog;
import com.simulation.shop.entity.BeanStock;
import com.simulation.shop.entity.MilkStock;
import com.simulation.shop.entity.StepTransactionId;
import com.simulation.shop.repository.BeansRepository;
import com.simulation.shop.repository.MilkRepository;
import com.simulation.shop.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
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
        return auditLog;
    }

    public void loadInventory() {
        long count = StreamSupport.stream(
                beansRepository.findAll().spliterator(), false)
                .count();

        if (count >= 100) {
            LOGGER.debug("Skipping inventory load, Count :" + count);
        } else {
            LOGGER.info("Loading inventory - current count :" + count);
            for (int i = 1; i < 11; i++) {
                String stockId = UUID.randomUUID().toString();

                BeanStock beanStock = new BeanStock();
                beanStock.setStockId("bean-" + stockId);
                beanStock.setBeans("**********");

                MilkStock milkStock = new MilkStock();
                milkStock.setStockId("milk-" + stockId);
                milkStock.setMilk("----------");

                beansRepository.save(beanStock);
                milkRepository.save(milkStock);
            }
        }
    }

    public int buildStepTimeWithJitter() {
        Random random = new Random();
        int result = 0;
        int jitter = Constants.JITTER;
        jitter = jitter > 0 ? random.nextInt(jitter) : jitter;

        if (random.nextBoolean()) {
            result = config.getStep().getProcessing() + jitter;
        } else {
            result = config.getStep().getProcessing() - jitter;
        }
        return result;
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
