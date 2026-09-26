package expensemanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final String id;
    private final LocalDate date;
    private final double amount;
    private final Category category;
    private final String description;
    private final boolean isExpense;

    public Transaction(String id,LocalDate date,double amount,Category category,String description,boolean isExpense){
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be Greater than Zero");
        }
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.isExpense = isExpense;
    }
    public String getId(){ return id;}
    public LocalDate getDate(){ return date;}
    public double getAmount(){ return amount;}
    public Category getCategory(){ return category;}
    public String getDescription(){ return description;}
    public boolean isExpense(){ return isExpense;}

    public String toCsv() {
        return String.join(",", id, date.toString(), String.valueOf(amount), category.name(), description, String.valueOf(isExpense));
    }

    // Parse CSV line into a Transaction object
    public static Transaction fromCsv(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) return null;

        String id = parts[0];
        LocalDate date = LocalDate.parse(parts[1]);
        double amount = Double.parseDouble(parts[2]);
        Category category = Category.valueOf(parts[3]);
        String description = parts[4];
        boolean isExpense = Boolean.parseBoolean(parts[5]);

        return new Transaction(id, date, amount, category, description, isExpense);
    }

    public String toString(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String type = isExpense ? "EXPENSE" : "INCOME ";
        return String.format("[%s] %s | %-13s | $%-8.2f | %s", id, date.format(fmt), category, amount, type + " - " + description);
    }
}
