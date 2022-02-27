package com.machine.espresso;

import com.coffee.shared.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableConfigurationProperties(EspressoProperties.class)
@ConditionalOnClass(EspressoMachine.class)
public class EspressoMachineAutoConfiguration {

    @Autowired
    private EspressoProperties espressoProperties;

    @Bean
    public List<EspressoMachine> espressoMachines() {
        return Stream.iterate(1, i -> i + 1)
                .limit(espressoProperties.getLimit())
                .map(i -> {
                            EspressoMachine machine = new EspressoMachine(String.format(Constants.MACHINEID_FORMAT, Constants.ESPRESSO_PREFIX, i));
                            return machine;
                        }
                )
                .collect(Collectors.toList());
    }
}
