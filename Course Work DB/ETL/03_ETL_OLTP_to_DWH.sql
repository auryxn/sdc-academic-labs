-- ============================================================
-- Course Work: University Management System
-- ETL Process: OLTP -> OLAP (rerunnable, incremental)
-- ============================================================

USE University_DWH;
GO

-- ======== 1. Load Dim_Date ========
MERGE INTO Dim_Date AS target
USING (
    SELECT DISTINCT
        CAST(CONVERT(VARCHAR(8), e.EnrollmentDate, 112) AS INT) AS DateKey,
        e.EnrollmentDate AS FullDate,
        DAY(e.EnrollmentDate) AS Day,
        MONTH(e.EnrollmentDate) AS Month,
        DATENAME(MONTH, e.EnrollmentDate) AS MonthName,
        DATEPART(QUARTER, e.EnrollmentDate) AS Quarter,
        YEAR(e.EnrollmentDate) AS Year,
        CASE
            WHEN MONTH(e.EnrollmentDate) >= 9 THEN CAST(YEAR(e.EnrollmentDate) AS VARCHAR) + '-' + CAST(YEAR(e.EnrollmentDate) + 1 AS VARCHAR)
            ELSE CAST(YEAR(e.EnrollmentDate) - 1 AS VARCHAR) + '-' + CAST(YEAR(e.EnrollmentDate) AS VARCHAR)
        END AS AcademicYear
    FROM University_OLTP.dbo.Enrollment e
    UNION
    SELECT DISTINCT
        CAST(CONVERT(VARCHAR(8), p.PaymentDate, 112) AS INT),
        p.PaymentDate,
        DAY(p.PaymentDate),
        MONTH(p.PaymentDate),
        DATENAME(MONTH, p.PaymentDate),
        DATEPART(QUARTER, p.PaymentDate),
        YEAR(p.PaymentDate),
        CASE
            WHEN MONTH(p.PaymentDate) >= 9 THEN CAST(YEAR(p.PaymentDate) AS VARCHAR) + '-' + CAST(YEAR(p.PaymentDate) + 1 AS VARCHAR)
            ELSE CAST(YEAR(p.PaymentDate) - 1 AS VARCHAR) + '-' + CAST(YEAR(p.PaymentDate) AS VARCHAR)
        END
    FROM University_OLTP.dbo.Payment p
    UNION
    SELECT DISTINCT
        CAST(CONVERT(VARCHAR(8), g.GradeDate, 112) AS INT),
        g.GradeDate,
        DAY(g.GradeDate),
        MONTH(g.GradeDate),
        DATENAME(MONTH, g.GradeDate),
        DATEPART(QUARTER, g.GradeDate),
        YEAR(g.GradeDate),
        CASE
            WHEN MONTH(g.GradeDate) >= 9 THEN CAST(YEAR(g.GradeDate) AS VARCHAR) + '-' + CAST(YEAR(g.GradeDate) + 1 AS VARCHAR)
            ELSE CAST(YEAR(g.GradeDate) - 1 AS VARCHAR) + '-' + CAST(YEAR(g.GradeDate) AS VARCHAR)
        END
    FROM University_OLTP.dbo.Grade g
) AS source
ON target.DateKey = source.DateKey
WHEN NOT MATCHED THEN
    INSERT (DateKey, FullDate, Day, Month, MonthName, Quarter, Year, AcademicYear)
    VALUES (source.DateKey, source.FullDate, source.Day, source.Month, source.MonthName,
            source.Quarter, source.Year, source.AcademicYear);
GO

-- ======== 2. Load Dim_Student (SCD Type 2) ========
-- Close expired records
UPDATE ds
SET ds.ValidTo = DATEADD(DAY, -1, GETDATE()), ds.IsCurrent = 0
FROM Dim_Student ds
INNER JOIN University_OLTP.dbo.Student s ON ds.StudentID_Source = s.StudentID
WHERE ds.IsCurrent = 1
  AND (ds.Status != s.Status OR ds.ProgramName != sp.ProgramName)
  INNER JOIN University_OLTP.dbo.StudyProgram sp ON s.ProgramID = sp.ProgramID;

