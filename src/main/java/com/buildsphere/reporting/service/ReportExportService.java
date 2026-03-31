package com.buildsphere.reporting.service;

import com.buildsphere.reporting.model.ReportEntry;
import com.buildsphere.reporting.model.SectionType;
import com.buildsphere.reporting.model.WeeklyReport;
import com.buildsphere.reporting.repository.ReportEntryRepository;
import com.buildsphere.reporting.repository.WeeklyReportRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

@Service
public class ReportExportService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private static final PDType1Font PDF_FONT = PDType1Font.HELVETICA;
    private static final float PDF_FONT_SIZE = 11f;
    private static final float PDF_LEADING = 14f;
    private static final float PDF_MARGIN = 50f;

    private final WeeklyReportRepository weeklyReportRepository;
    private final ReportEntryRepository reportEntryRepository;
    private final ObjectMapper objectMapper;

    public ReportExportService(WeeklyReportRepository weeklyReportRepository,
                               ReportEntryRepository reportEntryRepository,
                               ObjectMapper objectMapper) {
        this.weeklyReportRepository = weeklyReportRepository;
        this.reportEntryRepository = reportEntryRepository;
        this.objectMapper = objectMapper;
    }

    public byte[] exportPdf(Long weeklyReportId) {
        WeeklyReport report = getReport(weeklyReportId);
        Map<SectionType, List<ReportEntry>> grouped = groupEntries(weeklyReportId);

        try (PDDocument document = new PDDocument()) {
            PdfWriter writer = new PdfWriter(document);
            writer.addTitle("Weekly Report", true);
            writer.addLine("Report ID: " + report.getId());
            writer.addLine("Week: " + report.getWeekStart().format(DATE_FORMAT) + " - " + report.getWeekEnd().format(DATE_FORMAT));
            writer.addLine("Status: " + report.getStatus());
            writer.addBlankLine();

            for (SectionType section : SectionType.values()) {
                writer.addHeading(formatSectionTitle(section));
                List<ReportEntry> entries = grouped.get(section);
                if (entries == null || entries.isEmpty()) {
                    writer.addLine("No entries recorded.");
                    writer.addBlankLine();
                    continue;
                }
                int index = 1;
                for (ReportEntry entry : entries) {
                    writer.addLine(index + ". Event Date: " + entry.getEventDate().format(DATE_FORMAT));
                    writer.addLine("Contributor: " + entry.getContributedBy().getFullName());
                    writer.addLine("Content:");
                    for (String line : formatContent(entry.getContentJson())) {
                        writer.addLine("  " + line);
                    }
                    writer.addBlankLine();
                    index++;
                }
            }
            writer.close();
            return writer.toBytes();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to generate PDF", ex);
        }
    }

    public byte[] exportDocx(Long weeklyReportId) {
        WeeklyReport report = getReport(weeklyReportId);
        Map<SectionType, List<ReportEntry>> grouped = groupEntries(weeklyReportId);

        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            addDocxHeading(document, "Weekly Report", 16, true);
            addDocxParagraph(document, "Report ID: " + report.getId());
            addDocxParagraph(document, "Week: " + report.getWeekStart().format(DATE_FORMAT) + " - " + report.getWeekEnd().format(DATE_FORMAT));
            addDocxParagraph(document, "Status: " + report.getStatus());
            addDocxParagraph(document, "");

            for (SectionType section : SectionType.values()) {
                addDocxHeading(document, formatSectionTitle(section), 13, true);
                List<ReportEntry> entries = grouped.get(section);
                if (entries == null || entries.isEmpty()) {
                    addDocxParagraph(document, "No entries recorded.");
                    addDocxParagraph(document, "");
                    continue;
                }
                int index = 1;
                for (ReportEntry entry : entries) {
                    addDocxParagraph(document, index + ". Event Date: " + entry.getEventDate().format(DATE_FORMAT));
                    addDocxParagraph(document, "Contributor: " + entry.getContributedBy().getFullName());
                    addDocxParagraph(document, "Content:");
                    for (String line : formatContent(entry.getContentJson())) {
                        addDocxParagraph(document, "  " + line);
                    }
                    addDocxParagraph(document, "");
                    index++;
                }
            }

            document.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to generate DOCX", ex);
        }
    }

    private WeeklyReport getReport(Long weeklyReportId) {
        return weeklyReportRepository.findById(weeklyReportId)
                .orElseThrow(() -> new IllegalArgumentException("Weekly report not found"));
    }

    private Map<SectionType, List<ReportEntry>> groupEntries(Long weeklyReportId) {
        List<ReportEntry> entries = reportEntryRepository.findByWeeklyReportIdOrderBySectionTypeAscCreatedAtAsc(weeklyReportId);
        Map<SectionType, List<ReportEntry>> grouped = new EnumMap<>(SectionType.class);
        for (ReportEntry entry : entries) {
            grouped.computeIfAbsent(entry.getSectionType(), key -> new ArrayList<>()).add(entry);
        }
        return grouped;
    }

    private List<String> formatContent(String contentJson) {
        if (contentJson == null || contentJson.isBlank()) {
            return List.of("(empty)");
        }
        String trimmed = contentJson.trim();
        if ((trimmed.startsWith("{") && trimmed.endsWith("}")) || (trimmed.startsWith("[") && trimmed.endsWith("]"))) {
            try {
                JsonNode node = objectMapper.readTree(trimmed);
                return formatJsonLines(node);
            } catch (IOException ignored) {
                return splitLines(trimmed);
            }
        }
        return splitLines(trimmed);
    }

    private List<String> formatJsonLines(JsonNode node) {
        List<String> lines = new ArrayList<>();
        if (node.isArray()) {
            int index = 1;
            for (JsonNode item : node) {
                lines.add(index + ". " + item.asText());
                index++;
            }
            return lines.isEmpty() ? List.of("(empty)") : lines;
        }
        if (node.isObject()) {
            node.fieldNames().forEachRemaining(field -> {
                String label = toTitleCase(field);
                JsonNode value = node.get(field);
                String textValue = value == null || value.isNull() ? "" : value.asText();
                lines.add(label + ": " + textValue);
            });
            return lines.isEmpty() ? List.of("(empty)") : lines;
        }
        return List.of(node.asText());
    }

    private String toTitleCase(String field) {
        String spaced = field.replaceAll("([A-Z])", " $1").replace('_', ' ').trim();
        if (spaced.isEmpty()) {
            return field;
        }
        return spaced.substring(0, 1).toUpperCase(Locale.ENGLISH) + spaced.substring(1);
    }

    private List<String> splitLines(String text) {
        String normalized = text.replace("\r\n", "\n").replace("\r", "\n");
        String[] lines = normalized.split("\n");
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            if (!line.isBlank()) {
                result.add(line);
            }
        }
        return result.isEmpty() ? List.of("(empty)") : result;
    }

    private String formatSectionTitle(SectionType section) {
        return section.name().replace('_', ' ');
    }

    private void addDocxHeading(XWPFDocument document, String text, int fontSize, boolean bold) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(bold);
        run.setFontSize(fontSize);
    }

    private void addDocxParagraph(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontSize(11);
    }

    private static class PdfWriter implements AutoCloseable {
        private final PDDocument document;
        private PDPage page;
        private PDPageContentStream stream;
        private float y;

        private PdfWriter(PDDocument document) throws IOException {
            this.document = document;
            newPage();
        }

        void addTitle(String title, boolean spaced) throws IOException {
            addHeading(title);
            if (spaced) {
                addBlankLine();
            }
        }

        void addHeading(String text) throws IOException {
            addWrappedLine(text, 13f, true);
        }

        void addLine(String text) throws IOException {
            addWrappedLine(text, PDF_FONT_SIZE, false);
        }

        void addBlankLine() throws IOException {
            y -= PDF_LEADING;
            ensureSpace();
        }

        private void addWrappedLine(String text, float fontSize, boolean bold) throws IOException {
            List<String> lines = wrapText(text, fontSize);
            for (String line : lines) {
                ensureSpace();
                stream.beginText();
                stream.setFont(bold ? PDType1Font.HELVETICA_BOLD : PDF_FONT, fontSize);
                stream.newLineAtOffset(PDF_MARGIN, y);
                stream.showText(line);
                stream.endText();
                y -= PDF_LEADING;
            }
        }

        private List<String> wrapText(String text, float fontSize) throws IOException {
            float maxWidth = page.getMediaBox().getWidth() - (PDF_MARGIN * 2);
            String[] words = text.split(" ");
            List<String> lines = new ArrayList<>();
            StringBuilder current = new StringBuilder();
            for (String word : words) {
                if (current.length() == 0) {
                    current.append(word);
                    continue;
                }
                String candidate = current + " " + word;
                float width = PDF_FONT.getStringWidth(candidate) / 1000 * fontSize;
                if (width > maxWidth) {
                    lines.add(current.toString());
                    current = new StringBuilder(word);
                } else {
                    current.append(" ").append(word);
                }
            }
            if (current.length() > 0) {
                lines.add(current.toString());
            }
            return lines;
        }

        private void ensureSpace() throws IOException {
            if (y <= PDF_MARGIN) {
                stream.close();
                newPage();
            }
        }

        private void newPage() throws IOException {
            page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);
            stream = new PDPageContentStream(document, page);
            y = page.getMediaBox().getHeight() - PDF_MARGIN;
        }

        byte[] toBytes() throws IOException {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                document.save(outputStream);
                return outputStream.toByteArray();
            }
        }

        @Override
        public void close() throws IOException {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
