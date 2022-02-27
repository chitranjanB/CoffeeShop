package com.machine.grinder;

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
@EnableConfigurationProperties(GrinderProperties.class)
@ConditionalOnClass(GrinderMachine.class)
public class GrinderMachineAutoConfiguration {

    @Autowired
    private GrinderProperties grinderProperties;

    @Bean
    public List<GrinderMachine> grinderMachines() {
        return Stream.iterate(1, i -> i + 1)
                .limit(grinderProperties.getLimit())
                .map(i -> {
                            GrinderMachine machine = new GrinderMachine(String.format(Constants.MACHINEID_FORMAT, Constants.GRINDER_PREFIX, i));
                            return machine;
                        }
                )
                .collect(Collectors.toList());
    }
}
