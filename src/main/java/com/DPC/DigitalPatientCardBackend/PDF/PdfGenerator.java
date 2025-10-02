package com.DPC.DigitalPatientCardBackend.PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.OutputStream;
import java.util.List;

public class PdfGenerator {

    public static void generatePdf(OutputStream out, String title, List<String> paragraphs, List<List<String>> tableData) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, out);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Font tableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        // Title
        if (title != null && !title.isEmpty()) {
            Paragraph p = new Paragraph(title, titleFont);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(12);
            document.add(p);

            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));
        }

        // Paragraphs
        if (paragraphs != null) {
            for (String line : paragraphs) {
                Paragraph p = new Paragraph(line, normal);
                p.setSpacingBefore(6);
                p.setSpacingAfter(6);
                document.add(p);
            }
        }

        // Table
        if (tableData != null && !tableData.isEmpty()) {
            int cols = tableData.get(0).size();
            PdfPTable table = new PdfPTable(cols);
            table.setWidthPercentage(100);
            table.setSpacingBefore(12);

            // Header
            List<String> headerRow = tableData.get(0);
            for (String cell : headerRow) {
                PdfPCell headerCell = new PdfPCell(new Phrase(cell, tableHeader));
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell.setPadding(6);
                table.addCell(headerCell);
            }

            // Data
            for (int i = 1; i < tableData.size(); i++) {
                for (String cellValue : tableData.get(i)) {
                    PdfPCell c = new PdfPCell(new Phrase(cellValue == null ? "" : cellValue, normal));
                    c.setPadding(6);
                    table.addCell(c);
                }
            }

            document.add(table);
        }

        document.close();
    }
}
