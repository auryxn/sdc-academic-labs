-- ============================================================
-- Course Work: University Management System
-- OLAP Schema (Snowflake DWH: 2 Facts, 1 SCD Type 2, 1 Bridge)
-- ============================================================

CREATE DATABASE University_DWH;
GO

USE University_DWH;
GO

-- ==================== DIMENSIONS ====================

-- Dim_Date (regular)
CREATE TABLE Dim_Date (
    DateKey INT PRIMARY KEY,
    FullDate DATE NOT NULL,
    Day INT,
    Month INT,
    MonthName NVARCHAR(20),
    Quarter INT,
    Year INT,
    AcademicYear NVARCHAR(10)
);

-- Dim_Student (with SCD Type 2)
CREATE TABLE Dim_Student (
    StudentKey INT PRIMARY KEY IDENTITY(1,1),
    StudentID_Source INT NOT NULL,
    FirstName NVARCHAR(50),
    LastName NVARCHAR(50),
    Email NVARCHAR(100),
    ProgramName NVARCHAR(100),
    DegreeLevel NVARCHAR(50),
    DepartmentName NVARCHAR(100),
    FacultyName NVARCHAR(100),
    Status NVARCHAR(20),
    ValidFrom DATE NOT NULL,
    ValidTo DATE NULL,
    IsCurrent BIT DEFAULT 1
);

-- Dim_Department (regular)
CREATE TABLE Dim_Department (
    DepartmentKey INT PRIMARY KEY IDENTITY(1,1),
    DepartmentID_Source INT,
    DepartmentName NVARCHAR(100),
    FacultyName NVARCHAR(100),
    Budget DECIMAL(12,2)
);

-- Dim_Course (regular)
CREATE TABLE Dim_Course (
    CourseKey INT PRIMARY KEY IDENTITY(1,1),
    CourseID_Source INT,
    CourseCode NVARCHAR(20),
    CourseName NVARCHAR(100),
    Credits INT,
    DepartmentName NVARCHAR(100),
    TeacherName NVARCHAR(100),
    Semester NVARCHAR(20),
    AcademicYear INT
);

-- Dim_Teacher (bridge table target — but we also need a regular dim)
CREATE TABLE Dim_Teacher (
    TeacherKey INT PRIMARY KEY IDENTITY(1,1),
    TeacherID_Source INT,
    TeacherName NVARCHAR(100),
    Degree NVARCHAR(50),
    DepartmentName NVARCHAR(100),
    HireYear INT
);

-- ==================== BRIDGE TABLE ====================
-- Many-to-many: Course <-> Teacher (co-teaching, multiple teachers per course)

CREATE TABLE Bridge_CourseTeacher (
    BridgeID INT PRIMARY KEY IDENTITY(1,1),
    CourseKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Course(CourseKey),
    TeacherKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Teacher(TeacherKey),
    Role NVARCHAR(50) DEFAULT 'Primary',
    CONSTRAINT UQ_CourseTeacher UNIQUE (CourseKey, TeacherKey)
);

-- ==================== FACT TABLES ====================

-- Fact 1: Enrollment Fact (student enrollment analytics)
CREATE TABLE Fact_Enrollment (
    EnrollmentKey INT PRIMARY KEY IDENTITY(1,1),
    StudentKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Student(StudentKey),
    CourseKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Course(CourseKey),
    DateKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Date(DateKey),
    DepartmentKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Department(DepartmentKey),
    EnrollmentCount INT DEFAULT 1,
    CreditsEnrolled INT,
    GradeValue DECIMAL(4,2),
    GradePassed BIT,
    IsCompleted BIT,
    DroppedOut BIT
);

-- Fact 2: Payment Fact (financial analytics)
CREATE TABLE Fact_Payment (
    PaymentKey INT PRIMARY KEY IDENTITY(1,1),
    StudentKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Student(StudentKey),
    DateKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Date(DateKey),
    DepartmentKey INT NOT NULL FOREIGN KEY REFERENCES Dim_Department(DepartmentKey),
    PaymentAmount DECIMAL(10,2),
    PaymentMethod NVARCHAR(50),
    Semester NVARCHAR(20),
    AcademicYear INT,
    IsFullPayment BIT
);
