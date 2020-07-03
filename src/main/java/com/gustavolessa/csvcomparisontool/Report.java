package com.gustavolessa.csvcomparisontool;

import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Report {

    private final List<CSVEntry> diff;
    private static long lastCorrelationId = 0;

    private final List<String[]> toWrite;

    private Path output;

    @Autowired
    private Data data;

    public Report() {
        diff = new ArrayList<>();
        toWrite = new ArrayList<>();
    }

    public void generate() {
        /*
        for each entry of dataset1, check if there an entry in dataset2 with the same account number
         */

        List<String> sameColumns = data.getSameColumns();

        //For each entry in dataset1
        for (CSVEntry c : data.getDataset1()) {
            // for each entry in dataset2
            for (CSVEntry s : data.getDataset2()) {

                //    boolean added = false;
                //if account number of entry c = account number of entry s

                boolean isTheSame = false;
//                List<String> sameColumns = data.getSameColumns();
                // sameColumns.forEach(System.out::println);

                for (String column : sameColumns) {
                    if (c.getData().get(column)
                            .equalsIgnoreCase(s.getData().get(column))) {
                        isTheSame = true;
                    } else {
                        isTheSame = false;
                        break;
                    }
                }

                if (isTheSame) {

                    List<String> columnsToCompare = data.getColumnsToCompare();
                    for (int x = 0; x < columnsToCompare.size(); x++) {
                        String column = columnsToCompare.get(x);
                        if (c.getData().get(column)
                                .equalsIgnoreCase(s.getData()
                                        .get(column))) {

                            lastCorrelationId++;
                            c.add("Correlation ID", String.valueOf(lastCorrelationId));
                            s.add("Correlation ID", String.valueOf(lastCorrelationId));

                            diff.add(c);
                            diff.add(s);
                            //   added = true;
                            break;
                        }

                    }
                    //     if(added) continue;

                }
            }
        }


        System.out.println("------ REPORT -------");

        data.getColumns().forEach(e -> System.out.print(e + "\t"));
        System.out.println();
        for (int x = 0; x < diff.size(); x++) {
            for (int y = 0; y < data.getColumns().size(); y++) {
                System.out.print(diff
                        .get(x)
                        .getData()
                        .get(data
                                .getColumns()
                                .get(y)) + "\t\t");
            }
            System.out.println();
        }
    }

    public void write() {
        List<String> columns = data.getColumns();
        columns.add(0, "Correlation ID");
        toWrite.add(columns.toArray(String[]::new));
        for (int x = 0; x < diff.size(); x++) {
            List<String> temp = new ArrayList<>();
            for (int y = 0; y < data.getColumns().size(); y++) {
                temp.add(diff
                        .get(x)
                        .getData()
                        .get(data
                                .getColumns()
                                .get(y)));
            }
            toWrite.add(temp.toArray(String[]::new));
        }
        try {
            ReportWriter.writeReport(toWrite, Paths.get(data.getOutputDest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
