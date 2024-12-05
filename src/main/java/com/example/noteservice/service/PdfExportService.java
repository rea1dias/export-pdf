package com.example.noteservice.service;

import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;

public interface PdfExportService {
    void exportPdf(HttpServletResponse response) throws Exception, DocumentException;
}
