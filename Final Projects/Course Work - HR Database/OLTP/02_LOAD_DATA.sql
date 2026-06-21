-- ============================================================
-- Course Work: HR Management System
-- Load CSV Data into OLTP (rerunnable, checks duplicates)
-- ============================================================

USE hr_oltp;

-- Disable FK checks for loading
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Load Departments
INSERT INTO departments (department_name, location, manager_id)
SELECT tmp.department_name, tmp.location, e.employee_id
FROM (
    SELECT 'Engineering' AS department_name, 'New York' AS location, 'John' AS mgr_first, 'Smith' AS mgr_last
    UNION ALL SELECT 'Human Resources', 'Chicago', 'Sarah', 'Johnson'
    UNION ALL SELECT 'Marketing', 'San Francisco', 'Michael', 'Brown'
    UNION ALL SELECT 'Finance', 'Boston', 'Emily', 'Davis'
    UNION ALL SELECT 'Sales', 'Los Angeles', 'David', 'Wilson'
    UNION ALL SELECT 'IT Support', 'Dallas', 'Jessica', 'Taylor'
    UNION ALL SELECT 'Legal', 'Washington', 'Amy', 'Anderson'
    UNION ALL SELECT 'Operations', 'Seattle', 'Robert', 'Thomas'
) tmp
LEFT JOIN employees e ON e.first_name = tmp.mgr_first AND e.last_name = tmp.mgr_last
WHERE NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.department_name = tmp.department_name
);

-- 2. Load Positions
INSERT INTO positions (position_title, department_id, min_salary, max_salary)
SELECT tmp.position_title, d.department_id, tmp.min_salary, tmp.max_salary
FROM (
    SELECT 'Software Engineer' AS position_title, 'Engineering' AS dept, 60000 AS min_s, 120000 AS max_s
    UNION ALL SELECT 'Senior Developer', 'Engineering', 90000, 160000
    UNION ALL SELECT 'QA Engineer', 'Engineering', 50000, 95000
    UNION ALL SELECT 'DevOps Engineer', 'Engineering', 80000, 140000
    UNION ALL SELECT 'HR Manager', 'Human Resources', 55000, 95000
    UNION ALL SELECT 'Recruiter', 'Human Resources', 40000, 70000
    UNION ALL SELECT 'Marketing Specialist', 'Marketing', 45000, 85000
    UNION ALL SELECT 'Marketing Manager', 'Marketing', 65000, 120000
    UNION ALL SELECT 'Financial Analyst', 'Finance', 50000, 90000
    UNION ALL SELECT 'Accountant', 'Finance', 45000, 80000
    UNION ALL SELECT 'Sales Representative', 'Sales', 35000, 75000
    UNION ALL SELECT 'Sales Manager', 'Sales', 60000, 130000
    UNION ALL SELECT 'IT Support Specialist', 'IT Support', 40000, 75000
    UNION ALL SELECT 'Legal Counsel', 'Legal', 80000, 180000
    UNION ALL SELECT 'Operations Manager', 'Operations', 60000, 110000
    UNION ALL SELECT 'Data Analyst', 'Engineering', 55000, 100000
) tmp
JOIN departments d ON d.department_name = tmp.dept
WHERE NOT EXISTS (
    SELECT 1 FROM positions p WHERE p.position_title = tmp.position_title
);

-- 3. Load Employees (handles hierarchy)
INSERT INTO employees (first_name, last_name, email, phone, hire_date, position_id, salary, manager_id, status)
SELECT tmp.first_name, tmp.last_name, tmp.email, tmp.phone, tmp.hire_date, 
       p.position_id, tmp.salary, m.employee_id, tmp.status
FROM (
    SELECT 'John' AS fn, 'Smith' AS ln, 'john.smith@company.com' AS email, '+1-212-555-0101' AS ph,
           '2020-01-15' AS hd, 'Senior Developer' AS pos, 140000 AS sal, NULL AS mgr_fn, NULL AS mgr_ln, 'active' AS st
    UNION ALL SELECT 'Sarah','Johnson','sarah.johnson@company.com','+1-312-555-0202','2020-03-01','HR Manager',85000,NULL,NULL,'active'
    UNION ALL SELECT 'Michael','Brown','michael.brown@company.com','+1-415-555-0303','2020-06-10','Marketing Manager',110000,NULL,NULL,'active'
    UNION ALL SELECT 'Emily','Davis','emily.davis@company.com','+1-617-555-0404','2021-02-20','Financial Analyst',75000,NULL,NULL,'active'
    UNION ALL SELECT 'David','Wilson','david.wilson@company.com','+1-213-555-0505','2021-04-15','Sales Manager',120000,NULL,NULL,'active'
    UNION ALL SELECT 'Jessica','Taylor','jessica.taylor@company.com','+1-214-555-0606','2021-08-01','IT Support Specialist',65000,NULL,NULL,'active'
    UNION ALL SELECT 'Amy','Anderson','amy.anderson@company.com','+1-202-555-0707','2022-01-10','Legal Counsel',150000,NULL,NULL,'active'
    UNION ALL SELECT 'Robert','Thomas','robert.thomas@company.com','+1-206-555-0808','2022-03-20','Operations Manager',95000,NULL,NULL,'active'
    UNION ALL SELECT 'Alex','Johnson','alex.johnson@company.com','+1-212-555-0909','2022-06-01','Software Engineer',85000,'John','Smith','active'
    UNION ALL SELECT 'Maria','Garcia','maria.garcia@company.com','+1-212-555-1010','2022-07-15','QA Engineer',70000,'John','Smith','active'
    UNION ALL SELECT 'James','Williams','james.williams@company.com','+1-312-555-1111','2023-01-15','Recruiter',55000,'Sarah','Johnson','active'
    UNION ALL SELECT 'Linda','Martinez','linda.martinez@company.com','+1-415-555-1212','2023-03-01','Marketing Specialist',65000,'Michael','Brown','active'
    UNION ALL SELECT 'Charles','Anderson','charles.anderson@company.com','+1-617-555-1313','2023-04-20','Accountant',60000,'Emily','Davis','active'
    UNION ALL SELECT 'Patricia','Thomas','patricia.thomas@company.com','+1-213-555-1414','2023-06-10','Sales Representative',55000,'David','Wilson','active'
    UNION ALL SELECT 'Joseph','Jackson','joseph.jackson@company.com','+1-212-555-1515','2023-08-01','Data Analyst',75000,'John','Smith','active'
) tmp
JOIN positions p ON p.position_title = tmp.pos
LEFT JOIN employees m ON m.first_name = tmp.mgr_fn AND m.last_name = tmp.mgr_ln
WHERE NOT EXISTS (
    SELECT 1 FROM employees e WHERE e.email = tmp.email
);

