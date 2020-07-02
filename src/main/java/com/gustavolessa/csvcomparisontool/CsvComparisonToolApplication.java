package com.gustavolessa.csvcomparisontool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication
public class CsvComparisonToolApplication implements ApplicationRunner {

	private static Logger LOG = LoggerFactory
			.getLogger(CsvComparisonToolApplication.class);

	GenericApplicationContext context
			= new AnnotationConfigApplicationContext(AppConfig.class);

	public static void main(String... args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(CsvComparisonToolApplication.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	public void run(ApplicationArguments args){
//		LOG.info("EXECUTING : command line runner");
		ArgsValidator argsValidator = context.getBean(ArgsValidator.class, args);
		argsValidator.isValid();
	}

}
