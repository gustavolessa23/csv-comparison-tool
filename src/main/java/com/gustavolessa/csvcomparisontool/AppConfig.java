package com.gustavolessa.csvcomparisontool;

import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
//
//    @Autowired
//    GenericApplicationContext context;

//    @Autowired
//    private ApplicationArguments args;

    @Bean
    @Scope(value = "prototype")
    public ArgsValidator argsValidator(ApplicationArguments args) {
        return new ArgsValidator(args);
    }
}