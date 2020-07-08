package com.gustavolessa.csvcomparisontool.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Validates the arguments provided.
 */

public class ArgsHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ArgsHandler.class);
    private ApplicationArguments args;

    private String systemColumnId;
    private List<String> datasetOptions;
    private List<String> columnsToCompare;
    private List<List<String>> keyColumns;
    private List<String> outputFileType;
    private String dest;
    private String src;


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

        src = getArgSingle("src", "--src parameter is required.");

        systemColumnId = getArgSingle("system", "--system parameter is required to identify which column" +
                "contains the information that will be used to split datasets.");

        keyColumns = getArgLists("key", "--same parameter is required to define which column " +
                "identifies the account number.");

        outputFileType = Optional.of(args.getNonOptionArgs())
                .orElse(List.of("csv"));

        columnsToCompare = getArgList("compare", "--compare parameter is required to define " +
                "which columns will be used to compare the rates applied.");

        datasetOptions = getArgList("options", "--options parameter is required to define " +
                "the dataset present in the CSV.");

        if (datasetOptions.size() != 2)
            throw new IllegalArgumentException("--options parameter should contain exactly two items. (E.g --options=\"A,B\"");
    }


    public String getArgSingle(String arg, String errMessage) throws IllegalArgumentException {
        return Optional.of(args.getOptionValues(arg).get(0))
                .orElseThrow(() -> new IllegalArgumentException(errMessage));
    }

    public List<String> getArgList(String arg, String errMessage) throws IllegalArgumentException {
        return List.of(Optional.of(args.getOptionValues(arg).get(0))
                .orElseThrow(() ->
                        new IllegalArgumentException(errMessage))
                .split(",", -1));
    }

    public List<List<String>> getArgLists(String arg, String errMessage) throws IllegalArgumentException {
        return Optional.of(args.getOptionValues(arg))
                .orElseThrow(() ->
                        new IllegalArgumentException(errMessage))
                .stream()
                .map(e -> new ArrayList<String>(List.of(e.split(",", -1))))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public List<String> getDatasetOptions() {
        return datasetOptions;
    }

    public String getSystemColumnId() {
        return systemColumnId;
    }

    public List<String> getColumnsToCompare() {
        return columnsToCompare;
    }

    public List<List<String>> getKeyColumns() {
        return keyColumns;
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
