-- ============================================================
-- Course Work: HR Management System
-- OLTP Queries
-- ============================================================

USE hr_oltp;

-- Q1: How many employees are in each department? (headcount & avg salary)
SELECT 
    d.department_name,
    COUNT(e.employee_id) AS employee_count,
    ROUND(AVG(e.salary), 2) AS avg_salary,
    ROUND(SUM(e.salary), 2) AS total_salary_cost
FROM departments d
JOIN positions p ON d.department_id = p.department_id
JOIN employees e ON p.position_id = e.position_id
WHERE e.status = 'active'
GROUP BY d.department_name
ORDER BY employee_count DESC;

-- Q2: Which positions have the most candidates in pipeline?
SELECT 
    p.position_title,
    d.department_name,
    COUNT(c.candidate_id) AS total_candidates,
    SUM(CASE WHEN c.status = 'hired' THEN 1 ELSE 0 END) AS hired_count,
    SUM(CASE WHEN c.status = 'rejected' THEN 1 ELSE 0 END) AS rejected_count,
    ROUND(SUM(CASE WHEN c.status = 'hired' THEN 1 ELSE 0 END) * 100.0 / COUNT(c.candidate_id), 1) AS hire_rate_pct
FROM positions p
JOIN departments d ON p.department_id = d.department_id
LEFT JOIN candidates c ON p.position_id = c.position_id
GROUP BY p.position_title, d.department_name
HAVING total_candidates > 0
ORDER BY total_candidates DESC;

-- Q3: Training enrollment and completion rates by employee
SELECT 
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    p.position_title,
    COUNT(et.enrollment_id) AS programs_enrolled,
    SUM(CASE WHEN et.status = 'completed' THEN 1 ELSE 0 END) AS programs_completed,
    ROUND(AVG(et.score), 2) AS avg_score,
    ROUND(SUM(CASE WHEN et.status = 'completed' THEN 1 ELSE 0 END) * 100.0 / COUNT(et.enrollment_id), 1) AS completion_rate_pct
FROM employees e
JOIN positions p ON e.position_id = p.position_id
LEFT JOIN employee_training et ON e.employee_id = et.employee_id
GROUP BY e.employee_id, e.first_name, e.last_name, p.position_title
ORDER BY programs_enrolled DESC;

-- Q4: Recruitment campaign performance
SELECT 
    campaign_name,
    channel,
    budget,
    total_applications,
    ROUND(budget / NULLIF(total_applications, 0), 2) AS cost_per_application,
    DATEDIFF(end_date, start_date) AS campaign_duration_days
FROM recruitment_campaigns
ORDER BY total_applications DESC;
