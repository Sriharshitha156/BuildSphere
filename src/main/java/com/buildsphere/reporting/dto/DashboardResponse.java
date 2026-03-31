package com.buildsphere.reporting.dto;

import java.util.Map;

public record DashboardResponse(
        Long weeklyReportId,
        long totalEntries,
        Map<String, Long> entriesBySection,
        Map<String, String> sectionCompletion
) {
}
