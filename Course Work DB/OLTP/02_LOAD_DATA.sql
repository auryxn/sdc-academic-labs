-- ============================================================
-- Course Work: University Management System
-- Load CSV data into OLTP (rerunnable, no duplicates)
-- ============================================================

USE University_OLTP;
GO

-- Helper: check and load Faculty
MERGE INTO Faculty AS target
USING (
    SELECT TRIM(FacultyName) AS FacultyName, TRIM(DeanName) AS DeanName,
           TRIM(Phone) AS Phone, TRY_CAST(EstablishedYear AS INT) AS EstablishedYear
    FROM OPENROWSET(BULK 'C:\CSV\faculty.csv', FORMAT='CSV', FIRSTROW=2) AS src
) AS source
ON target.FacultyName = source.FacultyName
WHEN NOT MATCHED THEN
    INSERT (FacultyName, DeanName, Phone, EstablishedYear)
    VALUES (source.FacultyName, source.DeanName, source.Phone, source.EstablishedYear);
GO

-- Load Departments
MERGE INTO Department AS target
USING (
    SELECT TRIM(src.DepartmentName) AS DepartmentName, f.FacultyID,
           TRIM(src.HeadName) AS HeadName, TRY_CAST(src.Budget AS DECIMAL(12,2)) AS Budget
    FROM OPENROWSET(BULK 'C:\CSV\department.csv', FORMAT='CSV', FIRSTROW=2) AS src
    INNER JOIN Faculty f ON f.FacultyName = TRIM(src.FacultyName)
) AS source
ON target.DepartmentName = source.DepartmentName
WHEN NOT MATCHED THEN
    INSERT (DepartmentName, FacultyID, HeadName, Budget)
    VALUES (source.DepartmentName, source.FacultyID, source.HeadName, source.Budget);
GO

-- Load Study Programs
MERGE INTO StudyProgram AS target
USING (
    SELECT TRIM(src.ProgramName) AS ProgramName, d.DepartmentID,
           TRIM(src.DegreeLevel) AS DegreeLevel,
           TRY_CAST(src.DurationYears AS INT) AS DurationYears,
           TRY_CAST(src.TuitionFee AS DECIMAL(10,2)) AS TuitionFee
    FROM OPENROWSET(BULK 'C:\CSV\study_program.csv', FORMAT='CSV', FIRSTROW=2) AS src
    INNER JOIN Department d ON d.DepartmentName = TRIM(src.DepartmentName)
) AS source
ON target.ProgramName = source.ProgramName
WHEN NOT MATCHED THEN
    INSERT (ProgramName, DepartmentID, DegreeLevel, DurationYears, TuitionFee)
    VALUES (source.ProgramName, source.DepartmentID, source.DegreeLevel, source.DurationYears, source.TuitionFee);
GO

-- Load Students
MERGE INTO Student AS target
USING (
    SELECT TRIM(src.FirstName) AS FirstName, TRIM(src.LastName) AS LastName,
           TRIM(src.Email) AS Email, TRIM(src.Phone) AS Phone,
           TRY_CAST(src.DateOfBirth AS DATE) AS DateOfBirth,
           TRY_CAST(src.EnrollmentDate AS DATE) AS EnrollmentDate,
           sp.ProgramID, TRIM(src.Status) AS Status
    FROM OPENROWSET(BULK 'C:\CSV\student.csv', FORMAT='CSV', FIRSTROW=2) AS src
    INNER JOIN StudyProgram sp ON sp.ProgramName = TRIM(src.ProgramName)
) AS source
ON target.Email = source.Email
WHEN NOT MATCHED THEN
    INSERT (FirstName, LastName, Email, Phone, DateOfBirth, EnrollmentDate, ProgramID, Status)
    VALUES (source.FirstName, source.LastName, source.Email, source.Phone, source.DateOfBirth,
            source.EnrollmentDate, source.ProgramID, source.Status);
GO

-- Load Teachers
MERGE INTO Teacher AS target
USING (
    SELECT TRIM(src.FirstName) AS FirstName, TRIM(src.LastName) AS LastName,
           TRIM(src.Email) AS Email, TRIM(src.Phone) AS Phone,
           d.DepartmentID, TRY_CAST(src.HireDate AS DATE) AS HireDate,
           TRIM(src.Degree) AS Degree, TRY_CAST(src.Salary AS DECIMAL(10,2)) AS Salary
    FROM OPENROWSET(BULK 'C:\CSV\teacher.csv', FORMAT='CSV', FIRSTROW=2) AS src
    INNER JOIN Department d ON d.DepartmentName = TRIM(src.DepartmentName)
) AS source
ON target.Email = source.Email
WHEN NOT MATCHED THEN
    INSERT (FirstName, LastName, Email, Phone, DepartmentID, HireDate, Degree, Salary)
    VALUES (source.FirstName, source.LastName, source.Email, source.Phone, source.DepartmentID,
            source.HireDate, source.Degree, source.Salary);
GO

-- Load Courses
MERGE INTO Course AS target
USING (
    SELECT TRIM(src.CourseCode) AS CourseCode, TRIM(src.CourseName) AS CourseName,
           TRY_CAST(src.Credits AS INT) AS Credits,
           d.DepartmentID, t.TeacherID,
           TRY_CAST(src.MaxStudents AS INT) AS MaxStudents,
           TRIM(src.Semester) AS Semester, TRY_CAST(src.AcademicYear AS INT) AS AcademicYear
    FROM OPENROWSET(BULK 'C:\CSV\course.csv', FORMAT='CSV', FIRSTROW=2) AS src
    INNER JOIN Department d ON d.DepartmentName = TRIM(src.DepartmentName)
    INNER JOIN Teacher t ON t.Email = TRIM(src.TeacherEmail)
) AS source
ON target.CourseCode = source.CourseCode
WHEN NOT MATCHED THEN
    INSERT (CourseCode, CourseName, Credits, DepartmentID, TeacherID, MaxStudents, Semester, AcademicYear)
    VALUES (source.CourseCode, source.CourseName, source.Credits, source.DepartmentID,
            source.TeacherID, source.MaxStudents, source.Semester, source.AcademicYear);
GO
