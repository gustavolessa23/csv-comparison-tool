package com.gustavolessa.csvcomparisontool.data;

import com.gustavolessa.csvcomparisontool.entities.CSVEntry;
import com.gustavolessa.csvcomparisontool.services.ArgsHandler;
import com.gustavolessa.csvcomparisontool.services.ReportReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Data {

    // data from file
    private final List<String> columns;
    private List<CSVEntry> dataset1;
    private List<CSVEntry> dataset2;

    // parameters defined by run arguments
    int systemColumnIndex;
    private String systemA;
    private String systemB;
    private List<List<String>> keyColumns;
    private List<String> columnsToCompare;
    private String outputDest;

    @Autowired
    private ReportReader reportReader;
    @Autowired
    private ArgsHandler argsHandler;

    public Data() {
        this.columns = new ArrayList<>();
        this.dataset1 = new ArrayList<>();
        this.dataset2 = new ArrayList<>();
        this.keyColumns = new ArrayList<>();
        this.systemA = "";
        this.systemB = "";
        this.systemColumnIndex = -1;
        this.columnsToCompare = new ArrayList<>();
        this.outputDest = "";
    }

    public void readFile() throws FileNotFoundException {
        try {
            reportReader.setSrc(argsHandler.getSrc());
            reportReader.init();
            reportReader.read();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("CSV file not found.");
        } finally {
            reportReader.close();
        }
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
        keyColumns = argsHandler.getKeyColumns();
        // sameColumns.forEach(System.out::println);

        columnsToCompare = argsHandler.getColumnsToCompare();

        outputDest = argsHandler.getDest();
    }

    public void addLine(String[] line) {

        if (line[systemColumnIndex].equalsIgnoreCase(systemA)) {
            addLineToDataset(dataset1, line);
        } else if (line[systemColumnIndex].equalsIgnoreCase(systemB)) {
            addLineToDataset(dataset2, line);
        }
    }

    public void removeDuplicatesFromDatasets() {
        dataset1 = dataset1.stream().distinct().collect(Collectors.toList());
        dataset2 = dataset2.stream().distinct().collect(Collectors.toList());
    }

    private void addLineToDataset(List<CSVEntry> dataset, String[] line) {
        CSVEntry entry = new CSVEntry();
        for (int x = 0; x < line.length; x++) {
            entry.add(columns.get(x), line[x].trim());
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

    public List<List<String>> getKeyColumns() {
        return keyColumns;
    }

    public String getOutputDest() {
        return outputDest;
    }

    public void setKeyColumns(List<List<String>> keyColumns) {
        this.keyColumns = keyColumns;
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

    public String datasetToString(List<CSVEntry> dataset) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < dataset.size(); x++) {
            for (int y = 0; y < columns.size(); y++) {
                sb.append(dataset
                        .get(x)
                        .getData()
                        .get(columns
                                .get(y)));

                if (y != columns.size() - 1) sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
