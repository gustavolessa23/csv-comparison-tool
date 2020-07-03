package com.gustavolessa.csvcomparisontool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import java.util.List;
import java.util.Optional;

/**
 * Validates the arguments provided.
 */

public class ArgsHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ArgsHandler.class);
    private ApplicationArguments args;

    // --system="Platform" --same="AccountNumber" --compare="Dim1,Dim2,Dim3" csv
    private String systemColumnId;
    private List<String> datasetOptions;
    private List<String> columnsToCompare;
    private String sameColumn;
    private List<String> outputFileType;
    private String dest;
    private String src;

//    @Autowired
//    private Data data;
//    private FileReader fileReader;

    public ArgsHandler() {
        this.args = null;
    }

    public void setArgs(ApplicationArguments args) throws IllegalArgumentException {
        this.args = args;
        readArgs();
    }

    private void readArgs() throws IllegalArgumentException {
        dest = Optional.of(args.getOptionValues("dest").get(0))
                .orElseGet(() -> System.getProperty("java.io.tmpdir"));

        src = Optional.of(args.getOptionValues("src").get(0))
                .orElseThrow(() -> new IllegalArgumentException("--src parameter is required."));

        systemColumnId = Optional.of(args.getOptionValues("system").get(0))
                .orElseThrow(() -> new IllegalArgumentException("--system parameter is required to identify which column" +
                        "contains the information that will be used to split datasets."));

        sameColumn = Optional.of(args.getOptionValues("same").get(0))
                .orElseThrow(() -> new IllegalArgumentException("--same parameter is required to define which column " +
                        "identifies the account number."));

        outputFileType = Optional.of(args.getNonOptionArgs())
                .orElse(List.of("csv"));

        columnsToCompare = List.of(Optional.of(args.getOptionValues("compare").get(0))
                .orElseThrow(() ->
                        new IllegalArgumentException("--compare parameter is required to define " +
                                "which columns will be used to compare the rates applied."))
                .split(",", -1));
        //columnsToCompare.stream().forEach(System.out::println);

//        String coll = Optional.of(args.getOptionValues("options").get(0))
//                .orElseThrow(() -> new IllegalArgumentException("--options parameter is required to define " +
//                "the dataset present in the CSV."));
        datasetOptions = List.of(Optional.of(args.getOptionValues("options").get(0))
                .orElseThrow(() ->
                        new IllegalArgumentException("--options parameter is required to define " +
                                "the dataset present in the CSV."))
                .split(",", -1));

        if (datasetOptions.size() != 2)
            throw new IllegalArgumentException("--options parameter should contain exactly two items. (E.g --options=\"A,B\"");
    }

//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext)
//            throws BeansException {
//        this.fileReader = applicationContext.getBean(FileReader.class);
//    }

    public List<String> getDatasetOptions() {
        return datasetOptions;
    }

    public String getSystemColumnId() {
        return systemColumnId;
    }

    public List<String> getColumnsToCompare() {
        return columnsToCompare;
    }

    public String getSameColumn() {
        return sameColumn;
    }

    public List<String> getOutputFileType() {
        return outputFileType;
    }

    public String getDest() {
        return dest;
    }

    public String getSrc() {
        return src;
    }
}
