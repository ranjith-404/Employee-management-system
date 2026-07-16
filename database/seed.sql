USE employee_db;

INSERT INTO departments (dept_name, location, created_at) VALUES
('Human Resources', 'Building A - Floor 1', NOW()),
('Engineering', 'Building B - Floor 3', NOW()),
('Marketing', 'Building A - Floor 2', NOW()),
('Finance', 'Building C - Floor 1', NOW()),
('Operations', 'Building B - Floor 1', NOW());

INSERT INTO employees (dept_id, first_name, last_name, email, phone, hire_date, is_active, created_at) VALUES
(1, 'Sarah', 'Johnson', 'sarah.johnson@company.com', '555-0101', '2020-01-15', true, NOW()),
(2, 'Michael', 'Chen', 'michael.chen@company.com', '555-0102', '2019-06-20', true, NOW()),
(2, 'Emily', 'Rodriguez', 'emily.rodriguez@company.com', '555-0103', '2021-03-10', true, NOW()),
(3, 'David', 'Williams', 'david.williams@company.com', '555-0104', '2020-08-25', true, NOW()),
(4, 'Jessica', 'Brown', 'jessica.brown@company.com', '555-0105', '2019-11-30', true, NOW()),
(5, 'James', 'Taylor', 'james.taylor@company.com', '555-0106', '2022-02-14', true, NOW()),
(2, 'Lisa', 'Anderson', 'lisa.anderson@company.com', '555-0107', '2021-07-01', true, NOW()),
(1, 'Robert', 'Martinez', 'robert.martinez@company.com', '555-0108', '2020-04-18', false, NOW());

INSERT INTO users (emp_id, username, password, role, is_active, created_at) VALUES
(1, 'admin', 'admin123', 'ADMIN', true, NOW()),
(2, 'mchen', 'emp123', 'EMPLOYEE', true, NOW()),
(3, 'erodriguez', 'emp123', 'EMPLOYEE', true, NOW()),
(4, 'dwilliams', 'emp123', 'EMPLOYEE', true, NOW()),
(5, 'jbrown', 'emp123', 'EMPLOYEE', true, NOW());

INSERT INTO attendance (emp_id, att_date, check_in, check_out, hours_worked, status, created_at) VALUES
(2, CURDATE(), '09:00:00', '17:30:00', 8.50, 'PRESENT', NOW()),
(3, CURDATE(), '08:45:00', NULL, NULL, 'PRESENT', NOW()),
(4, CURDATE(), '09:15:00', '17:00:00', 7.75, 'PRESENT', NOW());

INSERT INTO leaves (emp_id, leave_type, start_date, end_date, days, reason, status, created_at) VALUES
(2, 'SICK', DATE_ADD(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 2, 'Medical appointment', 'PENDING', NOW()),
(3, 'CASUAL', DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1, 'Personal work', 'PENDING', NOW()),
(4, 'PAID', DATE_ADD(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 5, 'Family vacation', 'APPROVED', NOW());

INSERT INTO payroll (emp_id, pay_month, pay_year, basic_salary, bonus, deductions, paid_date, created_at) VALUES
(2, 6, 2026, 7500.00, 500.00, 200.00, '2026-07-01', NOW()),
(3, 6, 2026, 6800.00, 300.00, 150.00, '2026-07-01', NOW()),
(4, 6, 2026, 7200.00, 400.00, 180.00, '2026-07-01', NOW()),
(5, 6, 2026, 6500.00, 200.00, 120.00, NULL, NOW());

SELECT 'Sample data seeded!' AS Status;
