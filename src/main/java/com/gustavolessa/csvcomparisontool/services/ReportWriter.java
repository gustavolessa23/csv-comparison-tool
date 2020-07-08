package com.gustavolessa.csvcomparisontool.services;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReportWriter {

    public static void writeReport(List<String[]> report, Path path) throws Exception {

        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));
        writer.writeAll(report);

        writer.close();
    }
}
