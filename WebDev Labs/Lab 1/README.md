# SubZero: Digital Subscription Tracker & Optimization Service

## 🎯 Overview
**SubZero** is a robust enterprise-grade backend application built with Spring Boot, designed to help users track, manage, and optimize their recurring digital subscriptions (SaaS, streaming, software licenses, etc.).

The project focuses on preventing "forgotten" renewals, providing detailed expense analytics across multiple currencies, and offering recommendations for subscription management.

## 🚀 Key Features
- **Subscription Management:** CRUD operations for user subscriptions.
- **Renewal Alerts:** Automated notifications before billing dates (Scheduled Tasks).
- **Expense Analytics:** Dashboard endpoints to view spending by month/category.
- **Multi-Currency Support:** Automatic currency conversion for international SaaS tools.
- **Optimization Engine:** Logic to identify overlapping or underused services.

## 🛠️ Tech Stack
- **Framework:** Spring Boot 3.3+
- **Persistence:** Spring Data JPA + PostgreSQL (Hibernate)
- **API Architecture:** RESTful API
- **Tooling:** Maven, Java 17+, Lombok
- **Documentation:** Swagger/OpenAPI (Planned)

## 📊 Domain Model (Draft)
- **User:** Primary account entity.
- **Subscription:** Core entity containing billing cycle, price, and renewal dates.
- **ServiceCategory:** (e.g., Streaming, Cloud Hosting, Productivity).
- **BillingLog:** History of transactions per subscription.

---
**Course:** Java Development & Spring Framework
**Lab 1:** Kickoff & Project Initialization
**Deadline:** 2026-04-01
