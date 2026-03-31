package com.buildsphere.reporting.dto;

import com.buildsphere.reporting.model.SectionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EntryRequest(
        @NotNull Long weeklyReportId,
        @NotNull SectionType sectionType,
        @NotNull LocalDate eventDate,
        @NotBlank String contentJson
) {
}
