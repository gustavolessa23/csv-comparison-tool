package com.gustavolessa.csvcomparisontool;

import com.gustavolessa.csvcomparisontool.services.AppRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CsvComparisonToolApplication implements ApplicationRunner {

	private static final Logger LOG = LoggerFactory
			.getLogger(CsvComparisonToolApplication.class);

    //	@Autowired
//	private ApplicationContext context;
//
//	@Autowired
//	private Data data;
//
//	@Autowired
//	private Report report;
//
//	@Autowired
//	private ArgsHandler argsHandler;
    @Autowired
    private AppRunner appRunner;

	public static void main(String... args) {
		SpringApplication.run(CsvComparisonToolApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
        LOG.info("STARTING THE APPLICATION");

        appRunner.start(args);

        LOG.info("APPLICATION FINISHED");
    }

}
