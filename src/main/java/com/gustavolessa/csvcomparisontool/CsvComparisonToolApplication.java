package com.gustavolessa.csvcomparisontool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CsvComparisonToolApplication {

	private static final Logger LOG = LoggerFactory
			.getLogger(CsvComparisonToolApplication.class);

//    @Autowired
//    private AppRunner appRunner;


	public static void main(String... args) {

		new SpringApplicationBuilder(CsvComparisonToolApplication.class)
				.headless(false)
				.web(WebApplicationType.NONE)
				.run(args);

//        ConfigurableApplicationContext ctx  =new SpringApplicationBuilder(MainWindow.class).headless(false).run(args);
//		//SpringApplication.run(CsvComparisonToolApplication.class, args);
//        JFrame frame = new JFrame("CSV Comparison Tool");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
	}

	//	@Override
	public void run(ApplicationArguments args) {

//        MainWindow appWindow = new MainWindow();
		// appWindow.setWindow();

		//appRunner.start(args); // run app

		LOG.info("APPLICATION FINISHED");
	}

}
