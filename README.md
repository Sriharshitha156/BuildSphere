# TEAM 41 BVRITH
Collaborative Weekly Report Management System

Spring Boot + MySQL full-stack starter based on your `ProblemStatement.pdf`.

## Tech Stack
- Backend: Java 17, Spring Boot, Spring Security (JWT), Spring Data JPA
- Database: MySQL
- Frontend: HTML/CSS/JavaScript (served by Spring Boot static resources)

## Features Implemented
- Role-based login (`FACULTY`, `COORDINATOR`, `ADMIN`)
- Weekly report creation with date range isolation
- Section-wise entry CRUD for all 17 sections
- Entry date validation within selected reporting week
- Contributor tracking for every entry
- Dashboard API with section completion indicators

## Default Test Users
- `admin@buildsphere.local` / `Admin@123`
- `coordinator@buildsphere.local` / `Coordinator@123`
- `faculty@buildsphere.local` / `Faculty@123`

## Setup
1. Start MySQL server.
2. Update DB credentials in `src/main/resources/application.properties` if needed.
3. Run:
   ```bash
   mvn spring-boot:run
   ```
4. Open:
   `http://localhost:8080`

## API Endpoints
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/reports` (coordinator/admin)
- `GET /api/reports`
- `POST /api/reports/entries`
- `PUT /api/reports/entries/{entryId}`
- `DELETE /api/reports/entries/{entryId}`
- `GET /api/reports/entries?weeklyReportId=1`
- `GET /api/dashboard?weeklyReportId=1`

## Notes
- `contentJson` is stored as JSON text so each section can keep dynamic fields without schema changes.
- Export to PDF/DOCX and real-time collaboration sockets can be added in the next phase.
