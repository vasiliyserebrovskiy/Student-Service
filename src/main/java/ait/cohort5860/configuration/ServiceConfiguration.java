package ait.cohort5860.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (23.06.2025)
 */
@Configuration // component type for configuration
public class ServiceConfiguration {

    @Bean // annotation for adding this method to application context
    ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT); //strong
        return mapper;
    }
}
