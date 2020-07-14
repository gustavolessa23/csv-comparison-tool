package com.gustavolessa.csvcomparisontool.config;

import com.gustavolessa.csvcomparisontool.data.Data;
import com.gustavolessa.csvcomparisontool.services.AppRunner;
import com.gustavolessa.csvcomparisontool.services.ArgsHandler;
import com.gustavolessa.csvcomparisontool.services.ReportReader;
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
    public AppRunner appRunner() {
        return new AppRunner();
    }

    @Bean
    public ArgsHandler argsValidator() {
        return new ArgsHandler();
    }

    @Bean
    public Data data() {
        return new Data();
    }

    @Bean
    public ReportReader fileReader() {
        return new ReportReader();
    }

//    @Bean
//    public Report report() {
//        return new Report();
//    }

//    @Bean
//    public ReportReader fileReader(){
//        return new ReportReader();
//    }
}