package com.gustavolessa.csvcomparisontool;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {
    // data from file
    private final List<String> columns;
    private final List<CSVEntry> dataset1;
    private final List<CSVEntry> dataset2;
    private final List<String> rateColumns;
    // parameters defined by run arguments
    int systemColumnIndex;
    @Autowired
    private FileReader fileReader;
    @Autowired
    private ArgsHandler argsHandler;
    private String systemA;
    private String systemB;
    private String sameColumn;
    private List<String> columnsToCompare;

    public Data() {
        this.columns = new ArrayList<>();
        this.dataset1 = new ArrayList<>();
        this.dataset2 = new ArrayList<>();
        this.rateColumns = new ArrayList<>();
        this.sameColumn = "";
        this.systemA = "";
        this.systemB = "";
        this.systemColumnIndex = -1;
        this.columnsToCompare = new ArrayList<>();
    }

    public void readFile() throws FileNotFoundException {
        try {
            fileReader.setSrc(argsHandler.getSrc());
            fileReader.init();
            fileReader.read();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("CSV file not found.");
        }
    }

    public void addRateColumns(String rateColumn) {
        this.rateColumns.add(rateColumn);
    }

    public void addColumn(String col) {
        columns.add(col);
    }

    private void setArguments() {
        systemColumnIndex = columns.indexOf(argsHandler.getSystemColumnId());
        //  System.out.println("index system :" +systemColumnIndex);
        systemA = argsHandler.getDatasetOptions().get(0);
        // System.out.println("system a: "+systemA);
        systemB = argsHandler.getDatasetOptions().get(1);
        // System.out.println("system b: "+systemB);
        sameColumn = argsHandler.getSameColumn();

        columnsToCompare = argsHandler.getColumnsToCompare();

    }

    public void addLine(String[] line) {
        if (line[systemColumnIndex].equalsIgnoreCase(systemA)) {
            addLineToDataset(dataset1, line);
        } else if (line[systemColumnIndex].equalsIgnoreCase(systemB)) {
            addLineToDataset(dataset2, line);
        }
    }

    private void addLineToDataset(List<CSVEntry> dataset, String[] line) {
        CSVEntry entry = new CSVEntry();
        for (int x = 0; x < line.length; x++) {
            entry.add(columns.get(x), line[x]);
//            System.out.println("column = "+columns.get(x));
//            System.out.println("field = "+line[x]);

        }
        dataset.add(entry);
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        Arrays.stream(columns).forEach(e -> addColumn(e.trim()));
        setArguments();
    }

    public List<CSVEntry> getDataset1() {
        return dataset1;
    }

    public List<CSVEntry> getDataset2() {
        return dataset2;
    }

    public String getSameColumn() {
        return sameColumn;
    }

    public void setSameColumn(String sameColumn) {
        this.sameColumn = sameColumn;
    }

    public List<String> getColumnsToCompare() {
        return columnsToCompare;
    }

    public void printDatasets() {
        printColumns();
        printDataset(dataset1);
        printColumns();
        printDataset(dataset2);
    }

    public void printColumns() {
        columns.forEach(e -> System.out.print(e + "\t"));
        System.out.println();
    }

    public void printDataset(List<CSVEntry> dataset) {
        for (int x = 0; x < dataset.size(); x++) {
            for (int y = 0; y < columns.size(); y++) {
                System.out.print(dataset
                        .get(x)
                        .getData()
                        .get(columns
                                .get(y)) + "\t\t");
            }
            System.out.println();
        }
    }
}
