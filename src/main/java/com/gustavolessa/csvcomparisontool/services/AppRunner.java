package com.gustavolessa.csvcomparisontool.services;

import com.gustavolessa.csvcomparisontool.data.Data;
import com.gustavolessa.csvcomparisontool.entities.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

import java.io.FileNotFoundException;


public class AppRunner {

    private static final Logger LOG = LoggerFactory
            .getLogger(AppRunner.class);

    @Autowired
    private Data data;

    @Autowired
    private Report report;

    @Autowired
    private ArgsHandler argsHandler;


    public void start(ApplicationArguments args) {

        // get arguments and save to variables
        try {
            argsHandler.setArgs(args);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
        }

        // read data file
        try {
            data.readFile();
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage());
        }

        // generate report
        report.generate(
                data.getDataset1(),
                data.getDataset2(),
                data.getKeyColumns(),
                data.getColumnsToCompare()
        );

        // write report
        report.write(data.getColumns(), data.getOutputDest());
        report.writeExcel(data.getColumns());
    }
}
