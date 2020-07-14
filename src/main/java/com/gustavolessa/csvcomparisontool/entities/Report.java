package com.gustavolessa.csvcomparisontool.entities;

import java.util.ArrayList;
import java.util.List;

public class Report {

    private final List<List<CSVReportEntry>> diff;
    private long lastCorrelationId;
    private final List<String> allColumns;
    private final List<List<String>> keyColumnsList;
    private final List<String> columnsToCompare;
    private final List<CSVEntry> dataset1;
    private final List<CSVEntry> dataset2;

    private Report(List<CSVEntry> dataset1,
                   List<CSVEntry> dataset2,
                   List<List<String>> keyColumnsList,
                   List<String> columnsToCompare,
                   List<String> allColumns) {

        diff = new ArrayList<>();
        this.keyColumnsList = keyColumnsList;
        this.dataset1 = dataset1;
        this.dataset2 = dataset2;
        this.columnsToCompare = columnsToCompare;
        this.allColumns = allColumns;
        lastCorrelationId = 0;

        generateReport();
    }

    private void generateReport() {
        /*
        for each entry of dataset1, check if there an entry in dataset2 with the same account number
         */
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

        formatForExcel();
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

    public void formatForExcel() {

        if (!allColumns.get(0).equalsIgnoreCase("Correlation ID")) {
            allColumns.add(0, "Correlation ID");
        }

        List<List<List<String>>> content = new ArrayList<>();

        for (int i = 0; i < diff.size(); i++) {
            List<CSVReportEntry> run = diff.get(i);
            List<List<String>> runContent = new ArrayList<>();


            for (int x = 0; x < run.size(); x++) {
                List<String> temp = new ArrayList<>();
                temp.add(String.valueOf(run.get(x).getId()));
                for (int y = 1; y < allColumns.size(); y++) {
                    temp.add(run
                            .get(x)
                            .getData()
                            .get(allColumns
                                    .get(y)));
                }
                runContent.add(temp);
            }
            content.add(runContent);
        }
    }

    public List<List<CSVReportEntry>> getDiff() {
        return diff;
    }

    public List<String> getAllColumns() {
        return allColumns;
    }

    public List<List<String>> getKeyColumnsList() {
        return keyColumnsList;
    }

    public List<String> getColumnsToCompare() {
        return columnsToCompare;
    }

    public static final class ReportBuilder {
        private List<List<String>> keyColumnsList;
        private List<String> columnsToCompare;
        private List<CSVEntry> dataset1;
        private List<CSVEntry> dataset2;
        private List<String> allColumns;

        private ReportBuilder() {
        }

        public static ReportBuilder aReport() {
            return new ReportBuilder();
        }

        public ReportBuilder withAllColumns(List<String> allColumns) {
            this.allColumns = allColumns;
            return this;
        }

        public ReportBuilder withKeyColumnsList(List<List<String>> keyColumnsList) {
            this.keyColumnsList = keyColumnsList;
            return this;
        }

        public ReportBuilder withColumnsToCompare(List<String> columnsToCompare) {
            this.columnsToCompare = columnsToCompare;
            return this;
        }

        public ReportBuilder withDataset1(List<CSVEntry> dataset1) {
            this.dataset1 = dataset1;
            return this;
        }

        public ReportBuilder withDataset2(List<CSVEntry> dataset2) {
            this.dataset2 = dataset2;
            return this;
        }

        public Report build() {
            Report report = new Report(dataset1, dataset2, keyColumnsList, columnsToCompare, allColumns);
            return report;
        }
    }
}
