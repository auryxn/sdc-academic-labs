-- ============================================================
-- Course Work: OLTP Queries (University Management System)
-- ============================================================

USE University_OLTP;
GO

-- Query 1: Students with highest GPA per department
SELECT d.DepartmentName, s.FirstName, s.LastName,
       AVG(g.GradeValue) AS AvgGrade
FROM Student s
INNER JOIN Enrollment e ON s.StudentID = e.StudentID
INNER JOIN Grade g ON e.EnrollmentID = g.EnrollmentID
INNER JOIN StudyProgram sp ON s.ProgramID = sp.ProgramID
INNER JOIN Department d ON sp.DepartmentID = d.DepartmentID
WHERE g.GradeType = 'Final'
GROUP BY d.DepartmentName, s.FirstName, s.LastName, s.StudentID
HAVING AVG(g.GradeValue) >= 8.0
ORDER BY AvgGrade DESC;

-- Query 2: Course popularity (enrollment count) per semester
SELECT c.CourseCode, c.CourseName, c.Semester, c.AcademicYear,
       COUNT(e.EnrollmentID) AS EnrolledStudents,
       c.MaxStudents,
       ROUND(CAST(COUNT(e.EnrollmentID) AS FLOAT) / c.MaxStudents * 100, 1) AS FillPercent
FROM Course c
LEFT JOIN Enrollment e ON c.CourseID = e.CourseID
GROUP BY c.CourseCode, c.CourseName, c.Semester, c.AcademicYear, c.MaxStudents
ORDER BY EnrolledStudents DESC;

-- Query 3: Total revenue (payments) by faculty and academic year
SELECT f.FacultyName, p.AcademicYear,
       COUNT(p.PaymentID) AS PaymentCount,
       SUM(p.Amount) AS TotalRevenue,
       AVG(p.Amount) AS AvgPayment
FROM Payment p
INNER JOIN Student s ON p.StudentID = s.StudentID
INNER JOIN StudyProgram sp ON s.ProgramID = sp.ProgramID
INNER JOIN Department d ON sp.DepartmentID = d.DepartmentID
INNER JOIN Faculty f ON d.FacultyID = f.FacultyID
GROUP BY f.FacultyName, p.AcademicYear
ORDER BY TotalRevenue DESC;
