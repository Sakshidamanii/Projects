import java.util.*;
import java.text.SimpleDateFormat;

class Expense {
    String description;
    double amount;
    Date date;
    String category;

    public Expense(String description, double amount, Date date, String category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return "Date: " + sdf.format(date) + ", Description: " + description + ", Category: " + category + ", Amount: $" + amount;
    }
}

public class ExpensesTracker {
    static List<Expense> expenses = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    calculateTotal();
                    break;
                case 4:
                    viewExpensesByCategory();
                    break;
                case 5:
                    System.out.println("Exiting Expenses Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 5);
    }

    static void displayMenu() {
        System.out.println("\n--- Expenses Tracker ---");
        System.out.println("1. Add Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. Calculate Total Expenses");
        System.out.println("4. View Expenses by Category");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    static void addExpense() {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter amount: $");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter category (e.g., Food, Transport, Utilities): ");
        String category = scanner.nextLine();

        Date date = new Date(); // Current date

        expenses.add(new Expense(description, amount, date, category));
        System.out.println("Expense added successfully!");
    }

    static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            System.out.println("\n--- Expense List ---");
            expenses.forEach(System.out::println);
        }
    }

    static void calculateTotal() {
        double total = expenses.stream().mapToDouble(e -> e.amount).sum();
        System.out.println("Total Expenses: $" + total);
    }

    static void viewExpensesByCategory() {
        System.out.print("Enter category to filter: ");
        String category = scanner.nextLine();

        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.category.equalsIgnoreCase(category)) {
                filteredExpenses.add(e);
            }
        }

        if (filteredExpenses.isEmpty()) {
            System.out.println("No expenses found for category: " + category);
        } else {
            System.out.println("\n--- Expenses in Category: " + category + " ---");
            filteredExpenses.forEach(System.out::println);
        }
    }
}
