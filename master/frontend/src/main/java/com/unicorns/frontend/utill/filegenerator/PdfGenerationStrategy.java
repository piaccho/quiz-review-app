package com.unicorns.frontend.utill.filegenerator;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.unicorns.frontend.config.Config;
import com.unicorns.frontend.model.Quiz;
import com.unicorns.frontend.model.QuizEntry;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PdfGenerationStrategy implements FileGenerationStrategy {

    private final String fileExtension = Config.getProperty("file_export_pdf.extensions");

    private final String fileName = Config.getProperty("file_export_pdf.descriptions");



    public void generateFile(List<QuizEntry> data, String path)
    {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(path));
            Document document = new Document(pdfDocument);
            float columnWidth = 100;
            Table table = new Table(new float[]{columnWidth, columnWidth, columnWidth, columnWidth});
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);


            table.addCell(new Cell().add(new Paragraph("Nickname")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Total Points")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Time Difference")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addCell(new Cell().add(new Paragraph("Prize")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            Map<String, DeviceRgb> prizeColors = new HashMap<>();
            Random random = new Random();

            for (QuizEntry entry : data) {
                DeviceRgb color;
                if (entry.getPrize() != null && entry.getPrize().getName() != null) {
                    String prizeName = entry.getPrize().getName();
                    if (!prizeColors.containsKey(prizeName)) {
                        color = new DeviceRgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                        prizeColors.put(prizeName, color);
                    } else {
                        color = prizeColors.get(prizeName);
                    }
                } else {
                    color = new DeviceRgb(226, 226, 226);
                }

                table.addCell(new Cell().add(new Paragraph(entry.getNickname())).setBackgroundColor(color));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getTotalPoints()))).setBackgroundColor(color));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getTimeDifferenceInSeconds()))).setBackgroundColor(color));
                table.addCell(new Cell().add(new Paragraph(entry.getPrize() != null ? entry.getPrize().getName() : "N/A")).setBackgroundColor(color));
            }

            document.add(table);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getFileName() {
        return fileName;
    }

}
