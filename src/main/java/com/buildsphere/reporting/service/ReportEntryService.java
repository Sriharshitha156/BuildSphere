package com.buildsphere.reporting.service;

import com.buildsphere.reporting.dto.EntryRequest;
import com.buildsphere.reporting.dto.EntryResponse;
import com.buildsphere.reporting.model.ReportEntry;
import com.buildsphere.reporting.model.User;
import com.buildsphere.reporting.model.WeeklyReport;
import com.buildsphere.reporting.repository.ReportEntryRepository;
import com.buildsphere.reporting.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReportEntryService {
    private final ReportEntryRepository reportEntryRepository;
    private final WeeklyReportService weeklyReportService;
    private final UserRepository userRepository;

    public ReportEntryService(ReportEntryRepository reportEntryRepository,
                              WeeklyReportService weeklyReportService,
                              UserRepository userRepository) {
        this.reportEntryRepository = reportEntryRepository;
        this.weeklyReportService = weeklyReportService;
        this.userRepository = userRepository;
    }

    public EntryResponse create(EntryRequest request) {
        WeeklyReport weeklyReport = weeklyReportService.getById(request.weeklyReportId());
        if (request.eventDate().isBefore(weeklyReport.getWeekStart()) || request.eventDate().isAfter(weeklyReport.getWeekEnd())) {
            throw new IllegalArgumentException("Event date must be inside selected weekly range");
        }

        User contributor = currentUser();
        LocalDateTime now = LocalDateTime.now();
        ReportEntry saved = reportEntryRepository.save(ReportEntry.builder()
                .weeklyReport(weeklyReport)
                .sectionType(request.sectionType())
                .eventDate(request.eventDate())
                .contentJson(request.contentJson())
                .contributedBy(contributor)
                .createdAt(now)
                .updatedAt(now)
                .build());

        return toResponse(saved);
    }

    public EntryResponse update(Long id, EntryRequest request) {
        ReportEntry entry = reportEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entry not found"));
        WeeklyReport weeklyReport = weeklyReportService.getById(request.weeklyReportId());
        if (request.eventDate().isBefore(weeklyReport.getWeekStart()) || request.eventDate().isAfter(weeklyReport.getWeekEnd())) {
            throw new IllegalArgumentException("Event date must be inside selected weekly range");
        }

        entry.setWeeklyReport(weeklyReport);
        entry.setSectionType(request.sectionType());
        entry.setEventDate(request.eventDate());
        entry.setContentJson(request.contentJson());
        entry.setUpdatedAt(LocalDateTime.now());
        return toResponse(reportEntryRepository.save(entry));
    }

    public void delete(Long id) {
        ReportEntry entry = reportEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entry not found"));
        reportEntryRepository.delete(entry);
    }

    public List<EntryResponse> listByWeeklyReport(Long weeklyReportId) {
        return reportEntryRepository.findByWeeklyReportIdOrderBySectionTypeAscCreatedAtAsc(weeklyReportId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private EntryResponse toResponse(ReportEntry entry) {
        return new EntryResponse(
                entry.getId(),
                entry.getWeeklyReport().getId(),
                entry.getSectionType(),
                entry.getEventDate(),
                entry.getContentJson(),
                entry.getContributedBy().getId(),
                entry.getContributedBy().getFullName(),
                entry.getCreatedAt(),
                entry.getUpdatedAt()
        );
    }

    private User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalArgumentException("Authentication required");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
    }
}
