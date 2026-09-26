package expensemanager;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExpenseService {
    private final TransactionRepository repository;
    private double monthlyBudget = 0.0;

    public ExpenseService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void setMonthlyBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative.");
        }
        this.monthlyBudget = budget;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void addTransaction(double amount, Category category, String description, boolean isExpense) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        Transaction tx = new Transaction(id, LocalDate.now(), amount, category, description, isExpense);
        repository.add(tx);
    }

    public List<Transaction> getAllTransactions() {
        return repository.getAll();
    }

    public double getTotalExpenses() {
        return repository.getAll().stream()
                .filter(Transaction::isExpense)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalIncome() {
        return repository.getAll().stream()
                .filter(tx -> !tx.isExpense())
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Map<Category, Double> getExpensesByCategory() {
        return repository.getAll().stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public double getRemainingBudget() {
        return monthlyBudget - getTotalExpenses();
    }

    // ==========================================
    // DAY 4: NEW ANALYTICS & FILTERING METHODS
    // ==========================================

    // 1. Calculate Average Expense Amount
    public double getAverageExpense() {
        return repository.getAll().stream()
                .filter(Transaction::isExpense)
                .mapToDouble(Transaction::getAmount)
                .average()
                .orElse(0.0);
    }

    // 2. Find Highest Spending Category
    public Optional<Map.Entry<Category, Double>> getHighestSpendingCategory() {
        return getExpensesByCategory().entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    // 3. Find Largest Single Expense
    public Optional<Transaction> getLargestExpense() {
        return repository.getAll().stream()
                .filter(Transaction::isExpense)
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }

    // 4. Filter Transactions by Date Range
    public List<Transaction> getTransactionsInDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.getAll().stream()
                .filter(tx -> !tx.getDate().isBefore(startDate) && !tx.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
}