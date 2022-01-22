package com.simulation.shop;

import java.time.Instant;

import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoffeeShop {

    @Autowired
    private GrinderMachine grinderMachine;

    @Autowired
    private EspressoMachine espressoMachine;

    @Autowired
    private SteamerMachine steamerMachine;

    public Latte brewLatte() {
        Grounds grounds = grindCoffee(grinderMachine);
        Coffee coffee = makeEspresso(espressoMachine, grounds);
        Milk milk = steamMilk(steamerMachine);
        return makeLatte(coffee, milk);
    }

    private Grounds grindCoffee(GrinderMachine grinderMachine) {
        Grounds grounds = null;
        synchronized (grinderMachine) {
            grounds = grinderMachine.grind();
        }
        return grounds;
    }

    private Coffee makeEspresso(EspressoMachine espressoMachine, Grounds grounds) {
        Coffee coffee = null;
        synchronized (espressoMachine) {
            coffee = espressoMachine.concentrate();
        }
        return coffee;
    }

    private Milk steamMilk(SteamerMachine steamerMachine) {
        Milk milk = null;
        synchronized (steamerMachine) {
            milk = steamerMachine.steam();
        }
        return milk;
    }

    private Latte makeLatte(Coffee coffee, Milk milk) {
        return CoffeeUtility.mix(coffee, milk);
    }
}
