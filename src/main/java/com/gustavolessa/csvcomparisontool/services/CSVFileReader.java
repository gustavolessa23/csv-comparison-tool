package com.gustavolessa.csvcomparisontool.services;

import com.gustavolessa.csvcomparisontool.data.Data;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CSVFileReader {

    @Autowired
    private Data data;
    private Reader reader;
    private CSVReader csvReader;
    private String[] record;
    private Path src;

    public CSVFileReader() {

    }

    public void setSrc(String path) throws FileNotFoundException {
        this.src = Paths.get(path);
        if (!Files.exists(src))
            throw new FileNotFoundException("CSV file not found at " + src.toString());
    }

    public void init() {
        try {
            // create a reader
            reader = Files.newBufferedReader(src);
            // create csv reader
            csvReader = new CSVReader(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        try {
            // read and add columns
            data.setColumns(csvReader.readNext());

            // read and add all lines, one at a time.
            while ((record = csvReader.readNext()) != null) {
                data.addLine(record);
            }
            System.out.println();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } finally {
            data.removeDuplicatesFromDatasets();
        }
    }

    public void close() {
        // close readers
        try {
            csvReader.close();
            reader.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


}
