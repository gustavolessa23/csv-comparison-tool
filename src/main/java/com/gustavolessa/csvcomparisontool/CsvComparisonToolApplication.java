package com.gustavolessa.csvcomparisontool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.FileNotFoundException;

@SpringBootApplication
public class CsvComparisonToolApplication implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory
			.getLogger(CsvComparisonToolApplication.class);

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Data data;

	@Autowired
	private Report report;

	public static void main(String... args) {
		SpringApplication.run(CsvComparisonToolApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		LOG.info("STARTING THE APPLICATION");

		ArgsHandler argsHandler = context.getBean(ArgsHandler.class);

		try {
			argsHandler.setArgs(args);
		} catch (IllegalArgumentException e) {
			LOG.error(e.getMessage());
		}

		try {
			data.readFile();
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
		}

		report.generate();
		report.write();
//		try{
//			fileReader.setSrc(argsHandler.getSrc());
//			fileReader.init();
//			fileReader.read();
//		}catch (FileNotFoundException e){
//			LOG.error(e.getMessage());
//		}


		LOG.info("APPLICATION FINISHED");
	}

}
