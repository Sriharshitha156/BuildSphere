package com.buildsphere.reporting.service;

import com.buildsphere.reporting.dto.WeekReportRequest;
import com.buildsphere.reporting.dto.WeekReportResponse;
import com.buildsphere.reporting.model.SectionType;
import com.buildsphere.reporting.model.User;
import com.buildsphere.reporting.model.WeeklyReport;
import com.buildsphere.reporting.repository.ReportEntryRepository;
import com.buildsphere.reporting.repository.UserRepository;
import com.buildsphere.reporting.repository.WeeklyReportRepository;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WeeklyReportService {
    private final WeeklyReportRepository weeklyReportRepository;
    private final UserRepository userRepository;
    private final ReportEntryRepository reportEntryRepository;

    public WeeklyReportService(WeeklyReportRepository weeklyReportRepository,
                               UserRepository userRepository,
                               ReportEntryRepository reportEntryRepository) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.userRepository = userRepository;
        this.reportEntryRepository = reportEntryRepository;
    }

    public WeekReportResponse create(WeekReportRequest request) {
        if (request.weekEnd().isBefore(request.weekStart())) {
            throw new IllegalArgumentException("weekEnd cannot be before weekStart");
        }
        WeeklyReport existing = weeklyReportRepository
                .findByWeekStartAndWeekEnd(request.weekStart(), request.weekEnd())
                .orElse(null);
        if (existing != null) {
            return toResponse(existing);
        }
        User user = currentUser();
        WeeklyReport saved = weeklyReportRepository.save(WeeklyReport.builder()
                .weekStart(request.weekStart())
                .weekEnd(request.weekEnd())
                .status("IN_PROGRESS")
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build());
        return toResponse(saved);
    }

    public List<WeekReportResponse> list() {
        return weeklyReportRepository.findAllByOrderByWeekStartDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public WeeklyReport getById(Long id) {
        return weeklyReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Weekly report not found"));
    }

    public WeekReportResponse toResponse(WeeklyReport report) {
        Map<SectionType, Long> counts = new EnumMap<>(SectionType.class);
        for (SectionType section : SectionType.values()) {
            counts.put(section, reportEntryRepository.countByWeeklyReportIdAndSectionType(report.getId(), section));
        }
        Map<String, String> sectionStatus = new LinkedHashMap<>();
        for (SectionType section : SectionType.values()) {
            sectionStatus.put(section.name(), counts.get(section) > 0 ? "COMPLETE" : "PENDING");
        }
        return new WeekReportResponse(report.getId(), report.getWeekStart(), report.getWeekEnd(), report.getStatus(), sectionStatus);
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