-- Insert new/changed records
MERGE INTO Dim_Student AS target
USING (
    SELECT s.StudentID AS StudentID_Source, s.FirstName, s.LastName, s.Email,
           sp.ProgramName, sp.DegreeLevel,
           d.DepartmentName, f.FacultyName, s.Status,
           s.EnrollmentDate AS ValidFrom
    FROM University_OLTP.dbo.Student s
    INNER JOIN University_OLTP.dbo.StudyProgram sp ON s.ProgramID = sp.ProgramID
    INNER JOIN University_OLTP.dbo.Department d ON sp.DepartmentID = d.DepartmentID
    INNER JOIN University_OLTP.dbo.Faculty f ON d.FacultyID = f.FacultyID
) AS source
ON target.StudentID_Source = source.StudentID_Source AND target.IsCurrent = 1
WHEN NOT MATCHED THEN
    INSERT (StudentID_Source, FirstName, LastName, Email, ProgramName, DegreeLevel,
            DepartmentName, FacultyName, Status, ValidFrom, ValidTo, IsCurrent)
    VALUES (source.StudentID_Source, source.FirstName, source.LastName, source.Email,
            source.ProgramName, source.DegreeLevel, source.DepartmentName, source.FacultyName,
            source.Status, source.ValidFrom, NULL, 1);
GO

-- ======== 3. Load Dim_Department ========
MERGE INTO Dim_Department AS target
USING (
    SELECT d.DepartmentID AS DepartmentID_Source, d.DepartmentName, f.FacultyName, d.Budget
    FROM University_OLTP.dbo.Department d
    INNER JOIN University_OLTP.dbo.Faculty f ON d.FacultyID = f.FacultyID
) AS source
ON target.DepartmentID_Source = source.DepartmentID_Source
WHEN NOT MATCHED THEN
    INSERT (DepartmentID_Source, DepartmentName, FacultyName, Budget)
    VALUES (source.DepartmentID_Source, source.DepartmentName, source.FacultyName, source.Budget);
GO

-- ======== 4. Load Dim_Course ========
MERGE INTO Dim_Course AS target
USING (
    SELECT c.CourseID AS CourseID_Source, c.CourseCode, c.CourseName, c.Credits,
           d.DepartmentName,
           CONCAT(t.FirstName, ' ', t.LastName) AS TeacherName,
           c.Semester, c.AcademicYear
    FROM University_OLTP.dbo.Course c
    INNER JOIN University_OLTP.dbo.Department d ON c.DepartmentID = d.DepartmentID
    LEFT JOIN University_OLTP.dbo.Teacher t ON c.TeacherID = t.TeacherID
) AS source
ON target.CourseID_Source = source.CourseID_Source
WHEN NOT MATCHED THEN
    INSERT (CourseID_Source, CourseCode, CourseName, Credits, DepartmentName, TeacherName, Semester, AcademicYear)
    VALUES (source.CourseID_Source, source.CourseCode, source.CourseName, source.Credits,
            source.DepartmentName, source.TeacherName, source.Semester, source.AcademicYear);
GO

-- ======== 5. Load Dim_Teacher ========
MERGE INTO Dim_Teacher AS target
USING (
    SELECT t.TeacherID AS TeacherID_Source,
           CONCAT(t.FirstName, ' ', t.LastName) AS TeacherName,
           t.Degree, d.DepartmentName, YEAR(t.HireDate) AS HireYear
    FROM University_OLTP.dbo.Teacher t
    INNER JOIN University_OLTP.dbo.Department d ON t.DepartmentID = d.DepartmentID
) AS source
ON target.TeacherID_Source = source.TeacherID_Source
WHEN NOT MATCHED THEN
    INSERT (TeacherID_Source, TeacherName, Degree, DepartmentName, HireYear)
    VALUES (source.TeacherID_Source, source.TeacherName, source.Degree, source.DepartmentName, source.HireYear);
GO

