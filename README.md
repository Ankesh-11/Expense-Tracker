# 💸 Expense Tracker Console Application (Java)

This is a simple **console-based Expense Tracker** built using Java. It allows users to **add incomes and expenses**, **view monthly summaries**, and **save/load data** to/from CSV files. The application ensures input validation, clean structure, and provides a user-friendly terminal interface.

---

## 📂 Features

- ✅ Add Income and Expense transactions
- 📅 Record transaction date (validated)
- 📊 View monthly summaries
- 💾 Save and Load transactions using CSV
- 🔐 Category validation for income and expenses
- 📁 Data persistence via CSV file
- 🚫 Exception handling for invalid input or corrupt data

---

## 📌 Categories Allowed

### Income Categories:
- `Salary`
- `Business`

### Expense Categories:
- `Food`
- `Rent`
- `Travel`

> Only the above categories are allowed. Invalid inputs will be rejected.

---

## 🛠️ Technologies Used

- Java 8+
- Standard I/O
- `java.time` package (for date parsing)
- File I/O (`BufferedReader`, `FileWriter`)
- Basic OOP structure (`Transaction` class, constants, utilities)

---

## 🚀 How to Run

1. **Clone this repository:**

```bash
git clone https://github.com/Ankesh-11/Expense-Tracker.git
cd expense
