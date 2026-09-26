package expensemanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseServiceTest {

    private TransactionRepository repository;
    private ExpenseService service;

    @BeforeEach
    void setUp() {
        java.io.File file = new java.io.File("transactions.csv"); // Replace with your actual file name
        if (file.exists()) {
            file.delete();
        }
        // Use an empty repository before each test
        repository = new TransactionRepository();
        service = new ExpenseService(repository);
    }

    @Test
    @DisplayName("Should correctly calculate total expenses and income")
    void testTotalsCalculation() {
        service.addTransaction(50.00, Category.FOOD, "Groceries", true);
        service.addTransaction(20.00, Category.TRANSPORT, "Bus fare", true);
        service.addTransaction(500.00, Category.OTHER, "Salary", false);

        assertEquals(70.00, service.getTotalExpenses(), 0.001);
        assertEquals(500.00, service.getTotalIncome(), 0.001);
    }

    @Test
    @DisplayName("Should compute correct remaining budget")
    void testRemainingBudget() {
        service.setMonthlyBudget(300.00);
        service.addTransaction(100.00, Category.SHOPPING, "Clothes", true);

        assertEquals(200.00, service.getRemainingBudget(), 0.001);
    }

    @Test
    @DisplayName("Should calculate correct category-wise spending")
    void testExpensesByCategory() {
        service.addTransaction(30.00, Category.FOOD, "Lunch", true);
        service.addTransaction(40.00, Category.FOOD, "Dinner", true);
        service.addTransaction(50.00, Category.UTILITIES, "Electricity", true);

        Map<Category, Double> categoryMap = service.getExpensesByCategory();

        assertEquals(70.00, categoryMap.get(Category.FOOD), 0.001);
        assertEquals(50.00, categoryMap.get(Category.UTILITIES), 0.001);
    }

    @Test
    @DisplayName("Should identify the highest spending category")
    void testHighestSpendingCategory() {
        service.addTransaction(10.00, Category.ENTERTAINMENT, "Movie", true);
        service.addTransaction(150.00, Category.SHOPPING, "Shoes", true);

        Optional<Map.Entry<Category, Double>> topCategory = service.getHighestSpendingCategory();

        assertTrue(topCategory.isPresent());
        assertEquals(Category.SHOPPING, topCategory.get().getKey());
        assertEquals(150.00, topCategory.get().getValue(), 0.001);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid transaction amount")
    void testInvalidTransactionAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addTransaction(-10.00, Category.FOOD, "Invalid", true);
        });
    }
}