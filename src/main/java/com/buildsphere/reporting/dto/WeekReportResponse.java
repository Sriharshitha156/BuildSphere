package com.buildsphere.reporting.dto;

import java.time.LocalDate;
import java.util.Map;

public record WeekReportResponse(
        Long id,
        LocalDate weekStart,
        LocalDate weekEnd,
        String status,
        Map<String, String> sectionStatus
) {
}
