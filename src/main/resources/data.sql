-- Seed sample weekly report + entries if they don't exist

INSERT INTO weekly_reports (week_start, week_end, status, created_by, created_at)
SELECT '2026-03-24', '2026-03-30', 'IN_PROGRESS', u.id, NOW()
FROM users u
WHERE u.email = 'coordinator@buildsphere.local'
  AND NOT EXISTS (
    SELECT 1 FROM weekly_reports wr
    WHERE wr.week_start = '2026-03-24' AND wr.week_end = '2026-03-30'
  );

INSERT INTO report_entries (weekly_report_id, section_type, event_date, content_json, contributed_by, created_at, updated_at)
SELECT wr.id, 'GENERAL_POINTS', '2026-03-25',
       '{"title":"Department Meeting","details":"Weekly planning completed"}',
       u.id, NOW(), NOW()
FROM weekly_reports wr
JOIN users u ON u.email = 'faculty@buildsphere.local'
WHERE wr.week_start = '2026-03-24' AND wr.week_end = '2026-03-30'
  AND NOT EXISTS (
    SELECT 1 FROM report_entries re
    WHERE re.weekly_report_id = wr.id
      AND re.section_type = 'GENERAL_POINTS'
      AND re.event_date = '2026-03-25'
  );

INSERT INTO report_entries (weekly_report_id, section_type, event_date, content_json, contributed_by, created_at, updated_at)
SELECT wr.id, 'STUDENT_ACHIEVEMENTS', '2026-03-26',
       '{"student":"Ravi Kumar","rollNo":"CSE21-045","achievement":"Won hackathon"}',
       u.id, NOW(), NOW()
FROM weekly_reports wr
JOIN users u ON u.email = 'faculty@buildsphere.local'
WHERE wr.week_start = '2026-03-24' AND wr.week_end = '2026-03-30'
  AND NOT EXISTS (
    SELECT 1 FROM report_entries re
    WHERE re.weekly_report_id = wr.id
      AND re.section_type = 'STUDENT_ACHIEVEMENTS'
      AND re.event_date = '2026-03-26'
  );
