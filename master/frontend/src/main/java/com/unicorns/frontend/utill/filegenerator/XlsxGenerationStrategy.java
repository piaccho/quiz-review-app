package com.unicorns.frontend.utill.filegenerator;

import com.unicorns.frontend.config.Config;
import com.unicorns.frontend.model.Prize;
import com.unicorns.frontend.model.Quiz;
import com.unicorns.frontend.model.QuizEntry;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class XlsxGenerationStrategy implements FileGenerationStrategy {

    private final String fileExtension = Config.getProperty("file_export_xlsx.extensions");

    private final String fileName = Config.getProperty("file_export_xlsx.descriptions");



    public void generateFile(List<QuizEntry> data, String path)
    {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Quiz Entries");
            Map<String, CellStyle> prizeStyles = new HashMap<>();
            Random rand = new Random();

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nickname");
            headerRow.createCell(1).setCellValue("Total Points");
            headerRow.createCell(2).setCellValue("Time Difference");
            headerRow.createCell(3).setCellValue("Prize");


            sheet.setColumnWidth(0, 256 * 20);
            sheet.setColumnWidth(1, 256 * 15);
            sheet.setColumnWidth(2, 256 * 20);
            sheet.setColumnWidth(3, 256 * 25);


            int rowNum = 1;
            for (QuizEntry entry : data) {
                Row row = sheet.createRow(rowNum++);
                CellStyle style;

                Prize prize = entry.getPrize();
                if (prize != null) {
                    String prizeName = prize.getName();
                    if (!prizeStyles.containsKey(prizeName)) {

                        byte[] rgb = new byte[3];
                        rand.nextBytes(rgb);
                        XSSFColor color = new XSSFColor(rgb, null);


                        style = workbook.createCellStyle();
                        style.setFillForegroundColor(color);
                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        prizeStyles.put(prizeName, style);
                    }
                    style = prizeStyles.get(prizeName);
                } else {

                    style = workbook.createCellStyle();
                }


                createCell(row, 0, entry.getNickname(), style);
                createCell(row, 1, String.valueOf(entry.getTotalPoints()), style);
                createCell(row, 2, String.valueOf(entry.getTimeDifferenceInSeconds()), style);
                createCell(row, 3, prize != null ? prize.getName() : "N/A", style);
            }

            System.out.println(path);
            try (FileOutputStream outputStream = new FileOutputStream(path)) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getFileName() {
        return fileName;
    }


}
