package expense.tracker;

import java.time.LocalDate;

class Transaction {
    private final LocalDate date;
    private final double amount;
    private final String category;
    private final boolean isIncome;

    public Transaction(LocalDate date, double amount, String category, boolean isIncome) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.isIncome = isIncome;
    }

    public LocalDate getDate() { return date; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public boolean isIncome() { return isIncome; }

}