package com.buildsphere.reporting.repository;

import com.buildsphere.reporting.model.ReportEntry;
import com.buildsphere.reporting.model.SectionType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportEntryRepository extends JpaRepository<ReportEntry, Long> {
    List<ReportEntry> findByWeeklyReportIdOrderBySectionTypeAscCreatedAtAsc(Long weeklyReportId);
    long countByWeeklyReportIdAndSectionType(Long weeklyReportId, SectionType sectionType);
}