-- ======== 6. Load Bridge_CourseTeacher ========
MERGE INTO Bridge_CourseTeacher AS target
USING (
    SELECT dc.CourseKey, dt.TeacherKey
    FROM University_OLTP.dbo.Course c
    INNER JOIN Dim_Course dc ON c.CourseID = dc.CourseID_Source
    INNER JOIN University_OLTP.dbo.Teacher t ON c.TeacherID = t.TeacherID
    INNER JOIN Dim_Teacher dt ON t.TeacherID = dt.TeacherID_Source
) AS source
ON target.CourseKey = source.CourseKey AND target.TeacherKey = source.TeacherKey
WHEN NOT MATCHED THEN
    INSERT (CourseKey, TeacherKey) VALUES (source.CourseKey, source.TeacherKey);
GO

-- ======== 7. Load Fact_Enrollment ========
MERGE INTO Fact_Enrollment AS target
USING (
    SELECT
        ds.StudentKey, dc.CourseKey, dd.DateKey, ddim.DepartmentKey,
        1 AS EnrollmentCount, dc.Credits AS CreditsEnrolled,
        g.GradeValue,
        CASE WHEN g.GradeValue >= 5.0 THEN 1 ELSE 0 END AS GradePassed,
        CASE WHEN e.Status = 'Completed' THEN 1 ELSE 0 END AS IsCompleted,
        CASE WHEN e.Status = 'Dropped' THEN 1 ELSE 0 END AS DroppedOut
    FROM University_OLTP.dbo.Enrollment e
    INNER JOIN Dim_Student ds ON e.StudentID = ds.StudentID_Source AND ds.IsCurrent = 1
    INNER JOIN Dim_Course dc ON e.CourseID = dc.CourseID_Source
    INNER JOIN Dim_Date dd ON dd.FullDate = e.EnrollmentDate
    INNER JOIN Dim_Department ddim ON ddim.DepartmentName = (
        SELECT d.DepartmentName FROM University_OLTP.dbo.Course c
        INNER JOIN University_OLTP.dbo.Department d ON c.DepartmentID = d.DepartmentID
        WHERE c.CourseID = e.CourseID
    )
    LEFT JOIN University_OLTP.dbo.Grade g ON e.EnrollmentID = g.EnrollmentID AND g.GradeType = 'Final'
) AS source
ON target.StudentKey = source.StudentKey AND target.CourseKey = source.CourseKey
WHEN NOT MATCHED THEN
    INSERT (StudentKey, CourseKey, DateKey, DepartmentKey, EnrollmentCount, CreditsEnrolled,
            GradeValue, GradePassed, IsCompleted, DroppedOut)
    VALUES (source.StudentKey, source.CourseKey, source.DateKey, source.DepartmentKey,
            source.EnrollmentCount, source.CreditsEnrolled, source.GradeValue,
            source.GradePassed, source.IsCompleted, source.DroppedOut);
GO

-- ======== 8. Load Fact_Payment ========
MERGE INTO Fact_Payment AS target
USING (
    SELECT
        ds.StudentKey, dd.DateKey, ddim.DepartmentKey,
        p.Amount AS PaymentAmount,
        p.PaymentMethod, p.Semester, p.AcademicYear,
        CASE WHEN p.Amount >= 4500 THEN 1 ELSE 0 END AS IsFullPayment
    FROM University_OLTP.dbo.Payment p
    INNER JOIN Dim_Student ds ON p.StudentID = ds.StudentID_Source AND ds.IsCurrent = 1
    INNER JOIN Dim_Date dd ON dd.FullDate = p.PaymentDate
    INNER JOIN Dim_Department ddim ON ddim.DepartmentName = (
        SELECT d.DepartmentName FROM University_OLTP.dbo.Student s
        INNER JOIN University_OLTP.dbo.StudyProgram sp ON s.ProgramID = sp.ProgramID
        INNER JOIN University_OLTP.dbo.Department d ON sp.DepartmentID = d.DepartmentID
        WHERE s.StudentID = p.StudentID
    )
) AS source
ON target.StudentKey = source.StudentKey AND target.DateKey = source.DateKey
   AND target.PaymentAmount = source.PaymentAmount
WHEN NOT MATCHED THEN
    INSERT (StudentKey, DateKey, DepartmentKey, PaymentAmount, PaymentMethod, Semester, AcademicYear, IsFullPayment)
    VALUES (source.StudentKey, source.DateKey, source.DepartmentKey, source.PaymentAmount,
            source.PaymentMethod, source.Semester, source.AcademicYear, source.IsFullPayment);
GO
