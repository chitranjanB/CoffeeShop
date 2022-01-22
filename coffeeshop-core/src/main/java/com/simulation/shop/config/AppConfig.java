package com.simulation.shop.config;

import com.simulation.shop.CoffeeShop;
import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.util.CoffeeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class AppConfig {

    @Autowired
    private CoffeeUtility utility;

    @Bean
    public List<GrinderMachine> grinderMachines() {
        return Stream.iterate(1, i -> i + 1)
                .limit(Constants.CUSTOMERS)
                .map(i -> {
                            GrinderMachine machine = new GrinderMachine(String.format(Constants.MACHINEID_FORMAT, Constants.GRINDER_PREFIX, i));
                            machine.setUtility(utility);
                            return machine;
                        }
                )
                .collect(Collectors.toList());
    }

    @Bean
    public List<EspressoMachine> espressoMachines() {
        return Stream.iterate(1, i -> i + 1)
                .limit(Constants.CUSTOMERS)
                .map(i -> {
                    EspressoMachine machine = new EspressoMachine(String.format(Constants.MACHINEID_FORMAT, Constants.ESPRESSO_PREFIX, i));
                    machine.setUtility(utility);
                    return machine;
                })
                .collect(Collectors.toList());
    }

    @Bean
    public List<SteamerMachine> steamerMachines() {
        return Stream.iterate(1, i -> i + 1)
                .limit(Constants.CUSTOMERS)
                .map(i -> {
                    SteamerMachine machine = new SteamerMachine(String.format(Constants.MACHINEID_FORMAT, Constants.STEAMER_PREFIX, i));
                    machine.setUtility(utility);
                    return machine;
                })
                .collect(Collectors.toList());
    }

}