-- ============================================================
-- Course Work: OLAP Queries (DWH — University Management System)
-- ============================================================

USE University_DWH;
GO

-- Query 1: Enrollment trends by month and department
SELECT dd.Year, dd.Month, dd.MonthName, ddim.DepartmentName,
       COUNT(fe.EnrollmentKey) AS TotalEnrollments,
       SUM(fe.CreditsEnrolled) AS TotalCredits
FROM Fact_Enrollment fe
INNER JOIN Dim_Date dd ON fe.DateKey = dd.DateKey
INNER JOIN Dim_Department ddim ON fe.DepartmentKey = ddim.DepartmentKey
GROUP BY dd.Year, dd.Month, dd.MonthName, ddim.DepartmentName
ORDER BY dd.Year DESC, dd.Month DESC, TotalEnrollments DESC;

-- Query 2: Revenue analysis with student status (using SCD Type 2)
SELECT ds.DegreeLevel, ds.DepartmentName, ds.Status,
       COUNT(fp.PaymentKey) AS TransactionCount,
       SUM(fp.PaymentAmount) AS TotalRevenue,
       AVG(fp.PaymentAmount) AS AvgTransaction
FROM Fact_Payment fp
INNER JOIN Dim_Student ds ON fp.StudentKey = ds.StudentKey
WHERE ds.IsCurrent = 1
GROUP BY ds.DegreeLevel, ds.DepartmentName, ds.Status
ORDER BY TotalRevenue DESC;

-- Query 3: Teacher workload analysis (via Bridge table)
SELECT dt.TeacherName, dt.DepartmentName,
       COUNT(DISTINCT bct.CourseKey) AS CoursesTaught,
       COUNT(DISTINCT fe.StudentKey) AS StudentsTaught,
       SUM(fe.CreditsEnrolled) AS TotalCreditsManaged
FROM Dim_Teacher dt
INNER JOIN Bridge_CourseTeacher bct ON dt.TeacherKey = bct.TeacherKey
INNER JOIN Fact_Enrollment fe ON fe.CourseKey = bct.CourseKey
GROUP BY dt.TeacherName, dt.DepartmentName
ORDER BY StudentsTaught DESC;
