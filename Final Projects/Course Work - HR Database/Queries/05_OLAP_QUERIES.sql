-- ============================================================
-- Course Work: HR Management System
-- OLAP Queries (Analytical)
-- ============================================================

USE hr_dwh;

-- Q1: Monthly salary cost by department (from Fact_Employee_Snapshot)
SELECT 
    dt.year,
    dt.month,
    dt.month_name,
    dd.department_name,
    fes.employee_count,
    fes.total_salary,
    fes.avg_salary,
    fes.active_count
FROM Fact_Employee_Snapshot fes
JOIN Dim_Date dt ON fes.date_sk = dt.date_sk
JOIN Dim_Department dd ON fes.department_sk = dd.department_sk
ORDER BY dt.year, dt.month, fes.total_salary DESC;

-- Q2: Which departments have the highest salary costs per employee?
SELECT 
    dd.department_name,
    AVG(fes.employee_count) AS avg_headcount,
    AVG(fes.total_salary) AS avg_monthly_cost,
    AVG(fes.avg_salary) AS overall_avg_salary,
    AVG(fes.total_salary) / NULLIF(AVG(fes.employee_count), 0) AS cost_per_employee
FROM Fact_Employee_Snapshot fes
JOIN Dim_Department dd ON fes.department_sk = dd.department_sk
GROUP BY dd.department_name
ORDER BY avg_monthly_cost DESC;

-- Q3: Recruitment effectiveness — hires by campaign and position (Fact_Recruitment)
SELECT 
    dc.campaign_name,
    dc.channel,
    dp.position_title,
    COUNT(fr.candidate_count) AS total_candidates,
    SUM(fr.hire_count) AS total_hires,
    ROUND(SUM(fr.hire_count) * 100.0 / NULLIF(COUNT(fr.candidate_count), 0), 1) AS hire_rate_pct,
    ROUND(AVG(fr.avg_score), 2) AS avg_interview_score,
    ROUND(dc.budget / NULLIF(SUM(fr.hire_count), 0), 2) AS cost_per_hire
FROM Fact_Recruitment fr
JOIN Dim_Campaign dc ON fr.campaign_sk = dc.campaign_sk
JOIN Dim_Position dp ON fr.position_sk = dp.position_sk
GROUP BY dc.campaign_name, dc.channel, dp.position_title, dc.budget
ORDER BY total_hires DESC;

-- Q4: Employee training participation (via Bridge table)
SELECT 
    CONCAT(de.first_name, ' ', de.last_name) AS employee_name,
    de.position_title,
    de.department_name,
    COUNT(DISTINCT bet.training_sk) AS training_programs_count,
    GROUP_CONCAT(DISTINCT dt.program_name SEPARATOR ', ') AS programs
FROM Bridge_Employee_Training bet
JOIN Dim_Employee de ON bet.employee_sk = de.employee_sk AND de.is_current = TRUE
JOIN Dim_Training dt ON bet.training_sk = dt.training_sk AND dt.is_current = TRUE
GROUP BY de.employee_sk, de.first_name, de.last_name, de.position_title, de.department_name
ORDER BY training_programs_count DESC;

-- Q5: Year-over-Year salary trend
SELECT 
    dt.year,
    dd.department_name,
    SUM(fes.total_salary) AS yearly_salary_cost,
    SUM(fes.employee_count) AS yearly_headcount,
    ROUND(SUM(fes.total_salary) / NULLIF(SUM(fes.employee_count), 0), 2) AS yearly_avg_salary
FROM Fact_Employee_Snapshot fes
JOIN Dim_Date dt ON fes.date_sk = dt.date_sk
JOIN Dim_Department dd ON fes.department_sk = dd.department_sk
GROUP BY dt.year, dd.department_name
ORDER BY dt.year, yearly_salary_cost DESC;