-- 4. Load Candidates
INSERT INTO candidates (first_name, last_name, email, phone, position_id, status, applied_date, source)
SELECT tmp.fn, tmp.ln, tmp.email, tmp.ph, p.position_id, tmp.st, tmp.app_dt, tmp.src
FROM (
    SELECT 'Anna','White','anna.white@email.com','+1-555-2001','Software Engineer','new','2026-01-10','LinkedIn'
    UNION ALL SELECT 'Mark','Lee','mark.lee@email.com','+1-555-2002','Senior Developer','screening','2026-01-15','Referral'
    UNION ALL SELECT 'Sophie','Clark','sophie.clark@email.com','+1-555-2003','Marketing Specialist','interview','2026-01-20','Indeed'
    UNION ALL SELECT 'Kevin','Adams','kevin.adams@email.com','+1-555-2004','Data Analyst','offer','2026-02-01','LinkedIn'
    UNION ALL SELECT 'Rachel','Green','rachel.green@email.com','+1-555-2005','Sales Representative','screening','2026-02-05','Company Website'
    UNION ALL SELECT 'Daniel','Harris','daniel.harris@email.com','+1-555-2006','QA Engineer','interview','2026-02-10','LinkedIn'
    UNION ALL SELECT 'Laura','Nelson','laura.nelson@email.com','+1-555-2007','Financial Analyst','new','2026-02-15','Referral'
    UNION ALL SELECT 'Chris','Mitchell','chris.mitchell@email.com','+1-555-2008','DevOps Engineer','hired','2026-02-20','Indeed'
    UNION ALL SELECT 'Emma','Roberts','emma.roberts@email.com','+1-555-2009','Accountant','rejected','2026-03-01','LinkedIn'
    UNION ALL SELECT 'Tom','Walker','tom.walker@email.com','+1-555-2010','IT Support Specialist','interview','2026-03-05','Company Website'
) tmp
JOIN positions p ON p.position_title = tmp.pos
WHERE NOT EXISTS (
    SELECT 1 FROM candidates c WHERE c.email = tmp.email
);

-- 5. Load Training Programs
INSERT INTO training_programs (program_name, description, duration_days, cost, start_date)
SELECT tmp.prog, tmp.desc, tmp.days, tmp.cost, tmp.dt
FROM (
    SELECT 'Leadership Essentials' AS prog, 'Management skills for team leads' AS desc, 5 AS days, 2500.00 AS cost, '2026-02-01' AS dt
    UNION ALL SELECT 'Advanced SQL', 'DWH and query optimization', 3, 1800.00, '2026-03-01'
    UNION ALL SELECT 'Cloud Architecture', 'AWS cloud infrastructure', 10, 5000.00, '2026-04-01'
    UNION ALL SELECT 'Agile Methodology', 'Scrum master certification', 3, 1500.00, '2026-05-01'
    UNION ALL SELECT 'Data Science Basics', 'Python and ML fundamentals', 8, 3500.00, '2026-06-01'
    UNION ALL SELECT 'Communication Skills', 'Effective business communication', 2, 800.00, '2026-07-01'
    UNION ALL SELECT 'Project Management', 'PMP preparation course', 12, 4500.00, '2026-08-01'
    UNION ALL SELECT 'Cybersecurity Fundamentals', 'Network and data security', 5, 2800.00, '2026-09-01'
) tmp
WHERE NOT EXISTS (
    SELECT 1 FROM training_programs tp WHERE tp.program_name = tmp.prog
);

-- 6. Load Recruitment Campaigns
INSERT INTO recruitment_campaigns (campaign_name, start_date, end_date, budget, channel, total_applications)
SELECT tmp.name, tmp.start, tmp.end, tmp.budget, tmp.channel, tmp.apps
FROM (
    SELECT 'Q1 Tech Hiring' AS name, '2026-01-01' AS start, '2026-03-31' AS end, 50000.00 AS budget, 'LinkedIn' AS channel, 120 AS apps
    UNION ALL SELECT 'Spring Marketing Push', '2026-03-01', '2026-05-31', 30000.00, 'Google Ads', 85
    UNION ALL SELECT 'Summer Intern Program', '2026-04-01', '2026-06-30', 25000.00, 'University Career Fair', 200
    UNION ALL SELECT 'Junior Developer Drive', '2026-06-01', '2026-08-31', 45000.00, 'Indeed', 150
    UNION ALL SELECT 'Leadership Recruitment', '2026-07-01', '2026-09-30', 60000.00, 'Executive Search', 40
) tmp
WHERE NOT EXISTS (
    SELECT 1 FROM recruitment_campaigns rc WHERE rc.campaign_name = tmp.name
);

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'OLTP data loaded successfully!' AS message;
