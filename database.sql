CREATE DATABASE IF NOT EXISTS weekly_reporting_db;
USE weekly_reporting_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS weekly_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    week_start DATE NOT NULL,
    week_end DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_weekly_report_user FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS report_entries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    weekly_report_id BIGINT NOT NULL,
    section_type VARCHAR(80) NOT NULL,
    event_date DATE NOT NULL,
    content_json LONGTEXT NOT NULL,
    contributed_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_entry_week FOREIGN KEY (weekly_report_id) REFERENCES weekly_reports(id),
    CONSTRAINT fk_entry_user FOREIGN KEY (contributed_by) REFERENCES users(id)
);
