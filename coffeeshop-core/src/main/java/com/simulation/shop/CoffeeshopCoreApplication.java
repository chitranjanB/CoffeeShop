package com.simulation.shop;

import com.simulation.shop.machine.EspressoMachine;
import com.simulation.shop.machine.GrinderMachine;
import com.simulation.shop.machine.SteamerMachine;
import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.Latte;
import com.simulation.shop.model.Milk;
import com.simulation.shop.util.CoffeeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.Instant;

@SpringBootApplication
public class CoffeeshopCoreApplication {

	@Autowired
	private CoffeeShop shop;

	public static void main(String[] args) {
		SpringApplication.run(CoffeeshopCoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Let's inspect the beans provided by Spring Boot:");
			Instant start = Instant.now();
			int customers = args.length > 0 ? Integer.parseInt(args[0]) : 1;

			while (customers > 0) {
				Latte latte = shop.brewLatte();
				System.out.println(latte);
				customers--;
			}

			Instant finish = Instant.now();
			String timeElapsed = CoffeeUtility.timeElapsed(start, finish);
			System.out.println("---------------COFFEE SHOP CLOSED-----------------------");
			System.out.println("time elapsed " + timeElapsed);
		};
	}
}
