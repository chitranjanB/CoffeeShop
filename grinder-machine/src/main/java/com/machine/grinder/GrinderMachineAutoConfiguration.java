package com.machine.grinder;

import com.coffee.shared.config.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@ConditionalOnClass(GrinderMachine.class)
public class GrinderMachineAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public List<GrinderMachine> grinderMachines() {
        return Stream.iterate(1, i -> i + 1)
                .limit(4)
                .map(i -> {
                            GrinderMachine machine = new GrinderMachine(String.format(Constants.MACHINEID_FORMAT, Constants.GRINDER_PREFIX, i));
                            return machine;
                        }
                )
                .collect(Collectors.toList());
    }
}
