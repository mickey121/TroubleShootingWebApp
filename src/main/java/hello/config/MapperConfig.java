package hello.config;

import hello.dao.Option;
import hello.dao.Step;
import hello.dto.OptionDto;
import hello.dto.StepDto;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    MapperFactory mapperFactory() {
        DefaultMapperFactory defaultMapperFactory = new DefaultMapperFactory.Builder().useAutoMapping(true).build();

        defaultMapperFactory.classMap(Step.class, StepDto.class)
                .byDefault()
                .mapNulls(false).byDefault().register();

        defaultMapperFactory.classMap(Option.class, OptionDto.class)
                .field("steps{stepName}", "stepNames{}")
                .byDefault()
                .mapNulls(false).byDefault().register();

        return defaultMapperFactory;
    }

    @Bean
    MapperFacade mapperFacade() {
        return mapperFactory().getMapperFacade();
    }
}