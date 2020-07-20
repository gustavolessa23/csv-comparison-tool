package com.gustavolessa.csvcomparisontool.services;

import com.gustavolessa.csvcomparisontool.data.Data;
import com.gustavolessa.csvcomparisontool.entities.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;


public class AppRunner {

    private static final Logger LOG = LoggerFactory
            .getLogger(AppRunner.class);

    @Autowired
    private Data data;


//    @Autowired
//    private ArgsHandler argsHandler;
//
//    public void start(ApplicationArguments args) {
//
//        try {
//            // get arguments and save to variables
//            argsHandler.setArgs(args);
//            // read data file
//            data.readFile();
//            // create new report using Builder
//            Report report = Report.ReportBuilder.aReport()
//                    .withColumnsToCompare(data.getColumnsToCompare())
//                    .withDataset1(data.getDataset1())
//                    .withDataset2(data.getDataset2())
//                    .withKeyColumnsList(data.getKeyColumns())
//                    .withAllColumns(data.getColumns())
//                    .build();
//            // write report to file
//            ReportWriter.writeToExcel(report, data.getOutputDest());
//            // print output file path
////            System.out.println("Dest: " + data.getOutputDest());
//
//        } catch (IllegalArgumentException e) {
//            LOG.error(e.getMessage());
//        } catch (FileNotFoundException e) {
//            LOG.error(e.getMessage());
//        }
//
//        // --src="C:\Users\Gustavo Lessa\OneDrive - Neueda\CSV Tool\sampledata.csv" --dest="C:\Users\Gustavo Lessa\OneDrive - Neueda\CSV Tool\output" --system="Platform" --options="FC,CV"  --key="AccountNumber" --key="AccountNumber,Dim1" --compare="Dim2,Dim3,Rate" csv
//    }

    public String startFromUI() {

        //boolean done = false;
        String outputFilePath = "";

        try {
            // read data file
            data.readFile();
            // create new report using Builder
            Report report = Report.ReportBuilder.aReport()
                    .withColumnsToCompare(data.getColumnsToCompare())
                    .withDataset1(data.getDataset1())
                    .withDataset2(data.getDataset2())
                    .withKeyColumnsList(data.getKeyColumns())
                    .withAllColumns(data.getColumns())
                    .build();
            // write report to file
            outputFilePath = ReportWriter.writeToExcel(report, data.getOutputDest());
            // print output file path
//            System.out.println("Dest: " + data.getOutputDest());

        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
        } catch (FileNotFoundException | AccessDeniedException e) {
            LOG.error(e.getMessage());
        }
        return outputFilePath;
        // --src="C:\Users\Gustavo Lessa\OneDrive - Neueda\CSV Tool\sampledata.csv" --dest="C:\Users\Gustavo Lessa\OneDrive - Neueda\CSV Tool\output" --system="Platform" --options="FC,CV"  --key="AccountNumber" --key="AccountNumber,Dim1" --compare="Dim2,Dim3,Rate" csv
    }
}
