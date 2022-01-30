package com.simulation.shop;

import com.simulation.shop.repository.BeansRepository;
import com.simulation.shop.repository.MilkRepository;
import com.simulation.shop.util.CoffeeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class CoffeeshopApplication {

    @Autowired
    private BeansRepository beansRepository;

    @Autowired
    private MilkRepository milkRepository;

    @Autowired
    private CoffeeUtility utility;

    public static void main(String[] args) {
        SpringApplication.run(CoffeeshopApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.simulation.shop"))
                .build();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> utility.loadInventory();
    }

    @Scheduled(fixedRate = 2000)
    public void scheduleInventoryLoad() {
        utility.loadInventory();
    }

}
