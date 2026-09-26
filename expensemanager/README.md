# Personal Expense & Budget Manager

A robust, modular, object-oriented Java application built to track daily financial transactions, monitor category-wise spending, and perform automated budget analytics. Designed using a clean 3-tier architecture, file persistence, and verified with JUnit 5 unit testing.

---

## 🏗️ Architectural Overview & Design

The project follows a strict **Layered Architecture (3-Tier)** to decouple business logic, data persistence, and domain models:

┌──────────────────────────────────────────────────────────┐
│              Business Logic Layer (Service)              │
│       • ExpenseService.java                              │
│         - Financial calculations & budget analytics      │
│         - Java Streams aggregations & grouping           │
└────────────────────────────┬─────────────────────────────┘
│
▼
┌──────────────────────────────────────────────────────────┐
│            Data / Persistence Layer (Repository)         │
│       • TransactionRepository.java                       │
│         - File I/O serialization and data loading        │
│       • Transaction.java (Domain Entity)                 │
│       • Category.java (Enumeration)                      │
└────────────────────────────┴─────────────────────────────┘

### Key Technical Highlights
* **Separation of Concerns:** Business rules in `ExpenseService` remain independent of disk storage mechanisms in `TransactionRepository`.
* **Functional Streams API:** Utilizes Java Streams for real-time aggregations (totaling expenses/incomes, finding top spending categories, and grouping by category).
* **Test Isolation:** The JUnit 5 test suite manages setup state explicitly (`@BeforeEach`), ensuring clean isolation from persisted disk storage.

---

## 🛠️ Tech Stack & Prerequisites

* **Language:** Java 17+ (JDK)
* **Testing:** JUnit 5 (Jupiter)
* **Persistence:** Custom file storage using Java NIO / File I/O
* **Tooling:** PowerShell / Java CLI (`javac` & `java`)

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone [https://github.com/YOUR_GITHUB_USERNAME/ExpenseManager.git](https://github.com/YOUR_GITHUB_USERNAME/ExpenseManager.git)
cd ExpenseManager

2. Compile the Project
Compile all Java source files from the project root:

PowerShell
javac -cp ".;junit-platform-console-standalone-1.10.2.jar;.." *.java

3. Run Automated Unit Tests
Execute the JUnit 5 test suite using the standalone console launcher:

PowerShell
java -jar junit-platform-console-standalone-1.10.2.jar execute --class-path ".;.." --select-class expensemanager.ExpenseServiceTest

📊 Core Features
[x] Transaction Management: Track incomes and expenses with unique IDs, dates, categories, and descriptions.

[x] Financial Analytics: Calculate real-time totals for income, expenses, and net remaining budget.

[x] Category Breakdown: Group and aggregate spending dynamically by budget category.

[x] Top Expense Driver: Automatically identify peak spending categories using stream reduction.

[x] Data Persistence: Automatically save and reload transaction histories across application sessions.

[x] Automated Testing: Comprehensive test coverage covering business logic and edge cases.