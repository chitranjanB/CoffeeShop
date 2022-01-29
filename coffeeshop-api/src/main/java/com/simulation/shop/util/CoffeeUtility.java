package com.simulation.shop.util;

import com.simulation.shop.OutOfIngredientsException;
import com.simulation.shop.config.CoffeeShopPropConfig;
import com.simulation.shop.config.Constants;
import com.simulation.shop.config.Step;
import com.simulation.shop.entity.AuditLog;
import com.simulation.shop.entity.BeanStock;
import com.simulation.shop.entity.MilkStock;
import com.simulation.shop.entity.StepTransactionId;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.repository.BeansRepository;
import com.simulation.shop.repository.MilkRepository;
import com.simulation.shop.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Stream;
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

    private static final String COLLECT_APEX_METRIC_FORMAT = "%s::%s::%s::%s";

    private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);

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

        if (count > 100) {
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

    public boolean isShopClosed(int command) {
        return command == 0;
    }

    public int fetchRequiredMachines(int customers) {
        int machineLimit = config.getMachine().getLimit();
        return customers >= machineLimit ? machineLimit : customers;
    }

    public int fetchCustomerId(String metadata) {
        return Integer.parseInt(metadata.split("-")[1]);
    }

    /**
     * It calculates where to pick the beans/milk inventory
     *
     * @return
     */
    public int fetchMachineId(String machineName) {
        String[] split = machineName.split("-");
        int machineId = Integer.parseInt(split[split.length - 1]);
        return machineId;
    }

    public void loadupBeans(int machines, int productLimit) {
        Stream.iterate(1, i -> i + 1)
                .limit(machines)
                .forEach(machine -> loadMachine(productLimit, machine, Constants.BEANS_INVENTORY));
    }

    public void loadupMilk(int machines, int productLimit) {
        Stream.iterate(1, i -> i + 1)
                .limit(machines)
                .forEach(machine -> loadMachine(productLimit, machine, Constants.MILK_INVENTORY));
    }


    private void loadMachine(int beansLimit, Integer machineId, String inventoryName) {
        try (PrintWriter pw = new PrintWriter(String.format(inventoryName, machineId))) {
            Stream.iterate(1, i -> i + 1)
                    .limit(beansLimit)
                    .forEach(i -> pw.println("**************" + i));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void benchmarks() {
        if (isDebuggingEnabled()) {
            Map<String, List<String>> map = processStats();
            LOGGER.info("\n*******************BENCHMARK STATS (Apex Timeline chart)*******************");
            queue.clear();
        }
    }

    private Map<String, List<String>> processStats() {
        Map<String, List<String>> map = new HashMap<>();
        for (String element : queue) {
            String[] split = element.split("::");
            String key = split[0];
            String info = split[1] + "|" + split[2] + "|" + split[3];
            List<String> value = null;
            if (map.containsKey(key)) {
                value = map.get(key);
                value.add(info);
            } else {
                value = new ArrayList<String>();
                value.add(info);
                map.put(key, value);
            }

        }
        return map;
    }


    public Latte mix(Coffee coffee, SteamedMilk milk) {
        return new Latte(coffee, milk);
    }

    public String timeElapsed(Instant start, Instant finish) {
        String unit = "ms";
        long timeElapsed = Duration.between(start, finish).toMillis();
        return timeElapsed + unit;
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

    public boolean isDebuggingEnabled() {
        return Constants.IS_DEBUG_ENABLED;
    }

    public long stats(List<Long> samples) {
        long timePerLatte = 0;
        long maxBrewTime = 0;
        long totalBrewTime = totalBrewTime(samples);
        for (Long e : samples) {

            timePerLatte = Long.sum(timePerLatte, e);
            if (e > maxBrewTime) {
                maxBrewTime = e;
            }
        }
        timePerLatte = timePerLatte / samples.size();

        LOGGER.info(
                String.format(Constants.STATS_FORMAT, samples.size(), maxBrewTime, timePerLatte, totalBrewTime, samples));
        LOGGER.info("-----------------------------------------------------------------------");
        return timePerLatte;

    }

    private long totalBrewTime(List<Long> samples) {
        long totalBrewTime = 0;
        for (Long e : samples) {
            totalBrewTime = Long.sum(totalBrewTime, e);
        }
        return totalBrewTime;
    }

    public StringBuffer buildMetadata(int i) {
        return new StringBuffer(String.format(Constants.CUSTOMER_PREFIX, UUID.randomUUID()));
    }

    public String buildThreadMeta(StringBuffer oldMeta, String threadInfo) {
        return oldMeta + "--" + threadInfo;
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

    public int readNoOfOrders(PipedInputStream orderInputStream) {
        int noOfOrders = 0;
        try {
            noOfOrders = Integer.valueOf(readLine(orderInputStream));
        } catch (Exception e) {
            LOGGER.error(
                    "Exception occured : " + e.getMessage(), e);
        }
        return noOfOrders;
    }

    public void writeNoOfOrders(PipedOutputStream orderProducer, int noOfOrders) {
        String orders = String.valueOf(noOfOrders);
        int length = orders.length();
        for (int i = 3; i > length; i--) {
            orders = "0" + orders;
        }
        writeLine(orderProducer, orders);
    }

    public String readLine(PipedInputStream orderInputStream) {
        String data = "";
        try {
            for (int i = 0; i < 3; i++) {
                data = data + String.valueOf((char) orderInputStream.read());
            }

        } catch (IOException e) {
            LOGGER.error(
                    "Exception occured : " + e.getMessage(), e);
        }
        return data;
    }

    public void writeLine(PipedOutputStream orderProducer, String orders) {
        try {
            orderProducer.write(orders.getBytes(), 0, orders.length());
        } catch (IOException e) {
            LOGGER.error(
                    "Exception occured : " + e.getMessage(), e);
        }
    }

    public StringBuffer addMachineMetadata(StringBuffer metadata, String machineName) {
        return metadata.append(":").append(machineName);
    }
}
