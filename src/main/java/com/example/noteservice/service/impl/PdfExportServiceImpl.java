package com.example.noteservice.service.impl;

import com.example.noteservice.entity.Note;
import com.example.noteservice.repository.NoteRepository;
import com.example.noteservice.service.PdfExportService;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfExportServiceImpl implements PdfExportService {

    private final NoteRepository repository;

    @Override
    public void exportPdf(HttpServletResponse response) throws Exception, DocumentException {

        List<Note> notes = repository.findAll();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(16);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Notes: ", font);
        p.setAlignment(Element.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 5.0f, 2.0f, 3.0f, 3.0f, 3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table, notes);

        document.add(table);

        document.close();
    }

    private void writeTableHeader(PdfPTable table) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.YELLOW);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);

        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Note ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Title", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Content", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Category", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Created At", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Updated At", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Reminder Time", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, List<Note> notes) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Note note : notes) {
            table.addCell(String.valueOf(note.getId()));
            table.addCell(note.getTitle());
            table.addCell(note.getContent());
            table.addCell(note.getCategory().toString());
            table.addCell(note.getCreatedAt().toString());
            table.addCell(note.getUpdatedAt().toString());
            if (note.getReminderTime() != null) {
                table.addCell(note.getReminderTime().format(formatter));
            } else {
                table.addCell("N/A");
            }
        }
    }
}
