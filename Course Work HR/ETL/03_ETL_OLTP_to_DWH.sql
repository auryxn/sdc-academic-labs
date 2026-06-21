-- ============================================================
-- Course Work: HR Management System
-- ETL: OLTP → OLAP (rerunnable, incremental)
-- ============================================================

USE hr_dwh;

-- ===== 1. EXTRACT from OLTP =====

-- 1.1 Departments
INSERT INTO Dim_Department (department_id, department_name, location, effective_date, is_current)
SELECT d.department_id, d.department_name, d.location, CURDATE(), TRUE
FROM hr_oltp.departments d
WHERE NOT EXISTS (
    SELECT 1 FROM Dim_Department dd 
    WHERE dd.department_id = d.department_id AND dd.is_current = TRUE
);

-- 1.2 Positions
INSERT INTO Dim_Position (position_id, position_title, min_salary, max_salary, effective_date, is_current)
SELECT p.position_id, p.position_title, p.min_salary, p.max_salary, CURDATE(), TRUE
FROM hr_oltp.positions p
WHERE NOT EXISTS (
    SELECT 1 FROM Dim_Position dp 
    WHERE dp.position_id = p.position_id AND dp.is_current = TRUE
);

-- 1.3 Employees (SCD Type 2)
-- Check if salary or position changed
INSERT INTO Dim_Employee (employee_id, first_name, last_name, email, position_title, 
                          department_name, salary, status, start_date, end_date, is_current)
SELECT e.employee_id, e.first_name, e.last_name, e.email, 
       p.position_title, d.department_name, e.salary, e.status,
       CURDATE(), NULL, TRUE
FROM hr_oltp.employees e
JOIN hr_oltp.positions p ON e.position_id = p.position_id
JOIN hr_oltp.departments d ON p.department_id = d.department_id
WHERE NOT EXISTS (
    SELECT 1 FROM Dim_Employee de 
    WHERE de.employee_id = e.employee_id AND de.is_current = TRUE
);

-- Close old version if salary/status changed
UPDATE Dim_Employee de
JOIN hr_oltp.employees e ON de.employee_id = e.employee_id
SET de.end_date = CURDATE(), de.is_current = FALSE
WHERE de.is_current = TRUE 
  AND (de.salary != e.salary OR de.status != e.status);

-- Insert new version if was closed above
INSERT INTO Dim_Employee (employee_id, first_name, last_name, email, position_title,
                          department_name, salary, status, start_date, end_date, is_current)
SELECT e.employee_id, e.first_name, e.last_name, e.email,
       p.position_title, d.department_name, e.salary, e.status,
       CURDATE(), NULL, TRUE
FROM hr_oltp.employees e
JOIN hr_oltp.positions p ON e.position_id = p.position_id
JOIN hr_oltp.departments d ON p.department_id = d.department_id
WHERE NOT EXISTS (
    SELECT 1 FROM Dim_Employee de 
    WHERE de.employee_id = e.employee_id AND de.is_current = TRUE
);

-- 1.4 Candidates
INSERT INTO Dim_Candidate (candidate_id, first_name, last_name, email, source, effective_date, is_current)
SELECT c.candidate_id, c.first_name, c.last_name, c.email, c.source, CURDATE(), TRUE
FROM hr_oltp.candidates c
WHERE NOT EXISTS (
    SELECT 1 FROM Dim_Candidate dc 
    WHERE dc.candidate_id = c.candidate_id AND dc.is_current = TRUE
);

-- 1.5 Training Programs
INSERT INTO Dim_Training (program_id, program_name, duration_days, cost, effective_date, is_current)
SELECT tp.program_id, tp.program_name, tp.duration_days, tp.cost, CURDATE(), TRUE
FROM hr_oltp.training_programs tp
WHERE NOT EXISTS (
    SELECT 1 FROM Dim_Training dt 
    WHERE dt.program_id = tp.program_id AND dt.is_current = TRUE
);

