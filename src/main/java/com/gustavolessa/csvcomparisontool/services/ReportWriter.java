package com.gustavolessa.csvcomparisontool.services;

import com.gustavolessa.csvcomparisontool.entities.CSVReportRow;
import com.gustavolessa.csvcomparisontool.entities.Report;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportWriter {

    public static String writeToExcel(Report report, String path) {

        List<String> columns = report.getAllColumns();
        List<List<CSVReportRow>> diff = report.getDiff();
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Report");
        //   sheet.setDefaultColumnWidth(1500);

        // Header style

        String rgbS = "B4C6E7";
        byte[] rgbB = new byte[3]; // get byte array from hex string
        try {
            rgbB = Hex.decodeHex(rgbS);
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        // Font style
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 10);
        headerStyle.setFont(font);

        // Cell with no border
        CellStyle regularStyle = workbook.createCellStyle();
        regularStyle.setBorderBottom(BorderStyle.THIN);
        regularStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        regularStyle.setFont(font);

        // Cell with no border alternate
        CellStyle regularAlternateStyle = workbook.createCellStyle();
        regularAlternateStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        regularAlternateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        regularAlternateStyle.setBorderBottom(BorderStyle.THIN);
        regularAlternateStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        regularAlternateStyle.setFont(font);

        // Cell with bottom border
        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(BorderStyle.MEDIUM);
        borderStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        borderStyle.setFont(font);

        // Cell with bottom and top borders
        CellStyle columnsListStyle = workbook.createCellStyle();
        columnsListStyle.setBorderTop(BorderStyle.MEDIUM);
        columnsListStyle.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        columnsListStyle.setBorderBottom(BorderStyle.MEDIUM);
        columnsListStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        columnsListStyle.setFont(font);

        // Cell with border alternate
        CellStyle borderAlternateStyle = workbook.createCellStyle();
        borderAlternateStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        borderAlternateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        borderAlternateStyle.setBorderBottom(BorderStyle.MEDIUM);
        borderAlternateStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        borderAlternateStyle.setFont(font);

        // Conflicting cell style with border
        CellStyle conflictBorderStyle = workbook.createCellStyle();
        conflictBorderStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        conflictBorderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        conflictBorderStyle.setBorderBottom(BorderStyle.MEDIUM);
        conflictBorderStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        conflictBorderStyle.setFont(font);

        // Conflicting cell style with no border
        CellStyle conflictPlainStyle = workbook.createCellStyle();
        conflictPlainStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        conflictPlainStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        conflictPlainStyle.setBorderBottom(BorderStyle.THIN);
        conflictPlainStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        conflictPlainStyle.setFont(font);

        int rowCounter = 0;

        // for each run set using --key parameter
        for (int i = 0; i < diff.size(); i++) {
            List<CSVReportRow> run = diff.get(i);

            // create row to display which key columns were considered
            StringBuilder sb = new StringBuilder("Key columns: ");
            for (int j = 0; j < report.getKeyColumnsList().get(i).size(); j++) {
                sb.append(report.getKeyColumnsList().get(i).get(j));
                if (j != report.getKeyColumnsList().get(i).size() - 1) {
                    sb.append(", ");
                }
            }
            Row keysRow = sheet.createRow(rowCounter++);
            Cell keysCell = keysRow.createCell(0);
            keysCell.setCellStyle(regularStyle);
            keysCell.setCellValue(sb.toString());
            sheet.addMergedRegion(new CellRangeAddress(rowCounter - 1, rowCounter - 1, 0, report.getAllColumns().size() - 1));


            // create row to display which columns were compared
            sb = new StringBuilder("Columns compared: ");
            for (int j = 0; j < report.getColumnsToCompare().size(); j++) {
                sb.append(report.getColumnsToCompare().get(j));
                if (j != report.getColumnsToCompare().size() - 1) {
                    sb.append(", ");
                }
            }
            Row columnsRow = sheet.createRow(rowCounter++);
            Cell columnsCell = columnsRow.createCell(0);
            columnsCell.setCellStyle(regularStyle);
            columnsCell.setCellValue(sb.toString());
            sheet.addMergedRegion(new CellRangeAddress(rowCounter - 1, rowCounter - 1, 0, report.getAllColumns().size() - 1));


            // render header
            Row header = sheet.createRow(rowCounter++);

            for (int x = 0; x < columns.size(); x++) {
                Cell headerCell = header.createCell(x);
                headerCell.setCellValue(columns.get(x));
                headerCell.setCellStyle(headerStyle);
            }

            //  render content
            for (int j = 0; j < run.size(); j++) {
                CSVReportRow entry = run.get(j);
                boolean isAlternateCell = false;

                Row row = sheet.createRow(rowCounter++);
                Cell idCell = row.createCell(0);
                long id = entry.getId();
                idCell.setCellValue(String.valueOf(id));
                idCell.setCellStyle(regularStyle);

                if (id % 2 == 0) isAlternateCell = true;

                for (int y = 1; y < columns.size(); y++) {

                    Cell cell = row.createCell(y);

                    cell.setCellValue(run
                            .get(j)
                            .getData()
                            .get(columns
                                    .get(y)));
                    cell.setCellStyle(regularStyle);

                    if (j == run.size() - 1) {
                        cell.setCellStyle(borderStyle);
                        idCell.setCellStyle(borderStyle);
                    } else if (j < run.size() - 1) {
                        if (run.get(j + 1).getId() != run.get(j).getId()) {
                            cell.setCellStyle(borderStyle);
                            idCell.setCellStyle(borderStyle);
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

        setBordersToMergedCells(sheet);
        return saveWorkbookToFile(workbook, path);
    }

    private static void setBordersToMergedCells(Sheet sheet) {
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        for (CellRangeAddress rangeAddress : mergedRegions) {
            RegionUtil.setBorderTop(BorderStyle.MEDIUM, rangeAddress, sheet);
            RegionUtil.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex(), rangeAddress, sheet);
            RegionUtil.setBorderBottom(BorderStyle.MEDIUM, rangeAddress, sheet);
            RegionUtil.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex(), rangeAddress, sheet);

        }
    }

    private static String saveWorkbookToFile(Workbook workbook, String path) {
        boolean done = true;

        if (!path.endsWith(File.separator)) {
            path = path.concat(File.separator);
        }

        path = path.concat(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())).concat(".xlsx");
        Path output = Paths.get(path);

        try {
            Files.createDirectories(output.getParent());
            Files.createFile(output);
        } catch (Exception e) {
            e.printStackTrace();
            done = false;
        }

        System.out.println("Saved at: " + output.toString());

        try {
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream(output.toString());
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            done = false;
            e.printStackTrace();
        } catch (IOException e) {
            done = false;
            e.printStackTrace();
        }
        if (!done)
            return "";
        else
            return output.toString();
    }

}
