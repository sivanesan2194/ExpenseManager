package expensemanager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
public class TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();
    private final Path filePath = Path.of("transactions.csv");

    public TransactionRepository() {
        loadFromCsv();
    }

    public void add(Transaction transaction) {
        transactions.add(transaction);
        saveToCsv(transaction);
    }

    public List<Transaction> getAll(){
        return Collections.unmodifiableList(transactions);
    }

    private void loadFromCsv() {
        if (!Files.exists(filePath)) {
            return; // No saved file yet
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                Transaction tx = Transaction.fromCsv(line);
                if (tx != null) {
                    transactions.add(tx);
                }
            }
            System.out.println("Loaded " + transactions.size() + " saved transactions from storage.");
        } 
        catch(IOException e) {
            System.err.println("Error reading transactions from CSV: " + e.getMessage());
        }
    }

    private void saveToCsv(Transaction transaction) {
        try {
            String line = transaction.toCsv() + System.lineSeparator();
            Files.writeString(filePath, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error saving transaction to CSV: " + e.getMessage());
        }
    }
}
