package com.gustavolessa.csvcomparisontool;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;

public class ReportWriter {

    public static void writeReport(List<String[]> report, Path path) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));
        writer.writeAll(report);

        writer.close();
//        return Helpers.readFile(path);
    }
}
