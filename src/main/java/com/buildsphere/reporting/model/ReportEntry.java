package com.buildsphere.reporting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report_entries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "weekly_report_id")
    private WeeklyReport weeklyReport;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectionType sectionType;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String contentJson;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contributed_by")
    private User contributedBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
