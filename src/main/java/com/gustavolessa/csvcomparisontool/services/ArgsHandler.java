package com.gustavolessa.csvcomparisontool.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String dest;
    private String src;


    public ArgsHandler() {
        this.args = null;
    }

    /**
     * Get running params and read them.
     *
     * @param args
     * @throws IllegalArgumentException
     */
    public void setArgs(ApplicationArguments args) throws IllegalArgumentException {
        this.args = args;
        readArgs();
    }

    /**
     * Read desired args, throwing exception if a needed one is not found.
     *
     * @throws IllegalArgumentException
     */
    private void readArgs() throws IllegalArgumentException {
        // output path
        dest = Optional.of(args.getOptionValues("dest").get(0))
                .orElseGet(() -> System.getProperty("java.io.tmpdir"));

        // source file path
        src = getArgSingle("src", "--src parameter is required.");

        // column name that defines the systems to be compared
        systemColumnId = getArgSingle("system", "--system parameter is required to identify which column" +
                "contains the information that will be used to split datasets.");

        // value options to the above column
        datasetOptions = getArgList("options", "--options parameter is required to define " +
                "the dataset present in the CSV.");

        // there should be 2 options above. Throw exception if not.
        if (datasetOptions.size() != 2)
            throw new IllegalArgumentException("--options parameter should contain exactly two items. (E.g --options=\"A,B\"");

        // sets of columns, which one defining key columns that will be used to determine
        // if the entries are comparable
        keyColumns = getArgLists("key", "--same parameter is required to define which column " +
                "identifies the account number.");
//
//        // output file type
//        outputFileType = Optional.of(args.getNonOptionArgs())
//                .orElse(List.of("csv"));

        // columns containing the values to be compared
        columnsToCompare = getArgList("compare", "--compare parameter is required to define " +
                "which columns will be used to compare the rates applied.");

    }


    public String getArgSingle(String arg, String errMessage) throws IllegalArgumentException {
        return Optional.of(args.getOptionValues(arg).get(0))
                .orElseThrow(() -> new IllegalArgumentException(errMessage));
    }

    public List<String> getArgList(String arg, String errMessage) throws IllegalArgumentException {
        List<String> strings = new ArrayList<>(Arrays.asList(Optional.of(args.getOptionValues(arg).get(0))
                .orElseThrow(() ->
                        new IllegalArgumentException(errMessage))
                .split(",", -1)));
        return strings;
    }

    public List<List<String>> getArgLists(String arg, String errMessage) throws IllegalArgumentException {
        return Optional.of(args.getOptionValues(arg))
                .orElseThrow(() ->
                        new IllegalArgumentException(errMessage))
                .stream()
                .map(e -> new ArrayList<String>(Arrays.asList(e.split(",", -1))))
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

//    public List<String> getOutputFileType() {
//        return outputFileType;
//    }

    public String getDest() {
        return dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSystemColumnId(String systemColumnId) {
        this.systemColumnId = systemColumnId;
    }

    public void setDatasetOptions(List<String> datasetOptions) {
        this.datasetOptions = datasetOptions;
    }

    public void setColumnsToCompare(List<String> columnsToCompare) {
        this.columnsToCompare = columnsToCompare;
    }

    public void setKeyColumns(List<List<String>> keyColumns) {
        this.keyColumns = keyColumns;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
