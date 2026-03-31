package com.buildsphere.reporting.dto;

import com.buildsphere.reporting.model.SectionType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EntryResponse(
        Long id,
        Long weeklyReportId,
        SectionType sectionType,
        LocalDate eventDate,
        String contentJson,
        Long contributedById,
        String contributedByName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
