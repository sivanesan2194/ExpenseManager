package expensemanager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TransactionRepository repo = new TransactionRepository();
        ExpenseService service = new ExpenseService(repo);
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.println("\n==============================================");
            System.out.println("         EXPENSE & BUDGET MANAGER            ");
            System.out.println("==============================================");
            System.out.println(" 1. View All Transactions (Table View)");
            System.out.println(" 2. Add Expense");
            System.out.println(" 3. Add Income");
            System.out.println(" 4. View Financial Summary & Budget Bar");
            System.out.println(" 5. View Spending by Category");
            System.out.println(" 6. View Advanced Analytics");
            System.out.println(" 7. Filter Transactions by Date Range");
            System.out.println(" 8. Set Monthly Budget");
            System.out.println(" 0. Exit");
            System.out.println("==============================================");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- All Transactions ---");
                    ReportGenerator.printTransactionTable(service.getAllTransactions());
                }
                case 2, 3 -> {
                    boolean isExpense = (choice == 2);
                    System.out.print("Enter amount: $");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.println("Select Category:");
                    Category[] categories = Category.values();
                    for (int i = 0; i < categories.length; i++) {
                        System.out.printf("  %d. %s\n", i + 1, categories[i]);
                    }
                    System.out.print("Category choice: ");
                    int catChoice = scanner.nextInt() - 1;
                    scanner.nextLine();

                    Category category = (catChoice >= 0 && catChoice < categories.length)
                            ? categories[catChoice] : Category.OTHER;

                    System.out.print("Enter description: ");
                    String desc = scanner.nextLine();

                    service.addTransaction(amount, category, desc, isExpense);
                    System.out.println("✔ Transaction added successfully!");
                }
                case 4 -> {
                    double totalIncome = service.getTotalIncome();
                    double totalExpense = service.getTotalExpenses();
                    double netBalance = totalIncome - totalExpense;
                    double remainingBudget = service.getRemainingBudget();

                    System.out.println("\n--- Financial Summary ---");
                    System.out.printf("Total Income:     $%.2f\n", totalIncome);
                    System.out.printf("Total Expenses:   $%.2f\n", totalExpense);
                    System.out.printf("Net Balance:      $%.2f\n", netBalance);
                    System.out.println("----------------------------------------------");
                    ReportGenerator.printProgressBar(totalExpense, service.getMonthlyBudget());
                    System.out.printf("Remaining Budget: $%.2f\n", remainingBudget);

                    if (remainingBudget < 0) {
                        System.out.println("⚠️  WARNING: You have exceeded your monthly budget!");
                    }
                }
                case 5 -> {
                    System.out.println("\n--- Spending by Category ---");
                    Map<Category, Double> byCat = service.getExpensesByCategory();
                    if (byCat.isEmpty()) {
                        System.out.println("No expenses recorded yet.");
                    } else {
                        byCat.forEach((cat, amt) -> System.out.printf("  %-15s: $%.2f\n", cat, amt));
                    }
                }
                case 6 -> {
                    System.out.println("\n--- Advanced Analytics ---");
                    System.out.printf("Average Expense Amount: $%.2f\n", service.getAverageExpense());

                    Optional<Map.Entry<Category, Double>> topCat = service.getHighestSpendingCategory();
                    if (topCat.isPresent()) {
                        System.out.printf("Top Spending Category:  %s ($%.2f)\n", 
                                topCat.get().getKey(), topCat.get().getValue());
                    } else {
                        System.out.println("Top Spending Category:  None");
                    }

                    Optional<Transaction> maxExp = service.getLargestExpense();
                    if (maxExp.isPresent()) {
                        System.out.println("Largest Single Expense: " + maxExp.get());
                    } else {
                        System.out.println("Largest Single Expense: None");
                    }
                }
                case 7 -> {
                    try {
                        System.out.print("Enter Start Date (YYYY-MM-DD): ");
                        LocalDate startDate = LocalDate.parse(scanner.nextLine().trim());
                        System.out.print("Enter End Date (YYYY-MM-DD): ");
                        LocalDate endDate = LocalDate.parse(scanner.nextLine().trim());

                        System.out.printf("\n--- Transactions (%s to %s) ---\n", startDate, endDate);
                        List<Transaction> filtered = service.getTransactionsInDateRange(startDate, endDate);
                        ReportGenerator.printTransactionTable(filtered);
                    } catch (DateTimeParseException e) {
                        System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
                    }
                }
                case 8 -> {
                    System.out.print("Enter new monthly budget limit: $");
                    double b = scanner.nextDouble();
                    service.setMonthlyBudget(b);
                    System.out.println("✔ Budget updated!");
                }
                case 0 -> {
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
        // scanner.close();
    }
}