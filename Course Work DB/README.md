# Course Work: University Management System
## Database Design & Analytical Solution

**Author:** Aliaksei Ioda
**Date:** 2026
**DBMS:** Microsoft SQL Server

---

## 1. Topic Overview

This course project implements a complete database solution for a **University Management System**. The system tracks students, teachers, courses, enrollments, grades, payments, and attendance across multiple faculties and departments.

---

## 2. OLTP Database Context

**Database:** `University_OLTP`

The OLTP database stores operational transactional data for day-to-day university operations.

### 2.1 Tables (10 tables, 3NF)

| # | Table | Description | Key Facts |
|---|---|---|---|
| 1 | **Faculty** | University faculties (e.g., Computer Science, Medicine) | |
| 2 | **Department** | Departments within faculties | Foreign Key → Faculty |
| 3 | **StudyProgram** | Degree programs (BSc, MSc, PhD) | FK → Department |
| 4 | **Student** | Student personal data and enrollment | FK → StudyProgram |
| 5 | **Teacher** | Teacher personal and employment data | FK → Department |
| 6 | **Course** | Courses offered per semester | FK → Department, Teacher |
| 7 | **Enrollment** | Student-course registrations | FK → Student, Course (unique pair) |
| 8 | **Grade** | Student grades (Midterm, Final, Lab, etc.) | FK → Enrollment |
| 9 | **Payment** | Tuition and fee payments | FK → Student |
| 10 | **Attendance** | Per-class attendance records | FK → Student, Course |

### 2.2 Relationships

```
Faculty 1────N Department 1────N StudyProgram 1────N Student
                                  Department 1────N Teacher
                                  Department 1────N Course N────M Enrollment N────M Grade
  Student 1────N Enrollment
  Student 1────N Payment
  Student 1────N Attendance
  Course 1────N Attendance
```

---

## 3. OLAP Database Context

**Database:** `University_DWH`

The DWH answers analytical questions:
- How many students enrolled per department per month?
- Which faculties generate the most revenue?
- What is the pass rate per course?
- How many credits are managed by each teacher?
- What is the student distribution by degree level and status?

### 3.1 Schema (Snowflake)

**5 Dimensions:**
- **Dim_Date** — Date hierarchy (Day → Month → Quarter → Year → AcademicYear)
- **Dim_Student** — SCD Type 2: tracks student status changes over time (Active/Graduated/Dropped)
- **Dim_Department** — Department + Faculty hierarchy
- **Dim_Course** — Course details with teacher assignment
- **Dim_Teacher** — Teacher information

**1 Bridge Table:**
- **Bridge_CourseTeacher** — Many-to-many relationship between courses and teachers (co-teaching support)

**2 Fact Tables:**
- **Fact_Enrollment** — Measures: EnrollmentCount, CreditsEnrolled, GradeValue, GradePassed
- **Fact_Payment** — Measures: PaymentAmount, IsFullPayment

### 3.2 SCD Type 2 Implementation

`Dim_Student` tracks historical changes:
- When a student changes status (Active → Dropped) or program, the old record is closed (ValidTo = yesterday, IsCurrent = 0) and a new record is inserted.
- All fact tables reference the current snapshot via `IsCurrent = 1`.

---

## 4. Instructions to Run

### Prerequisites
- SQL Server (2019+ or Azure SQL)
- SQL Server Management Studio (SSMS)

### Step-by-step

```sql
-- Step 1: Create OLTP database and tables
-- Run: Course Work DB/OLTP/01_CREATE_SCHEMA.sql

-- Step 2: Load CSV data into OLTP
-- Note: Update CSV file paths in 02_LOAD_DATA.sql before running
-- Run: Course Work DB/OLTP/02_LOAD_DATA.sql

-- Step 3: Create DWH database and dimensions/facts
-- Run: Course Work DB/OLAP/01_CREATE_DWH_SCHEMA.sql

-- Step 4: Run ETL from OLTP to DWH
-- Run: Course Work DB/ETL/03_ETL_OLTP_to_DWH.sql

-- Step 5: Run analytical queries
-- Run OLTP queries: Course Work DB/Queries/04_OLTP_QUERIES.sql
-- Run OLAP queries: Course Work DB/Queries/05_OLAP_QUERIES.sql
```

### CSV Data Files

Place CSV files in `C:\CSV\` (or update paths in the load script):
- `faculty.csv`
- `department.csv`
- `study_program.csv`
- `student.csv`
- `teacher.csv`
- `course.csv`

---

## 5. Power BI Report

**File:** `Course Work DB/Power BI/University_Report.pbix`

### Report Pages

#### Page 1: Overview
- **Title:** University Dashboard
- **Slicers:** Academic Year, Faculty
- **Visuals:**
  1. Card: Total Active Students
  2. Card: Total Revenue (Year)
  3. Stacked Bar Chart: Enrollments by Department
  4. Line Chart: Payment Trends by Month
  5. Map: Student distribution (by city/location — country-level)

#### Page 2: Academic Performance
- **Slicers:** Degree Level, Semester
- **Visuals:**
  1. Clustered Column Chart: Average Grade by Department
  2. Pie Chart: Pass/Fail Ratio by Course
  3. Table: Top 10 Students by GPA

#### Page 3: Financial Analytics
- **Slicers:** Payment Method, Year
- **Visuals:**
  1. Waterfall Chart: Revenue by Faculty
  2. Matrix: Payment Amount by Department × Month
  3. Donut Chart: Payment Method Distribution

---

## 6. Files Submitted

```
Course Work DB/
├── OLTP/
│   ├── 01_CREATE_SCHEMA.sql              -- OLTP tables creation
│   └── 02_LOAD_DATA.sql                  -- CSV data loading (rerunnable)
├── OLAP/
│   └── 01_CREATE_DWH_SCHEMA.sql          -- DWH snowflake schema
├── CSV Data/
│   ├── faculty.csv
│   ├── department.csv
│   ├── study_program.csv
│   ├── student.csv
│   ├── teacher.csv
│   └── course.csv
├── ETL/
│   └── 03_ETL_OLTP_to_DWH.sql            -- Full ETL pipeline
├── Queries/
│   ├── 04_OLTP_QUERIES.sql               -- 3 OLTP analytical queries
│   └── 05_OLAP_QUERIES.sql               -- 3 OLAP analytical queries
├── Power BI/
│   └── University_Report.pbix            -- Power BI dashboard
└── README.md
```
