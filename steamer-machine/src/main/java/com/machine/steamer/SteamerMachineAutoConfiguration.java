package com.machine.steamer;

import com.coffee.shared.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableConfigurationProperties(SteamerProperties.class)
@ConditionalOnClass(SteamerMachine.class)
public class SteamerMachineAutoConfiguration {

    @Autowired
    private SteamerProperties steamerProperties;

    @Bean
    public List<SteamerMachine> steamerMachines() {
        return Stream.iterate(1, i -> i + 1)
                .limit(steamerProperties.getLimit())
                .map(i -> {
                            SteamerMachine machine = new SteamerMachine(String.format(Constants.MACHINEID_FORMAT, Constants.STEAMER_PREFIX, i));
                            return machine;
                        }
                )
                .collect(Collectors.toList());
    }
}
