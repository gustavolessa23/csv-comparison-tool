package com.gustavolessa.csvcomparisontool.services;

import com.gustavolessa.csvcomparisontool.entities.CSVReportEntry;
import com.opencsv.CSVWriter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static void writeToExcel(List<String> columns, List<List<List<String>>> content) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Report");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        //font.setBold(true);
        headerStyle.setFont(font);

        CellStyle regularStyle = workbook.createCellStyle();
        regularStyle.setWrapText(true);

        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setWrapText(true);
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        for (int i = 0; i < columns.size(); i++) {
            sheet.setColumnWidth(i, 1500);
        }

        int rowCounter = 0;
        for (List<List<String>> contentList : content) {

            Row header = sheet.createRow(rowCounter++);

            for (int x = 0; x < columns.size(); x++) {
                Cell headerCell = header.createCell(x);
                headerCell.setCellValue(columns.get(x));
                headerCell.setCellStyle(headerStyle);
            }

            for (int j = 0; j < contentList.size(); j++) {
                Row row = sheet.createRow(rowCounter++);

                for (int y = 0; y < contentList.get(j).size(); y++) {
                    Cell cell = row.createCell(y);
                    cell.setCellValue(contentList.get(j).get(y));
                    cell.setCellStyle(regularStyle);

                    if (j == contentList.size() - 1) {
                        cell.setCellStyle(borderStyle);
                    } else if (j < contentList.size() - 1 &&
                            y <= contentList.get(j).size() - 1) {
                        if (!contentList.get(j + 1).get(0)
                                .equalsIgnoreCase(contentList.get(j).get(0))) {
                            cell.setCellStyle(borderStyle);
                        }
                    }
                }

            }
            rowCounter++;

        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "tempaqui.xlsx";
        System.out.println("Saved at: " + fileLocation);

        try {
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeToExcel2(List<String> columns, List<List<CSVReportEntry>> diff) {


        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Report");
        //   sheet.setDefaultColumnWidth(1500);

        // Header style
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        // Font style
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        //font.setBold(true);
        headerStyle.setFont(font);

        // Cell with no border
        CellStyle regularStyle = workbook.createCellStyle();
        regularStyle.setBorderBottom(BorderStyle.THIN);
        regularStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        // Cell with no border alternate
        CellStyle regularAlternateStyle = workbook.createCellStyle();
        regularAlternateStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        regularAlternateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        regularAlternateStyle.setBorderBottom(BorderStyle.THIN);
        regularAlternateStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //  regularStyle.setWrapText(true);

        // Cell with bottom border
        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(BorderStyle.MEDIUM);
        borderStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // borderStyle.setWrapText(true);

        // Cell with border alternate
        CellStyle borderAlternateStyle = workbook.createCellStyle();
        borderAlternateStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        borderAlternateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        borderAlternateStyle.setBorderBottom(BorderStyle.MEDIUM);
        borderAlternateStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        // Conflicting cell style no border
        CellStyle conflictBorderStyle = workbook.createCellStyle();
        conflictBorderStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        conflictBorderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        conflictBorderStyle.setBorderBottom(BorderStyle.MEDIUM);
        conflictBorderStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //conflictBorderStyle.setWrapText(true);

        // Conflicting cell style with border
        CellStyle conflictPlainStyle = workbook.createCellStyle();
        conflictPlainStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        conflictPlainStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        conflictPlainStyle.setBorderBottom(BorderStyle.THIN);
        conflictPlainStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // conflictPlainStyle.setWrapText(true);

        int rowCounter = 0;

        // for each run set using --key parameter
        for (int i = 0; i < diff.size(); i++) {
            List<CSVReportEntry> run = diff.get(i);

            // render header
            Row header = sheet.createRow(rowCounter++);

            for (int x = 0; x < columns.size(); x++) {
                Cell headerCell = header.createCell(x);
                headerCell.setCellValue(columns.get(x));
                headerCell.setCellStyle(headerStyle);
            }

            //  render content
            for (int j = 0; j < run.size(); j++) {
                CSVReportEntry entry = run.get(j);
                boolean isAlternateCell = false;

                Row row = sheet.createRow(rowCounter++);
                Cell idCell = row.createCell(0);
                long id = entry.getId();
                idCell.setCellValue(String.valueOf(id));
                idCell.setCellStyle(regularStyle);


                if (id % 2 == 0) {
                    isAlternateCell = true;
                }

                //System.out.println("ID: "+id+"\t Alternate: "+isAlternateCell);


//                if(id % 2 == 0){
//                    idCell.setCellStyle(regularAlternateStyle);
//                }else{
//                    idCell.setCellStyle(regularStyle);
//                }

                for (int y = 1; y < columns.size(); y++) {

                    Cell cell = row.createCell(y);

                    cell.setCellValue(run
                            .get(j)
                            .getData()
                            .get(columns
                                    .get(y)));
                    cell.setCellStyle(regularStyle);

                    if (j == run.size() - 1) {
//                        if(isAlternateCell){
//                            cell.setCellStyle(borderAlternateStyle);
//                            idCell.setCellStyle(borderAlternateStyle);
//                        }else{
                        cell.setCellStyle(borderStyle);
                        idCell.setCellStyle(borderStyle);
//                        }
                    } else if (j < run.size() - 1) {
                        if (run.get(j + 1).getId() != run.get(j).getId()) {
//                            if(isAlternateCell){
//                                cell.setCellStyle(borderAlternateStyle);
//                                idCell.setCellStyle(borderAlternateStyle);
//                            }else{
                            cell.setCellStyle(borderStyle);
                            idCell.setCellStyle(borderStyle);
//                            }
                        }
                    }


                    if (entry.getConflicting().contains(columns.get(y))) {
                        if (cell.getCellStyle().equals(borderStyle) ||
                                cell.getCellStyle().equals(borderAlternateStyle)) {
                            cell.setCellStyle(conflictBorderStyle);
                        } else {
                            cell.setCellStyle(conflictPlainStyle);
                        }
                    }

                    if (isAlternateCell) {
                        if (idCell.getCellStyle().equals(regularStyle)) {
                            idCell.setCellStyle(regularAlternateStyle);
                        } else if (idCell.getCellStyle().equals(borderStyle)) {
                            idCell.setCellStyle(borderAlternateStyle);
                        }


                        if (cell.getCellStyle().equals(regularStyle)) {
                            cell.setCellStyle(regularAlternateStyle);

                        } else if (cell.getCellStyle().equals(borderStyle)) {
                            cell.setCellStyle(borderAlternateStyle);
                        }
                    }

                }
                isAlternateCell = false;
            }
            rowCounter++;

        }

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
        System.out.println("Saved at: " + fileLocation);

        try {
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static CellStyle highlight(CellStyle style) {
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
