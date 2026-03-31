package com.buildsphere.reporting.service;

import com.buildsphere.reporting.dto.DashboardResponse;
import com.buildsphere.reporting.model.ReportEntry;
import com.buildsphere.reporting.model.SectionType;
import com.buildsphere.reporting.repository.ReportEntryRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final ReportEntryRepository reportEntryRepository;

    public DashboardService(ReportEntryRepository reportEntryRepository) {
        this.reportEntryRepository = reportEntryRepository;
    }

    public DashboardResponse getWeeklyDashboard(Long weeklyReportId) {
        List<ReportEntry> entries = reportEntryRepository.findByWeeklyReportIdOrderBySectionTypeAscCreatedAtAsc(weeklyReportId);
        Map<String, Long> bySection = new LinkedHashMap<>();
        Map<String, String> completion = new LinkedHashMap<>();

        for (SectionType section : SectionType.values()) {
            long count = entries.stream().filter(e -> e.getSectionType() == section).count();
            bySection.put(section.name(), count);
            completion.put(section.name(), count > 0 ? "COMPLETE" : "PENDING");
        }
        return new DashboardResponse(weeklyReportId, entries.size(), bySection, completion);
    }
}
