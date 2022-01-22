package com.simulation.shop;

import com.simulation.shop.config.CoffeeShopConfig;
import com.simulation.shop.config.CoffeeShopRuntime;
import com.simulation.shop.config.Constants;
import com.simulation.shop.config.Step;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.util.CoffeeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class CoffeeShop {

    @Autowired
    private List<GrinderMachine> grinderMachines;

    @Autowired
    private List<EspressoMachine> espressoMachines;

    @Autowired
    private List<SteamerMachine> steamerMachines;

    @Autowired
    private CoffeeShopConfig config;

    @Autowired
    private CoffeeUtility utility;

    /**
     * The CoffeeShop on-start keeps polling piped input stream for continuous orders
     * The application stops, if 0 is encountered in input stream
     *
     * @param pip
     */
    public void start(PipedInputStream pip) throws Exception {
        CoffeeShopRuntime.getInstance().setShopOpenTimestamp(LocalTime.now());

        System.out.println("-----------------------COFFEE SHOP STARTED-----------------------------");
        ExecutorService executor = Executors.newFixedThreadPool(utility.fetchRequiredMachines(Constants.CUSTOMERS));

        int orders = 0;
        while ((orders = utility.readNoOfOrders(pip)) != 0) {
            service(orders, executor);
        }

        long millis = Duration.between(CoffeeShopRuntime.getInstance().getShopOpenTimestamp(), LocalTime.now()).toMillis();
        System.out.println("---------------------COFFEE SHOP CLOSED (" + millis + "ms) --------------------------");
        utility.benchmarks();

        executor.shutdown();
    }

    private boolean service(int orders, ExecutorService executor) {
        if (orders == -1) {
            //Close the shop
            return true;
        } else {
            List<CompletableFuture<Coffee>> futures = Stream.iterate(1, i -> i + 1)
                    .limit(orders)
                    .map(i -> {
                        StringBuffer metadata = utility.buildMetadata(i);
                        return CompletableFuture
                                .supplyAsync(() -> grindCoffee(grinderMachines, metadata), executor)
                                .thenApply(grounds -> makeEspresso(espressoMachines, grounds, metadata))
                                .thenApply(espresso -> steamMilk(steamerMachines, metadata))
                                .thenApply(steamedMilk -> mix(metadata));
                    })
                    .collect(Collectors.toList());

            // Wait for Async threads to complete
            futures.stream()
                    .forEach(utility.handlingConsumerWrapper(future -> future.get(),
                            Exception.class));
        }
        return false;
    }

    private Grounds grindCoffee(List<GrinderMachine> grinderMachines, StringBuffer metadata) {
        Instant start = Instant.now();
        GrinderMachine machine = getAvailableGrinderMachine(grinderMachines);
        metadata = utility.addMachineMetadata(metadata, machine.getMachineName());
        Grounds grounds = machine.grind(metadata);
        Instant end = Instant.now();
        utility.collectApexMetric(utility.buildThreadMeta(metadata, Thread.currentThread().getName()),
                Step.GRIND_COFFEE, start, end);
        return grounds;
    }

    private Coffee makeEspresso(List<EspressoMachine> espressoMachines, Grounds grounds, StringBuffer metadata) {
        Instant start = Instant.now();
        EspressoMachine machine = getAvailableEspressoMachine(espressoMachines);
        metadata = utility.addMachineMetadata(metadata, machine.getMachineName());
        Coffee coffee = machine.concentrate(metadata);
        Instant end = Instant.now();
        utility.collectApexMetric(utility.buildThreadMeta(metadata, Thread.currentThread().getName()),
                Step.MAKE_ESPRESSO, start, end);
        return coffee;
    }

    private SteamedMilk steamMilk(List<SteamerMachine> steamerMachines, StringBuffer metadata) {
        Instant start = Instant.now();
        SteamerMachine machine = getAvailableSteamerMachine(steamerMachines);
        metadata = utility.addMachineMetadata(metadata, machine.getMachineName());
        SteamedMilk milk = machine.steam(metadata);
        Instant end = Instant.now();
        utility.collectApexMetric(utility.buildThreadMeta(metadata, Thread.currentThread().getName()),
                Step.STEAM_MILK, start, end);
        return milk;
    }

    private Coffee mix(StringBuffer metadata) {
        Coffee coffee = new Coffee();
        FileOutputStream fout = null;
        try {
            String[] split = metadata.toString().split(":");
            String customerName = split[0];
            String customerId = customerName.split("customer-")[1];
            String grindingMachine = split[1];
            String espressoMachine = split[2];
            String steamingMachine = split[3];

            fout = new FileOutputStream(String.format(Constants.COFFEE_FORMAT, customerId));
            ZipOutputStream zout = new ZipOutputStream(fout);

            ZipEntry entry = new ZipEntry(Constants.COFFEE_JAR_ENTRY);
            zout.putNextEntry(entry);
            String info = String.format(Constants.DATA_FORMAT, customerName, grindingMachine, espressoMachine, steamingMachine);
            InputStream fis = new ByteArrayInputStream(info.getBytes());

            int data = 0;
            while ((data = fis.read()) != -1) {
                zout.write(data);
            }

            zout.closeEntry();
            zout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coffee;
    }

    private GrinderMachine getAvailableGrinderMachine(List<GrinderMachine> machines) {
        Optional<GrinderMachine> optional = machines.stream().filter(m -> {
            boolean isAvailable = false;
            try {
                isAvailable = !m.isBeanInventoryEmpty() && m.getGrinderLock().tryLock(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return isAvailable;
        }).findAny();
        return optional.orElseThrow(() -> new OutOfIngredientsException("Beans Inventory is empty"));
    }

    private EspressoMachine getAvailableEspressoMachine(List<EspressoMachine> machines) {
        return machines.stream().filter(m -> {
            boolean isAvailable = false;
            try {
                isAvailable = m.getEspressoLock().tryLock(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return isAvailable;
        }).findAny().get();
    }

    private SteamerMachine getAvailableSteamerMachine(List<SteamerMachine> machines) {
        Optional<SteamerMachine> optional = machines.stream().filter(m -> {
            boolean isAvailable = false;
            try {
                isAvailable = !m.isMilkInventoryEmpty() && m.getSteamerLock().tryLock(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return isAvailable;
        }).findAny();

        return optional.orElseThrow(() -> new OutOfIngredientsException("Milk Inventory is empty"));
    }

}
