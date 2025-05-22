package expense.tracker;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static expense.tracker.Constants.*;

public class ExpenseTracker {

    private static final List<Transaction> transactions = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = readMenuChoice();
            switch (choice) {
                case 1 -> addIncome();
                case 2 -> addExpense();
                case 3 -> displaySummary();
                case 4 -> loadTransactions();
                case 5 -> saveTransactions();
                case 6 -> exitApplication();
                default -> System.out.println("❌ Invalid choice. Please select between 1 and 6.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n===== Expense Tracker Menu =====");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. View Summary");
        System.out.println("4. Load from CSV");
        System.out.println("5. Save to CSV");
        System.out.println("6. Exit");
        System.out.print("Select an option (1-6): ");
    }

    private static int readMenuChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addIncome() {
        System.out.println("\n=== Add Income ===");
        LocalDate date = readValidDate(Constants.DATE_FORMAT_HINT);
        double amount = readPositiveDouble("Enter amount: ");
        String category = readValidIncomeCategory();
        transactions.add(new Transaction(date, amount, category, true));
        System.out.println("✅ Income added successfully!");
    }

    private static void addExpense() {
        System.out.println("\n=== Add Expense ===");
        LocalDate date = readValidDate(Constants.DATE_FORMAT_HINT);
        double amount = readPositiveDouble("Enter amount: ");
        String category = readValidExpensesCategory();
        transactions.add(new Transaction(date, amount, category,false));
        System.out.println("✅ Expense added successfully!");
    }

    private static void displaySummary() {
        System.out.println("\n=== Monthly Summary ===");
        Map<String, List<Transaction>> grouped = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getDate().getMonth() + " " + t.getDate().getYear()));

        if (grouped.isEmpty()) {
            System.out.println("⚠ No transactions found.");
            return;
        }

        grouped.forEach((month, txns) -> {
            double income = txns.stream().filter(Transaction::isIncome).mapToDouble(Transaction::getAmount).sum();
            double expenses = txns.stream().filter(t -> !t.isIncome()).mapToDouble(Transaction::getAmount).sum();
            double balance = income - expenses;

            System.out.printf("%s - Income: %.2f | Expenses: %.2f | Balance: %.2f%n", month, income, expenses, balance);
        });
    }

    private static void loadTransactions() {
        String filePath = readNonEmptyInput("Enter path of CSV file to load: ");
        File file = new File(filePath);

        if (!file.exists() || !filePath.toLowerCase().endsWith(".csv") || file.isDirectory()) {
            System.out.println("\u274C Invalid file. Ensure it exists and is a valid .csv file.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 4) continue;
                Transaction t = new Transaction(
                        LocalDate.parse(data[0], DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        Double.parseDouble(data[1]),
                        data[2],
                        Boolean.parseBoolean(data[3])
                );

                transactions.add(t);
                count++;
            }
            System.out.println("\u2705 Loaded " + count + " transactions from file.");
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            System.out.println("\u274C Failed to load transactions: " + e.getMessage());
        }
    }

    private static void saveTransactions() {
        String filePath = readNonEmptyInput("Enter file path to save (e.g., data.csv): ");

        if (!filePath.toLowerCase().endsWith(".csv")) {
            System.out.println("\u274C File must have .csv extension.");
            return;
        }

        File file = new File(filePath);
        if (file.exists()) {
            System.out.print("\u26A0 File already exists. Overwrite? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                System.out.println("\u274C Save cancelled.");
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Transaction t : transactions) {
                writer.write(String.format("%s,%.2f,%s,%b%n",
                        t.getDate(), t.getAmount(), t.getCategory(), t.isIncome()));
            }
            System.out.println("\u2705 Transactions saved to " + filePath);
        } catch (IOException e) {
            System.out.println("\u274C Error saving file: " + e.getMessage());
        }
    }

    private static void exitApplication() {
        System.out.println("\nThank you for using Expense Tracker. Goodbye!");
        System.exit(0);
    }

    private static LocalDate readValidDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("\u274C Invalid date format. Use yyyy-MM-dd.");
            }
        }
    }

    private static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount > 0) return amount;
                System.out.println("\u274C Amount must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("\u274C Enter a valid number.");
            }
        }
    }

    private static String readValidIncomeCategory() {
        while (true){
            System.out.print("Enter category (" + String.join("/", INCOME_CATEGORIES) + "): ");
            String input = scanner.nextLine().trim();
            for (String cat : INCOME_CATEGORIES) {
                if (cat.equalsIgnoreCase(input)) return cat;
            }
            System.out.println("\u274C Invalid category. Please enter valid category.");
        }
    }

    private static String readValidExpensesCategory() {
        while(true){
            System.out.print("Enter category (" + String.join("/", EXPENSE_CATEGORIES) + "): ");
            String input = scanner.nextLine().trim();
            for (String cat : EXPENSE_CATEGORIES) {
                if (cat.equalsIgnoreCase(input)) return cat;
            }
            System.out.println("\u274C Invalid category. Please enter valid category.");
        }
    }

    private static String readNonEmptyInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isBlank()) return input;
            System.out.println("\u274C Input cannot be empty.");
        }
    }
}
