package com.simulation.shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "coffeeshop")
public class CoffeeShopPropConfig {
    private Inventory inventory;
    private Customer customer;
    private Machine machine;
    private Step step;
    private Debug debug;

    public static class Inventory {
        private Beans beans;
        private Milk milk;

        public static class Beans {
            private int limit;

            public int getLimit() {
                return limit;
            }

            public void setLimit(int limit) {
                this.limit = limit;
            }
        }

        public static class Milk {
            private int limit;

            public int getLimit() {
                return limit;
            }

            public void setLimit(int limit) {
                this.limit = limit;
            }
        }

        public Beans getBeans() {
            return beans;
        }

        public void setBeans(Beans beans) {
            this.beans = beans;
        }

        public Milk getMilk() {
            return milk;
        }

        public void setMilk(Milk milk) {
            this.milk = milk;
        }
    }

    public static class Customer {
        private int limit;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }
    }

    public static class Machine {
        private int limit;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }
    }

    public static class Step {
        private int processing;

        public int getProcessing() {
            return processing;
        }

        public void setProcessing(int processing) {
            this.processing = processing;
        }
    }

    public static class Debug {
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }
}

