package com.buildsphere.reporting.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record WeekReportRequest(
        @NotNull LocalDate weekStart,
        @NotNull LocalDate weekEnd
) {
}
