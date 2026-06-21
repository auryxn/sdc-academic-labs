-- ============================================================
-- Course Work: HR Management System
-- OLTP Schema (3NF)
-- ============================================================

CREATE DATABASE IF NOT EXISTS hr_oltp;
USE hr_oltp;

-- 1. Departments
CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(100) NOT NULL,
    location VARCHAR(200),
    manager_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Positions
CREATE TABLE positions (
    position_id INT PRIMARY KEY AUTO_INCREMENT,
    position_title VARCHAR(100) NOT NULL,
    department_id INT NOT NULL,
    min_salary DECIMAL(10,2),
    max_salary DECIMAL(10,2),
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- 3. Employees
CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    hire_date DATE NOT NULL,
    position_id INT NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    manager_id INT,
    status ENUM('active', 'inactive', 'terminated') DEFAULT 'active',
    FOREIGN KEY (position_id) REFERENCES positions(position_id),
    FOREIGN KEY (manager_id) REFERENCES employees(employee_id)
);

ALTER TABLE departments ADD FOREIGN KEY (manager_id) REFERENCES employees(employee_id);

-- 4. Candidates
CREATE TABLE candidates (
    candidate_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    position_id INT NOT NULL,
    status ENUM('new', 'screening', 'interview', 'offer', 'hired', 'rejected') DEFAULT 'new',
    applied_date DATE NOT NULL,
    source VARCHAR(50),
    FOREIGN KEY (position_id) REFERENCES positions(position_id)
);

-- 5. Interviews
CREATE TABLE interviews (
    interview_id INT PRIMARY KEY AUTO_INCREMENT,
    candidate_id INT NOT NULL,
    interviewer_id INT NOT NULL,
    interview_date DATETIME NOT NULL,
    type ENUM('phone', 'technical', 'hr', 'final') NOT NULL,
    score DECIMAL(5,2),
    notes TEXT,
    result ENUM('pass', 'fail', 'pending') DEFAULT 'pending',
    FOREIGN KEY (candidate_id) REFERENCES candidates(candidate_id),
    FOREIGN KEY (interviewer_id) REFERENCES employees(employee_id)
);

-- 6. Applications (many-to-many: candidate can apply to multiple positions)
CREATE TABLE applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    candidate_id INT NOT NULL,
    position_id INT NOT NULL,
    application_date DATE NOT NULL,
    status ENUM('submitted', 'reviewed', 'shortlisted', 'rejected') DEFAULT 'submitted',
    notes TEXT,
    FOREIGN KEY (candidate_id) REFERENCES candidates(candidate_id),
    FOREIGN KEY (position_id) REFERENCES positions(position_id)
);

-- 7. Training Programs
CREATE TABLE training_programs (
    program_id INT PRIMARY KEY AUTO_INCREMENT,
    program_name VARCHAR(200) NOT NULL,
    description TEXT,
    duration_days INT,
    cost DECIMAL(10,2),
    start_date DATE
);

-- 8. Employee Training (many-to-many)
CREATE TABLE employee_training (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    program_id INT NOT NULL,
    enrollment_date DATE NOT NULL,
    completion_date DATE,
    status ENUM('enrolled', 'in_progress', 'completed', 'dropped') DEFAULT 'enrolled',
    score DECIMAL(5,2),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    FOREIGN KEY (program_id) REFERENCES training_programs(program_id)
);

-- 9. Performance Reviews
CREATE TABLE performance_reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT NOT NULL,
    review_date DATE NOT NULL,
    reviewer_id INT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comments TEXT,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    FOREIGN KEY (reviewer_id) REFERENCES employees(employee_id)
);

-- 10. Recruitment Campaigns
CREATE TABLE recruitment_campaigns (
    campaign_id INT PRIMARY KEY AUTO_INCREMENT,
    campaign_name VARCHAR(200) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    budget DECIMAL(12,2),
    channel VARCHAR(100),
    total_applications INT DEFAULT 0
);

-- Indexes for performance
CREATE INDEX idx_employee_status ON employees(status);
CREATE INDEX idx_candidate_status ON candidates(status);
CREATE INDEX idx_interview_date ON interviews(interview_date);
CREATE INDEX idx_application_status ON applications(status);
