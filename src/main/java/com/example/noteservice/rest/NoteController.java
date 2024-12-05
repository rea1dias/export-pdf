package com.example.noteservice.rest;

import com.example.noteservice.model.NoteDto;
import com.example.noteservice.service.NoteService;
import com.example.noteservice.service.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService service;
    private final PdfExportService pdfService;

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto dto) {
        try {
            NoteDto createdNote = service.createNote(dto);
            return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteDto>> getNote(@RequestParam String title) {
        try {
            List<NoteDto> dtos = service.getNote(title);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable long id, @RequestBody NoteDto dto) {
        try {
            NoteDto updatedNote = service.updateNote(id, dto);
            return new ResponseEntity<>(updatedNote, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<NoteDto> deleteNote(@PathVariable long id) {
        try {
            service.deleteNote(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/export/pdf")
    public void exportPdf(HttpServletResponse response) {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=notes.pdf");
        try {
            pdfService.exportPdf(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred during PDF export", e);

        }
    }

}
