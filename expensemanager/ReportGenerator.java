package expensemanager;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportGenerator {

    // Print a visual progress bar for budget utilization
    public static void printProgressBar(double spent, double budget) {
        if (budget <= 0) {
            System.out.println("Budget: Not Set");
            return;
        }

        double percentage = Math.min((spent / budget) * 100, 100);
        int barLength = 20;
        int filledLength = (int) (barLength * (percentage / 100));

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filledLength) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");

        System.out.printf("Budget Usage: %s %.1f%% ($%.2f / $%.2f)\n", 
                bar.toString(), (spent / budget) * 100, spent, budget);
    }

    // Print transactions in a clean ASCII table
    public static void printTransactionTable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        String border = "+----------+------------+---------------+------------+-----------+-------------------------+";
        String header = "| ID       | Date       | Category      | Amount     | Type      | Description             |";

        System.out.println(border);
        System.out.println(header);
        System.out.println(border);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Transaction tx : transactions) {
            String typeStr = tx.isExpense() ? "EXPENSE" : "INCOME";
            String desc = tx.getDescription().length() > 23 
                    ? tx.getDescription().substring(0, 20) + "..." 
                    : tx.getDescription();

            System.out.printf("| %-8s | %-10s | %-13s | $%-9.2f | %-9s | %-23s |\n",
                    tx.getId(),
                    tx.getDate().format(fmt),
                    tx.getCategory(),
                    tx.getAmount(),
                    typeStr,
                    desc);
        }
        System.out.println(border);
    }
}