package com.simulation.shop;

import com.simulation.shop.config.CoffeeShopConfig;
import com.simulation.shop.config.Constants;
import com.simulation.shop.util.CoffeeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

@SpringBootApplication
public class CoffeeshopCoreApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoffeeshopCoreApplication.class);

    @Autowired
    private CoffeeShop shop;

    @Autowired
    private CoffeeShopConfig config;

    @Autowired
    private CoffeeUtility utility;

    public static void main(String[] args) {
        SpringApplication.run(CoffeeshopCoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            PipedOutputStream pop = new PipedOutputStream();
            PipedInputStream pip = new PipedInputStream();
            pop.connect(pip);

            Runnable kioskOperator = () -> {
                try {
                    Scanner sc = new Scanner(System.in);
                    int orders = -1;

                    while (!utility.isShopClosed(orders)) {
                        LOGGER.info("Input no. of orders to be placed : ");
                        orders = sc.nextInt();
                        utility.writeNoOfOrders(pop, orders);
                    }
                    sc.close();
                } catch (Exception e) {
                    LOGGER.error("Error while placing order " + e.getLocalizedMessage(), e);
                }
            };

            Thread kioskOperatorThread = new Thread(kioskOperator);
            kioskOperatorThread.start();

            //The below code is given to main thread, so that results are ready once all orders are processed
            int machines = utility.fetchRequiredMachines(Constants.CUSTOMERS);

            utility.loadupBeans(machines, config.getInventory().getBeans().getLimit());
            utility.loadupMilk(machines, config.getInventory().getMilk().getLimit());
            try {
                shop.start(pip);
            } catch (Exception e) {
                LOGGER.error("We are currently experiencing some issues " + e.getLocalizedMessage(), e);
            }
        };
    }
}