-- 1.6 Campaigns
INSERT INTO Dim_Campaign (campaign_id, campaign_name, channel, budget, effective_date, is_current)
SELECT rc.campaign_id, rc.campaign_name, rc.channel, rc.budget, CURDATE(), TRUE
FROM hr_oltp.recruitment_campaigns rc
WHERE NOT EXISTS (
    SELECT 1 FROM Dim_Campaign dc 
    WHERE dc.campaign_id = rc.campaign_id AND dc.is_current = TRUE
);

-- ===== 2. TRANSFORM & LOAD — Fact Tables =====

-- 2.1 Fact_Employee_Snapshot (monthly aggregates)
INSERT INTO Fact_Employee_Snapshot (date_sk, department_sk, position_sk, employee_count, total_salary, avg_salary, active_count)
SELECT 
    YEAR(CURDATE()) * 10000 + MONTH(CURDATE()) * 100 + 1 AS date_sk,
    dd.department_sk,
    dp.position_sk,
    COUNT(*) AS employee_count,
    SUM(e.salary) AS total_salary,
    AVG(e.salary) AS avg_salary,
    SUM(CASE WHEN e.status = 'active' THEN 1 ELSE 0 END) AS active_count
FROM hr_oltp.employees e
JOIN hr_oltp.positions p ON e.position_id = p.position_id
JOIN hr_oltp.departments d ON p.department_id = d.department_id
JOIN Dim_Department dd ON dd.department_id = d.department_id AND dd.is_current = TRUE
JOIN Dim_Position dp ON dp.position_id = p.position_id AND dp.is_current = TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM Fact_Employee_Snapshot fes
    JOIN Dim_Date dt ON fes.date_sk = dt.date_sk
    WHERE dt.year = YEAR(CURDATE()) AND dt.month = MONTH(CURDATE())
      AND fes.department_sk = dd.department_sk
      AND fes.position_sk = dp.position_sk
)
GROUP BY dd.department_sk, dp.position_sk;

-- 2.2 Fact_Recruitment
INSERT INTO Fact_Recruitment (date_sk, campaign_sk, candidate_sk, position_sk, 
                               candidate_count, hire_count, interview_count, avg_score)
SELECT 
    YEAR(c.applied_date) * 10000 + MONTH(c.applied_date) * 100 + DAY(c.applied_date) AS date_sk,
    COALESCE(dc.campaign_sk, 1),
    dcand.candidate_sk,
    dp.position_sk,
    1 AS candidate_count,
    CASE WHEN c.status = 'hired' THEN 1 ELSE 0 END AS hire_count,
    CASE WHEN EXISTS (SELECT 1 FROM hr_oltp.interviews i WHERE i.candidate_id = c.candidate_id) 
         THEN 1 ELSE 0 END AS interview_count,
    (SELECT AVG(i.score) FROM hr_oltp.interviews i WHERE i.candidate_id = c.candidate_id) AS avg_score
FROM hr_oltp.candidates c
JOIN hr_oltp.positions p ON c.position_id = p.position_id
JOIN Dim_Position dp ON dp.position_id = p.position_id AND dp.is_current = TRUE
JOIN Dim_Candidate dcand ON dcand.candidate_id = c.candidate_id AND dcand.is_current = TRUE
LEFT JOIN Dim_Campaign dc ON dc.campaign_id = 1 AND dc.is_current = TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM Fact_Recruitment fr
    JOIN Dim_Date dt ON fr.date_sk = dt.date_sk
    WHERE dt.full_date = c.applied_date
      AND fr.candidate_sk = dcand.candidate_sk
);

-- ===== 3. BRIDGE TABLE =====
INSERT INTO Bridge_Employee_Training (employee_sk, training_sk, enrollment_count)
SELECT de.employee_sk, dt.training_sk, 1
FROM hr_oltp.employee_training et
JOIN hr_oltp.employees e ON et.employee_id = e.employee_id
JOIN hr_oltp.training_programs tp ON et.program_id = tp.program_id
JOIN Dim_Employee de ON de.employee_id = e.employee_id AND de.is_current = TRUE
JOIN Dim_Training dt ON dt.program_id = tp.program_id AND dt.is_current = TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM Bridge_Employee_Training bet
    WHERE bet.employee_sk = de.employee_sk AND bet.training_sk = dt.training_sk
);

SELECT 'ETL process completed successfully!' AS message;
