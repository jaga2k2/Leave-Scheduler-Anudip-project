-- ============================================================
-- Leave Scheduler Database Initialization
-- Run this ONCE after creating the database
-- ============================================================

CREATE DATABASE IF NOT EXISTS leave_scheduler;
USE leave_scheduler;

-- Tables are auto-created by Spring Boot (ddl-auto=update)
-- This script seeds initial data

-- ============================================================
-- INSERT ADMIN USER (password: admin123)
-- BCrypt hash of "admin123"
-- ============================================================
INSERT IGNORE INTO users (username, password, email, first_name, last_name, role, department, designation, joining_date, active)
VALUES (
  'admin',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVSj1XQa3O',
  'admin@company.com',
  'System',
  'Admin',
  'ADMIN',
  'MANAGEMENT',
  'System Administrator',
  CURDATE(),
  true
);

-- ============================================================
-- INSERT SAMPLE MANAGER (password: manager123)
-- ============================================================
INSERT IGNORE INTO users (username, password, email, first_name, last_name, role, department, designation, joining_date, active)
VALUES (
  'john.manager',
  '$2a$10$8K1p/a0dLjLZXS/JZzGfXO5e6Z5eZ5eZ5eZ5eZ5eZ5eZ5eZ5eZ5e',
  'john.manager@company.com',
  'John',
  'Smith',
  'MANAGER',
  'ENGINEERING',
  'Engineering Manager',
  '2020-01-15',
  true
);

-- ============================================================
-- INSERT SAMPLE EMPLOYEE (password: emp123)
-- ============================================================
INSERT IGNORE INTO users (username, password, email, first_name, last_name, role, department, designation, joining_date, active, manager_id)
VALUES (
  'jane.doe',
  '$2a$10$8K1p/a0dLjLZXS/JZzGfXO5e6Z5eZ5eZ5eZ5eZ5eZ5eZ5eZ5eZ5e',
  'jane.doe@company.com',
  'Jane',
  'Doe',
  'EMPLOYEE',
  'ENGINEERING',
  'Software Engineer',
  '2022-06-01',
  true,
  (SELECT id FROM (SELECT id FROM users WHERE username = 'john.manager') AS tmp)
);

-- ============================================================
-- LEAVE BALANCES for each user (year 2026)
-- ============================================================
INSERT IGNORE INTO leave_balances (user_id, annual_balance, sick_balance, casual_balance, annual_used, sick_used, casual_used, last_credited_date, year)
SELECT id, 21, 10, 7, 0, 0, 0, CURDATE(), YEAR(CURDATE())
FROM users
WHERE NOT EXISTS (
  SELECT 1 FROM leave_balances lb WHERE lb.user_id = users.id
);

-- ============================================================
-- SAMPLE PUBLIC HOLIDAYS (India, 2026)
-- ============================================================
INSERT IGNORE INTO holidays (name, date, description, type) VALUES
('New Year''s Day',          '2026-01-01', 'Start of the New Year',           'NATIONAL'),
('Republic Day',             '2026-01-26', 'India Republic Day',               'NATIONAL'),
('Holi',                     '2026-03-20', 'Festival of Colours',              'NATIONAL'),
('Good Friday',              '2026-04-03', 'Good Friday',                      'NATIONAL'),
('Ambedkar Jayanti',         '2026-04-14', 'Dr. B.R. Ambedkar Birthday',       'NATIONAL'),
('Labour Day',               '2026-05-01', 'International Workers Day',        'NATIONAL'),
('Eid al-Adha',              '2026-06-27', 'Bakrid / Eid ul-Adha',             'NATIONAL'),
('Independence Day',         '2026-08-15', 'India Independence Day',           'NATIONAL'),
('Janmashtami',              '2026-08-20', 'Birth of Lord Krishna',            'NATIONAL'),
('Gandhi Jayanti',           '2026-10-02', 'Mahatma Gandhi Birthday',          'NATIONAL'),
('Dussehra',                 '2026-10-12', 'Vijaya Dashami',                   'NATIONAL'),
('Diwali',                   '2026-11-09', 'Festival of Lights',               'NATIONAL'),
('Christmas Day',            '2026-12-25', 'Christmas',                        'NATIONAL');
