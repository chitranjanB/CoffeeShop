package com.simulation.shop;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutionException;

import com.simulation.shop.config.Config;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.simulation.shop.util.CoffeeUtility;

/**
 * Just a timed test, without exceptions
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoffeeShopTest {

    @Test
    public void test_10_threads() throws Exception {
        PipedOutputStream pop = new PipedOutputStream();
        PipedInputStream pip = new PipedInputStream();
        pop.connect(pip);

        CoffeeUtility.writeNoOfOrders(pop, 10);
        CoffeeUtility.writeNoOfOrders(pop, 0);

        CoffeeUtility.loadupBeans(CoffeeUtility.fetchRequiredMachines(Config.CUSTOMERS), Config.BEANS_INVENTORY_LIMIT);
        CoffeeUtility.loadupMilk(CoffeeUtility.fetchRequiredMachines(Config.CUSTOMERS), Config.MILK_INVENTORY_LIMIT);
        CoffeeShop shop = new CoffeeShop();

        shop.start(pip);
    }
}
