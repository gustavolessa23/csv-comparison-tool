package com.gustavolessa.csvcomparisontool.entities;

import com.gustavolessa.csvcomparisontool.data.Data;
import com.gustavolessa.csvcomparisontool.services.ReportWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Report {

    private final List<List<CSVReportEntry>> diff;
    private long lastCorrelationId;

    private final List<String[]> toWrite;
    private List<List<String>> keyColumnsList;
    private List<String> columnsToCompare;
    private List<CSVEntry> dataset1;
    private List<CSVEntry> dataset2;


    private Path output;

    @Autowired
    private Data data;

    public Report() {
        diff = new ArrayList<>();
        toWrite = new ArrayList<>();
        keyColumnsList = null;
        columnsToCompare = null;
        dataset1 = null;
        dataset2 = null;
        lastCorrelationId = 0;
    }

    public void generate(List<CSVEntry> dataset1, List<CSVEntry> dataset2, List<List<String>> keyColumnsList, List<String> columnsToCompare) {
        /*
        for each entry of dataset1, check if there an entry in dataset2 with the same account number
         */
        this.keyColumnsList = keyColumnsList;
        this.dataset1 = dataset1;
        this.dataset2 = dataset2;
        this.columnsToCompare = columnsToCompare;

        // List<String> test = keyColumnsList.get(0);

        for (int run = 0; run < keyColumnsList.size(); run++) {

            //  List<CSVReportEntry> tempRun = new ArrayList<>();
            List<CSVReportEntry> correlation = new ArrayList<>();

            //For each entry in dataset1
            for (CSVEntry rowFrom1 : dataset1) {

                List<CSVReportEntry> foundItems = new ArrayList<>();

                // for each entry in dataset2
                for (CSVEntry rowFrom2 : dataset2) {

                    if (entriesHaveTheSameKeys(rowFrom1, rowFrom2, keyColumnsList.get(run))) {

                        boolean isAdded = false;

                        for (int currentColumn = 0; currentColumn < columnsToCompare.size(); currentColumn++) {
                            String column = columnsToCompare.get(currentColumn);

                            if (isDifferent(rowFrom1, rowFrom2, column)) {

                                if (foundItems.isEmpty()) {
                                    lastCorrelationId++;
                                    foundItems.add(CSVReportEntry.reportFromEntry(rowFrom1));
                                }

                                if (!isAdded) {
                                    foundItems.add(CSVReportEntry.reportFromEntry(rowFrom2));
                                    isAdded = true;
                                }

                                foundItems.get(0).addConflicting(column);
                                foundItems.get(foundItems.size() - 1).addConflicting(column);

                            }
                        }
                    }
                }
                foundItems.forEach(e -> e.setId(lastCorrelationId));
                correlation.addAll(foundItems);
                foundItems.clear();
            }

            diff.add(correlation);
            lastCorrelationId = 0;
        }


//        System.out.println("------ REPORT -------");
//
//        data.getColumns().forEach(e -> System.out.print(e + "\t"));
//        System.out.println();
//        for (int x = 0; x < diff.size(); x++) {
//            for (int y = 0; y < data.getColumns().size(); y++) {
//                System.out.print(diff
//                        .get(x)
//                        .getData()
//                        .get(data
//                                .getColumns()
//                                .get(y)) + "\t\t");
//            }
//            System.out.println();
//        }
    }

    private boolean isDifferent(CSVEntry set1, CSVEntry set2, String column) {
        return !set1.getData().get(column)
                .equalsIgnoreCase(set2.getData()
                        .get(column));
    }

    private boolean entriesHaveTheSameKeys(CSVEntry set1, CSVEntry set2, List<String> keys) {
//        int run = ;
        boolean entriesHaveTheSameKeys = false;
//                List<String> keyColumns = data.getSameColumns();
        // keyColumns.forEach(System.out::println);

        for (int col = 0; col < keys.size(); col++) {
            
            if (set1.getData().get(keys.get(col))
                    .equalsIgnoreCase(set2.getData().get(keys.get(col)))) {
//                System.out.println(set1.getData().get(keys.get(col)));
//                System.out.println(set2.getData().get(keys.get(col)));
                entriesHaveTheSameKeys = true;
            } else {
                entriesHaveTheSameKeys = false;
                break;
            }
        }
        return entriesHaveTheSameKeys;
    }

    public void write(List<String> columns, String path) {

        // List<String> columns = data.getColumns();
        columns.add(0, "Correlation ID");

        for (List<CSVReportEntry> run :
                diff) {
            toWrite.add(columns.toArray(String[]::new));

            for (int x = 0; x < run.size(); x++) {
                List<String> temp = new ArrayList<>();
                temp.add(String.valueOf(run.get(x).getId()));
                for (int y = 1; y < columns.size(); y++) {
                    temp.add(run
                            .get(x)
                            .getData()
                            .get(columns
                                    .get(y)));
                }
                toWrite.add(temp.toArray(String[]::new));
            }
        }

        try {
            ReportWriter.writeReport(toWrite, Paths.get(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
