package com.gustavolessa.csvcomparisontool;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Report {

    private final List<CSVEntry> diff;
    @Autowired
    private Data data;

    public Report() {
        diff = new ArrayList<>();
    }

    public void generate() {
        /*
        for each entry of dataset1, check if there an entry in dataset2 with the same account number
         */
        //For each entry in dataset1
        for (CSVEntry c : data.getDataset1()) {
            // for each entry in dataset2
            for (CSVEntry s : data.getDataset2()) {

                //    boolean added = false;
                //if account number of entry c = account number of entry s
                if (c.getData().get(data.getSameColumn())
                        .equalsIgnoreCase(s.getData().get(data.getSameColumn()))) {

                    for (int x = 0; x < data.getColumnsToCompare().size(); x++) {
                        if (!c.getData().get(data.getColumnsToCompare().get(x))
                                .equalsIgnoreCase(s.getData()
                                        .get(data.getColumnsToCompare().get(x)))) {

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
}
