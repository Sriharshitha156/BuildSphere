package com.buildsphere.reporting.repository;

import com.buildsphere.reporting.model.WeeklyReport;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
    Optional<WeeklyReport> findByWeekStartAndWeekEnd(LocalDate weekStart, LocalDate weekEnd);
    List<WeeklyReport> findAllByOrderByWeekStartDesc();
}
