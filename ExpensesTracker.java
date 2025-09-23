import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

// Expense class remains the same
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
        return "Date: " + sdf.format(date) + ", Description: " + description + ", Category: " + category + ", Amount: $"
                + amount;
    }

    // Getters for GUI use
    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
}

// Custom JPanel for circular progress indicator
class CircularProgress extends JPanel {
    private double progress; // Percentage (0-100)
    private double totalExpenses;
    private double budget;

    public CircularProgress(double totalExpenses, double budget) {
        this.totalExpenses = totalExpenses;
        this.budget = budget;
        updateProgress();
        setPreferredSize(new Dimension(150, 150));
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public void update(double newExpenses, double newBudget) {
        this.totalExpenses = newExpenses;
        this.budget = newBudget;
        updateProgress();
        repaint();
    }

    private void updateProgress() {
        if (budget > 0) {
            progress = (totalExpenses / budget) * 100;
            if (progress > 100)
                progress = 100;
        } else {
            progress = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        int centerX = size / 2;
        int centerY = size / 2;
        int radius = size / 2 - 10; // Padding

        // Background circle (gray)
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // Progress arc (green if under budget, red if over)
        double angle = (progress / 100) * 360;
        Color progressColor = progress <= 100 ? Color.GREEN : Color.RED;
        g2d.setColor(progressColor);
        Arc2D arc = new Arc2D.Double(centerX - radius, centerY - radius, 2 * radius, 2 * radius,
                90, -angle, Arc2D.PIE); // Start from top, clockwise
        g2d.fill(arc);

        // Center text: percentage
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String percent = String.format("%.1f%%", progress);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = centerX - fm.stringWidth(percent) / 2;
        int textY = centerY + fm.getHeight() / 2;
        g2d.drawString(percent, textX, textY);

        // Draw labels below (in parent or via tooltip, but for simplicity, we'll update
        // in main panel)
    }
}

// Main GUI class
public class ExpensesTrackerGUI extends JFrame {
    private List<Expense> expenses = new ArrayList<>();
    private double budget = 0.0;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JTextField budgetField;
    private JLabel totalLabel;
    private JLabel budgetLabel;
    private CircularProgress progressCircle;
    private JComboBox<String> categoryCombo;
    private static final String DATA_FILE = "expenses.dat";
    private static final String[] CATEGORIES = { "Food", "Transport", "Utilities", "Entertainment", "Other" };

    public ExpensesTrackerGUI() {
        loadData();
        initializeGUI();
        updateDisplay();
    }

    private void initializeGUI() {
        setTitle("Expenses Tracker - Daily Budget Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen

        // Top panel: Budget and Total
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Budget input
        JPanel budgetPanel = new JPanel(new FlowLayout());
        budgetPanel.add(new JLabel("Monthly Budget: $"));
        budgetField = new JTextField(10);
        budgetField.setText(String.valueOf(budget));
        budgetPanel.add(budgetField);
        JButton setBudgetBtn = new JButton("Set Budget");
        setBudgetBtn.addActionListener(e -> setBudget());
        budgetPanel.add(setBudgetBtn);
        topPanel.add(budgetPanel, BorderLayout.NORTH);

        // Total and Progress
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        totalLabel = new JLabel("Total Expenses: $0.00");
        budgetLabel = new JLabel("Budget: $0.00");
        progressCircle = new CircularProgress(0, budget);
        statsPanel.add(totalLabel);
        statsPanel.add(budgetLabel);
        statsPanel.add(progressCircle);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Center: Expense Table
        String[] columns = { "Date", "Description", "Category", "Amount" };
        tableModel = new DefaultTableModel(columns, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: Buttons and Filter
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Expense");
        addBtn.addActionListener(e -> showAddDialog());
        JButton editBtn = new JButton("Edit Selected");
        editBtn.addActionListener(e -> editSelected());
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteSelected());
        JButton viewAllBtn = new JButton("View All");
        viewAllBtn.addActionListener(e -> viewAll());
        JButton filterBtn = new JButton("Filter by Category");
        filterBtn.addActionListener(e -> filterByCategory());

        bottomPanel.add(addBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(viewAllBtn);
        bottomPanel.add(filterBtn);

        // Category filter combo
        bottomPanel.add(new JLabel("Category:"));
        categoryCombo = new JComboBox<>(CATEGORIES);
        categoryCombo.addItem("All");
        categoryCombo.setSelectedIndex(CATEGORIES.length); // Select "All"
        categoryCombo.addActionListener(e -> filterByCategory());
        bottomPanel.add(categoryCombo);

        JButton calculateBtn = new JButton("Calculate Total");
        calculateBtn.addActionListener(e -> calculateTotal());
        bottomPanel.add(calculateBtn);

        JButton saveBtn = new JButton("Save & Exit");
        saveBtn.addActionListener(e -> {
            saveData();
            System.exit(0);
        });
        bottomPanel.add(saveBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Mouse listener for table row selection
        expenseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelected();
                }
            }
        });

        // Initial load
        viewAll();
    }

    private void setBudget() {
        try {
            double newBudget = Double.parseDouble(budgetField.getText().trim());
            if (newBudget < 0) {
                JOptionPane.showMessageDialog(this, "Budget must be non-negative!", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            budget = newBudget;
            updateDisplay();
            JOptionPane.showMessageDialog(this, "Budget set to $" + budget);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for budget!", "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddDialog() {
        JTextField descField = new JTextField(15);
        JTextField amountField = new JTextField(10);
        JComboBox<String> catCombo = new JComboBox<>(CATEGORIES);

        Object[] fields = {
                "Description:", descField,
                "Amount ($):", amountField,
                "Category:", catCombo
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String desc = descField.getText().trim();
            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Description cannot be empty!", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive!", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String category = (String) catCombo.getSelectedItem();
                Date date = new Date();

                expenses.add(new Expense(desc, amount, date, category));
                updateDisplay();
                JOptionPane.showMessageDialog(this, "Expense added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for amount!", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelected() {
        int row = expenseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to edit!", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Expense exp = expenses.get(row);
        JTextField descField = new JTextField(exp.getDescription(), 15);
        JTextField amountField = new JTextField(String.valueOf(exp.getAmount()), 10);
        JComboBox<String> catCombo = new JComboBox<>(CATEGORIES);
        catCombo.setSelectedItem(exp.getCategory());

        Object[] fields = {
                "Description:", descField,
                "Amount ($):", amountField,
                "Category:", catCombo
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Edit Expense", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String desc = descField.getText().trim();
            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Description cannot be empty!", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive!", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String category = (String) catCombo.getSelectedItem();

                // Update expense
                exp.description = desc;
                exp.amount = amount;
                exp.category = category;
                // Note: Date remains the same for simplicity; could add date picker if needed

                updateDisplay();
                JOptionPane.showMessageDialog(this, "Expense updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for amount!", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int row = expenseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete!", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this expense?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            expenses.remove(row);
            updateDisplay();
            JOptionPane.showMessageDialog(this, "Expense deleted successfully!");
        }
    }

    private void viewAll() {
        populateTable(expenses);
        categoryCombo.setSelectedIndex(CATEGORIES.length); // "All"
    }

    private void filterByCategory() {
        String selectedCat = (String) categoryCombo.getSelectedItem();
        if (selectedCat.equals("All")) {
            viewAll();
            return;
        }

        List<Expense> filtered = expenses.stream()
                .filter(e -> e.category.equalsIgnoreCase(selectedCat))
                .collect(java.util.stream.Collectors.toList());
        populateTable(filtered);
    }

    private void calculateTotal() {
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        JOptionPane.showMessageDialog(this, "Total Expenses: $" + String.format("%.2f", total), "Total Calculation",
                JOptionPane.INFORMATION_MESSAGE);
        updateDisplay(); // Ensure labels are up-to-date
    }

    private void populateTable(List<Expense> expList) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (Expense e : expList) {
            tableModel.addRow(new Object[] {
                    sdf.format(e.getDate()),
                    e.getDescription(),
                    e.getCategory(),
                    String.format("$%.2f", e.getAmount())
            });
        }
    }

    private void updateDisplay() {
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        totalLabel.setText("Total Expenses: $" + String.format("%.2f", totalExpenses));
        budgetLabel.setText("Budget: $" + String.format("%.2f", budget));
        progressCircle.update(totalExpenses, budget);
        populateTable(expenses); // Refresh table if needed
    }

    // Data persistence using serialization
    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            expenses = (List<Expense>) ois.readObject();
            budget = ois.readDouble();
            budgetField.setText(String.valueOf(budget));
        } catch (FileNotFoundException e) {
            // No file yet, start empty
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            expenses = new ArrayList<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(expenses);
            oos.writeDouble(budget);
            JOptionPane.showMessageDialog(this, "Data saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ExpensesTrackerGUI().setVisible(true);
        });
    }
}
