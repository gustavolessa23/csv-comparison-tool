package com.gustavolessa.csvcomparisontool.entities;

import java.util.ArrayList;
import java.util.List;

public class Report {

    private final List<List<CSVReportRow>> diff;
    private long lastCorrelationId;
    private final List<String> allColumns;
    private final List<List<String>> keyColumnsList;
    private final List<String> columnsToCompare;
    private final List<CSVRow> dataset1;
    private final List<CSVRow> dataset2;
    private final String systemColumn;

    private Report(List<CSVRow> dataset1,
                   List<CSVRow> dataset2,
                   List<List<String>> keyColumnsList,
                   List<String> columnsToCompare,
                   List<String> allColumns,
                   String systemColumn) {

        diff = new ArrayList<>();
        this.keyColumnsList = keyColumnsList;
        this.dataset1 = dataset1;
        this.dataset2 = dataset2;
        this.columnsToCompare = columnsToCompare;
        this.allColumns = allColumns;
        lastCorrelationId = 0;
        this.systemColumn = systemColumn;

        generateReport();
    }

    private void generateReport() {

        diff.clear(); // remove previous results


        /*
        for each entry of dataset1, check if there an entry in dataset2 with the same account number
         */
        for (int run = 0; run < keyColumnsList.size(); run++) {

            //remove duplicate entries, considering the keyColumns
            List<CSVRow> distinctDataset1 = removeDuplicates(dataset1, keyColumnsList.get(run));
            System.out.println("Size dataset1 = " + distinctDataset1.size());
            List<CSVRow> distinctDataset2 = removeDuplicates(dataset2, keyColumnsList.get(run));

            //  List<CSVReportRow> tempRun = new ArrayList<>();
            List<CSVReportRow> correlation = new ArrayList<>();

            //For each entry in dataset1
            for (CSVRow rowFrom1 : distinctDataset1) {

                List<CSVReportRow> foundItems = new ArrayList<>();

                // for each entry in dataset2
                for (CSVRow rowFrom2 : distinctDataset2) {

                    if (entriesHaveTheSameKeys(rowFrom1, rowFrom2, keyColumnsList.get(run))) {

                        boolean isAdded = false;

                        for (int currentColumn = 0; currentColumn < columnsToCompare.size(); currentColumn++) {
                            String column = columnsToCompare.get(currentColumn);

                            if (isDifferent(rowFrom1, rowFrom2, column)) {

                                if (foundItems.isEmpty()) {
                                    lastCorrelationId++;
                                    foundItems.add(CSVReportRow.reportFromEntry(rowFrom1));
                                }

                                if (!isAdded) {
                                    foundItems.add(CSVReportRow.reportFromEntry(rowFrom2));
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

    }

    private List<CSVRow> removeDuplicates(List<CSVRow> dataset, List<String> keyColumns) {

        List<String> columnsToCheck = new ArrayList<>();
        columnsToCheck.add(systemColumn);
        columnsToCheck.addAll(keyColumns);
        columnsToCheck.addAll(columnsToCompare);

        if (dataset.isEmpty() || keyColumns.isEmpty()) return dataset;

        List<CSVRow> distinctDataset = new ArrayList<>();

        distinctDataset.add(dataset.get(0));

        for (int i = 1; i < dataset.size(); i++) {
            CSVRow row = dataset.get(i);

            boolean isDuplicate = true;

            for (CSVRow distinctRow : distinctDataset) {

                if (!entriesHaveTheSameKeys(row, distinctRow, columnsToCheck)) {
                    isDuplicate = false;
//                        for (String s : columnsToCompare) {
//                            if (!row.getData().get(s).equalsIgnoreCase(distinctRow.getData().get(s))) {
//                                isDuplicate = false;
//                                break;
//                            }
//
//                        }

                }

            }

            if (!isDuplicate) distinctDataset.add(row);


        }

        return distinctDataset;
    }

    private boolean isDifferent(CSVRow set1, CSVRow set2, String column) {
        return !set1.getData().get(column)
                .equalsIgnoreCase(set2.getData()
                        .get(column));
    }

    private boolean entriesHaveTheSameKeys(CSVRow set1, CSVRow set2, List<String> keys) {
        boolean entriesHaveTheSameKeys = false;


        for (int col = 0; col < keys.size(); col++) {

            if (set1.getData().get(keys.get(col))
                    .equalsIgnoreCase(set2.getData().get(keys.get(col)))) {

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
            List<CSVReportRow> run = diff.get(i);
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

    public List<List<CSVReportRow>> getDiff() {
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
        private List<CSVRow> dataset1;
        private List<CSVRow> dataset2;
        private List<String> allColumns;
        private String systemColumn;

        private ReportBuilder() {
        }

        public static ReportBuilder aReport() {
            return new ReportBuilder();
        }

        public ReportBuilder withAllColumns(List<String> allColumns) {
            this.allColumns = allColumns;
            return this;
        }

        public ReportBuilder withSystemColumn(String systemColumn) {
            this.systemColumn = systemColumn;
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

        public ReportBuilder withDataset1(List<CSVRow> dataset1) {
            this.dataset1 = dataset1;
            return this;
        }

        public ReportBuilder withDataset2(List<CSVRow> dataset2) {
            this.dataset2 = dataset2;
            return this;
        }

        public Report build() {
            Report report = new Report(dataset1, dataset2, keyColumnsList, columnsToCompare, allColumns, systemColumn);
            return report;
        }
    }
}
