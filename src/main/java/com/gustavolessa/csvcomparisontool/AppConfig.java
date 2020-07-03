package com.gustavolessa.csvcomparisontool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

//    @Bean
//    @Scope(value = "prototype")
//    public ArgsHandler argsValidator(ApplicationArguments args) {
//        return new ArgsHandler(args);
//    }

    @Bean
    public ArgsHandler argsValidator() {
        return new ArgsHandler();
    }

    @Bean
    public Data data() {
        return new Data();
    }

    @Bean
    public FileReader fileReader() {
        return new FileReader();
    }

    @Bean
    public Report report() {
        return new Report();
    }

//    @Bean
//    public FileReader fileReader(){
//        return new FileReader();
//    }
}