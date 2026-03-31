package com.buildsphere.reporting.controller;

import com.buildsphere.reporting.dto.EntryRequest;
import com.buildsphere.reporting.dto.EntryResponse;
import com.buildsphere.reporting.dto.WeekReportRequest;
import com.buildsphere.reporting.dto.WeekReportResponse;
import com.buildsphere.reporting.service.ReportEntryService;
import com.buildsphere.reporting.service.ReportExportService;
import com.buildsphere.reporting.service.WeeklyReportService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class WeeklyReportController {
    private final WeeklyReportService weeklyReportService;
    private final ReportEntryService reportEntryService;
    private final ReportExportService reportExportService;

    public WeeklyReportController(WeeklyReportService weeklyReportService,
                                  ReportEntryService reportEntryService,
                                  ReportExportService reportExportService) {
        this.weeklyReportService = weeklyReportService;
        this.reportEntryService = reportEntryService;
        this.reportExportService = reportExportService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COORDINATOR','ADMIN')")
    public ResponseEntity<WeekReportResponse> createWeeklyReport(@Valid @RequestBody WeekReportRequest request) {
        return ResponseEntity.ok(weeklyReportService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<WeekReportResponse>> listWeeklyReports() {
        return ResponseEntity.ok(weeklyReportService.list());
    }

    @PostMapping("/entries")
    @PreAuthorize("hasAnyRole('FACULTY','COORDINATOR','ADMIN')")
    public ResponseEntity<EntryResponse> createEntry(@Valid @RequestBody EntryRequest request) {
        return ResponseEntity.ok(reportEntryService.create(request));
    }

    @PutMapping("/entries/{entryId}")
    @PreAuthorize("hasAnyRole('FACULTY','COORDINATOR','ADMIN')")
    public ResponseEntity<EntryResponse> updateEntry(@PathVariable Long entryId, @Valid @RequestBody EntryRequest request) {
        return ResponseEntity.ok(reportEntryService.update(entryId, request));
    }

    @DeleteMapping("/entries/{entryId}")
    @PreAuthorize("hasAnyRole('FACULTY','COORDINATOR','ADMIN')")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long entryId) {
        reportEntryService.delete(entryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/entries")
    public ResponseEntity<List<EntryResponse>> listEntries(@RequestParam Long weeklyReportId) {
        return ResponseEntity.ok(reportEntryService.listByWeeklyReport(weeklyReportId));
    }

    @GetMapping("/{weeklyReportId}/export")
    @PreAuthorize("hasAnyRole('COORDINATOR','ADMIN')")
    public ResponseEntity<byte[]> exportWeeklyReport(@PathVariable Long weeklyReportId,
                                                     @RequestParam String format) {
        String normalized = format == null ? "" : format.trim().toLowerCase();
        byte[] payload;
        String filename;
        MediaType mediaType;
        if ("pdf".equals(normalized)) {
            payload = reportExportService.exportPdf(weeklyReportId);
            filename = "weekly-report-" + weeklyReportId + ".pdf";
            mediaType = MediaType.APPLICATION_PDF;
        } else if ("docx".equals(normalized)) {
            payload = reportExportService.exportDocx(weeklyReportId);
            filename = "weekly-report-" + weeklyReportId + ".docx";
            mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        } else {
            throw new IllegalArgumentException("Unsupported export format. Use pdf or docx.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .body(payload);
    }
}
