-- ============================================================
-- Course Work: HR Management System
-- OLAP Schema — Snowflake DWH
-- 2 Facts, 1 SCD Type 2, 1 Bridge Table
-- ============================================================

CREATE DATABASE IF NOT EXISTS hr_dwh;
USE hr_dwh;

-- ===== DIMENSIONS =====

-- 1. Dim_Department
CREATE TABLE Dim_Department (
    department_sk INT PRIMARY KEY AUTO_INCREMENT,
    department_id INT,
    department_name VARCHAR(100),
    location VARCHAR(200),
    effective_date DATE NOT NULL,
    is_current BOOLEAN DEFAULT TRUE
);

-- 2. Dim_Position
CREATE TABLE Dim_Position (
    position_sk INT PRIMARY KEY AUTO_INCREMENT,
    position_id INT,
    position_title VARCHAR(100),
    min_salary DECIMAL(10,2),
    max_salary DECIMAL(10,2),
    effective_date DATE NOT NULL,
    is_current BOOLEAN DEFAULT TRUE
);

-- 3. Dim_Employee (SCD Type 2 — track salary/position changes)
CREATE TABLE Dim_Employee (
    employee_sk INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    position_title VARCHAR(100),
    department_name VARCHAR(100),
    salary DECIMAL(10,2),
    status VARCHAR(20),
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN DEFAULT TRUE
);

-- 4. Dim_Date
CREATE TABLE Dim_Date (
    date_sk INT PRIMARY KEY,
    full_date DATE NOT NULL,
    year INT NOT NULL,
    quarter INT NOT NULL,
    month INT NOT NULL,
    month_name VARCHAR(20),
    week INT,
    day_of_week INT,
    day_name VARCHAR(20),
    is_weekend BOOLEAN
);

-- 5. Dim_Candidate
CREATE TABLE Dim_Candidate (
    candidate_sk INT PRIMARY KEY AUTO_INCREMENT,
    candidate_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    source VARCHAR(50),
    effective_date DATE NOT NULL,
    is_current BOOLEAN DEFAULT TRUE
);

-- 6. Dim_Training
CREATE TABLE Dim_Training (
    training_sk INT PRIMARY KEY AUTO_INCREMENT,
    program_id INT,
    program_name VARCHAR(200),
    duration_days INT,
    cost DECIMAL(10,2),
    effective_date DATE NOT NULL,
    is_current BOOLEAN DEFAULT TRUE
);

-- 7. Dim_Campaign
CREATE TABLE Dim_Campaign (
    campaign_sk INT PRIMARY KEY AUTO_INCREMENT,
    campaign_id INT,
    campaign_name VARCHAR(200),
    channel VARCHAR(100),
    budget DECIMAL(12,2),
    effective_date DATE NOT NULL,
    is_current BOOLEAN DEFAULT TRUE
);

-- ===== BRIDGE TABLE =====
-- Many-to-many: Employee ↔ Training
CREATE TABLE Bridge_Employee_Training (
    bridge_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_sk INT NOT NULL,
    training_sk INT NOT NULL,
    enrollment_count INT DEFAULT 1,
    FOREIGN KEY (employee_sk) REFERENCES Dim_Employee(employee_sk),
    FOREIGN KEY (training_sk) REFERENCES Dim_Training(training_sk)
);

-- ===== FACTS =====

-- Fact 1: Employee Fact (aggregated — monthly headcount, salary cost by dept/position)
CREATE TABLE Fact_Employee_Snapshot (
    fact_id INT PRIMARY KEY AUTO_INCREMENT,
    date_sk INT NOT NULL,
    department_sk INT NOT NULL,
    position_sk INT NOT NULL,
    employee_count INT NOT NULL,
    total_salary DECIMAL(14,2) NOT NULL,
    avg_salary DECIMAL(10,2),
    active_count INT,
    FOREIGN KEY (date_sk) REFERENCES Dim_Date(date_sk),
    FOREIGN KEY (department_sk) REFERENCES Dim_Department(department_sk),
    FOREIGN KEY (position_sk) REFERENCES Dim_Position(position_sk)
);

-- Fact 2: Recruitment Fact
CREATE TABLE Fact_Recruitment (
    fact_id INT PRIMARY KEY AUTO_INCREMENT,
    date_sk INT NOT NULL,
    campaign_sk INT NOT NULL,
    candidate_sk INT NOT NULL,
    position_sk INT NOT NULL,
    candidate_count INT DEFAULT 1,
    hire_count INT DEFAULT 0,
    interview_count INT DEFAULT 0,
    avg_score DECIMAL(5,2),
    cost_per_hire DECIMAL(10,2),
    FOREIGN KEY (date_sk) REFERENCES Dim_Date(date_sk),
    FOREIGN KEY (campaign_sk) REFERENCES Dim_Campaign(campaign_sk),
    FOREIGN KEY (candidate_sk) REFERENCES Dim_Candidate(candidate_sk),
    FOREIGN KEY (position_sk) REFERENCES Dim_Position(position_sk)
);

-- Fill Dim_Date (2020-2027)
DELIMITER //
CREATE PROCEDURE fill_dim_date(IN start_year INT, IN end_year INT)
BEGIN
    DECLARE d DATE;
    SET d = DATE(CONCAT(start_year, '-01-01'));
    WHILE d < DATE(CONCAT(end_year + 1, '-01-01')) DO
        INSERT IGNORE INTO Dim_Date (date_sk, full_date, year, quarter, month, month_name, 
                                     week, day_of_week, day_name, is_weekend)
        VALUES (
            YEAR(d) * 10000 + MONTH(d) * 100 + DAY(d),
            d,
            YEAR(d),
            CEIL(MONTH(d) / 3.0),
            MONTH(d),
            MONTHNAME(d),
            WEEK(d, 1),
            DAYOFWEEK(d),
            DAYNAME(d),
            CASE WHEN DAYOFWEEK(d) IN (1, 7) THEN TRUE ELSE FALSE END
        );
        SET d = DATE_ADD(d, INTERVAL 1 DAY);
    END WHILE;
END //
DELIMITER ;

CALL fill_dim_date(2020, 2027);

SELECT 'OLAP schema created successfully!' AS message;
