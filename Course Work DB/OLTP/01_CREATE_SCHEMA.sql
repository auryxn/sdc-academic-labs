-- ============================================================
-- Course Work: University Management System
-- OLTP Schema (3NF, 10 tables)
-- ============================================================

CREATE DATABASE University_OLTP;
GO

USE University_OLTP;
GO

-- 1. Faculties
CREATE TABLE Faculty (
    FacultyID INT PRIMARY KEY IDENTITY(1,1),
    FacultyName NVARCHAR(100) NOT NULL,
    DeanName NVARCHAR(100) NOT NULL,
    Phone NVARCHAR(20),
    EstablishedYear INT
);

-- 2. Departments
CREATE TABLE Department (
    DepartmentID INT PRIMARY KEY IDENTITY(1,1),
    DepartmentName NVARCHAR(100) NOT NULL,
    FacultyID INT NOT NULL FOREIGN KEY REFERENCES Faculty(FacultyID),
    HeadName NVARCHAR(100),
    Budget DECIMAL(12,2)
);

-- 3. Study Programs
CREATE TABLE StudyProgram (
    ProgramID INT PRIMARY KEY IDENTITY(1,1),
    ProgramName NVARCHAR(100) NOT NULL,
    DepartmentID INT NOT NULL FOREIGN KEY REFERENCES Department(DepartmentID),
    DegreeLevel NVARCHAR(50) CHECK (DegreeLevel IN ('Bachelor', 'Master', 'PhD')),
    DurationYears INT,
    TuitionFee DECIMAL(10,2)
);

-- 4. Students
CREATE TABLE Student (
    StudentID INT PRIMARY KEY IDENTITY(1,1),
    FirstName NVARCHAR(50) NOT NULL,
    LastName NVARCHAR(50) NOT NULL,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    Phone NVARCHAR(20),
    DateOfBirth DATE,
    EnrollmentDate DATE NOT NULL,
    ProgramID INT NOT NULL FOREIGN KEY REFERENCES StudyProgram(ProgramID),
    Status NVARCHAR(20) CHECK (Status IN ('Active', 'Graduated', 'Dropped', 'Suspended'))
);

-- 5. Teachers
CREATE TABLE Teacher (
    TeacherID INT PRIMARY KEY IDENTITY(1,1),
    FirstName NVARCHAR(50) NOT NULL,
    LastName NVARCHAR(50) NOT NULL,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    Phone NVARCHAR(20),
    DepartmentID INT NOT NULL FOREIGN KEY REFERENCES Department(DepartmentID),
    HireDate DATE NOT NULL,
    Degree NVARCHAR(50),
    Salary DECIMAL(10,2)
);

-- 6. Courses
CREATE TABLE Course (
    CourseID INT PRIMARY KEY IDENTITY(1,1),
    CourseCode NVARCHAR(20) UNIQUE NOT NULL,
    CourseName NVARCHAR(100) NOT NULL,
    Credits INT NOT NULL CHECK (Credits > 0),
    DepartmentID INT NOT NULL FOREIGN KEY REFERENCES Department(DepartmentID),
    TeacherID INT FOREIGN KEY REFERENCES Teacher(TeacherID),
    MaxStudents INT,
    Semester NVARCHAR(20) NOT NULL,
    AcademicYear INT NOT NULL
);

-- 7. Enrollments
CREATE TABLE Enrollment (
    EnrollmentID INT PRIMARY KEY IDENTITY(1,1),
    StudentID INT NOT NULL FOREIGN KEY REFERENCES Student(StudentID),
    CourseID INT NOT NULL FOREIGN KEY REFERENCES Course(CourseID),
    EnrollmentDate DATE NOT NULL,
    Status NVARCHAR(20) CHECK (Status IN ('Enrolled', 'Completed', 'Dropped')),
    CONSTRAINT UQ_Student_Course UNIQUE (StudentID, CourseID)
);

-- 8. Grades
CREATE TABLE Grade (
    GradeID INT PRIMARY KEY IDENTITY(1,1),
    EnrollmentID INT NOT NULL FOREIGN KEY REFERENCES Enrollment(EnrollmentID),
    GradeValue DECIMAL(4,2) CHECK (GradeValue >= 1.0 AND GradeValue <= 10.0),
    GradeDate DATE NOT NULL,
    GradeType NVARCHAR(50) CHECK (GradeType IN ('Midterm', 'Final', 'Project', 'Lab', 'Exam'))
);

-- 9. Payments
CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY IDENTITY(1,1),
    StudentID INT NOT NULL FOREIGN KEY REFERENCES Student(StudentID),
    Amount DECIMAL(10,2) NOT NULL,
    PaymentDate DATE NOT NULL,
    PaymentMethod NVARCHAR(50) CHECK (PaymentMethod IN ('Bank Transfer', 'Credit Card', 'Cash', 'Scholarship')),
    Semester NVARCHAR(20) NOT NULL,
    AcademicYear INT NOT NULL
);

-- 10. Attendance
CREATE TABLE Attendance (
    AttendanceID INT PRIMARY KEY IDENTITY(1,1),
    StudentID INT NOT NULL FOREIGN KEY REFERENCES Student(StudentID),
    CourseID INT NOT NULL FOREIGN KEY REFERENCES Course(CourseID),
    Date DATE NOT NULL,
    Status NVARCHAR(20) CHECK (Status IN ('Present', 'Absent', 'Late', 'Excused')),
    CONSTRAINT UQ_Student_Course_Date UNIQUE (StudentID, CourseID, Date)
);
