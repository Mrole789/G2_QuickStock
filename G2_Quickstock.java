import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class G2_Quickstock {
    // Arrays to store product data
    private static String[] productNames = loadProductNames();
    private static int[] productQuantities = loadProductQuantities();
    private static double[] productPrices = loadProductPrices();
    
    // Current sale data
    private static ArrayList<String> currentSaleProducts = new ArrayList<>();
    private static ArrayList<Integer> currentSaleQuantities = new ArrayList<>();
    private static ArrayList<Double> currentSaleTotals = new ArrayList<>();
    
    // Sales tracking
    private static int customerIdCounter = 1;
    private static int salesIdCounter = 1;
    private static final String SALES_REPORT_FILE = "salesreport.txt";
    
    public static void main(String[] args) {
        createMainInterface();
    }
    
 // Load product names from file
    private static String[] loadProductNames() {
        ArrayList<String> names = new ArrayList<>();
        try {
            File file = new File("products.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    names.add(parts[0]); // Product name is first part
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading products.");
        }
        return names.toArray(new String[0]);
    }

    // Load product quantities from file
    private static int[] loadProductQuantities() {
        ArrayList<Integer> quantities = new ArrayList<>();
        try {
            File file = new File("products.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    quantities.add(Integer.parseInt(parts[1])); // Quantity is second part
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading quantities.");
        }
        
        // Convert ArrayList to int[]
        int[] result = new int[quantities.size()];
        for (int i = 0; i < quantities.size(); i++) {
            result[i] = quantities.get(i);
        }
        return result;
    }

    // Load product prices from file
    private static double[] loadProductPrices() {
        ArrayList<Double> prices = new ArrayList<>();
        try {
            File file = new File("products.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    prices.add(Double.parseDouble(parts[2])); // Price is third part
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading prices. Try entering new product details.");
        }
        
        // Convert ArrayList to double[]
        double[] result = new double[prices.size()];
        for (int i = 0; i < prices.size(); i++) {
            result[i] = prices.get(i);
        }
        return result;
    }
    
    // Main interface with logo and role selection
    private static void createMainInterface() {
        JFrame frame = new JFrame("G2 QuickStock Retail Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        try {
            // Load and display logo
            ImageIcon logoIcon = new ImageIcon("logo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(350, 280, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(logoLabel, BorderLayout.NORTH);
        } catch (Exception e) {
            JLabel logoLabel = new JLabel("QuickStock Logo");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
            mainPanel.add(logoLabel, BorderLayout.NORTH);
        }
        
        // Role selection panel
        JPanel rolePanel = new JPanel(new GridLayout(2, 1, 10, 50));
        rolePanel.setBorder(BorderFactory.createEmptyBorder(80, 160, 80, 160));
        
        JButton managerBtn = new JButton("Manager/Admin");
        JButton salesStaffBtn = new JButton("Sales Staff");
        
        // Style buttons
        managerBtn.setFont(new Font("Arial", Font.BOLD, 16));
        salesStaffBtn.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Add action listeners
        managerBtn.addActionListener(e -> {
            frame.dispose();
            createManagerInterface();
        });
        
        salesStaffBtn.addActionListener(e -> {
            frame.dispose();
            createSalesStaffInterface();
        });
        
        rolePanel.add(managerBtn);
        rolePanel.add(salesStaffBtn);
        
        mainPanel.add(rolePanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    // Sales Staff Interface
    private static void createSalesStaffInterface() {
        JFrame frame = new JFrame("G2 QuickStock - Sales staff");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Back button
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> {
            frame.dispose();
            createMainInterface();
        });
     // Title
        JLabel titleLabel = new JLabel("G2 QuickStock - Sales staff", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 200, 100));
        
        JButton addSaleBtn = new JButton("Add Sale");
        JButton viewProductsBtn = new JButton("View Products");
        
        addSaleBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        viewProductsBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        
        addSaleBtn.addActionListener(e -> openAddSaleInterface());
        viewProductsBtn.addActionListener(e -> displayProducts());
        
        optionsPanel.add(addSaleBtn);
        optionsPanel.add(viewProductsBtn);
        
        mainPanel.add(backBtn, BorderLayout.NORTH);
        mainPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(optionsPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    // Display products with quantities and prices
    private static void displayProducts() {
        JFrame productsFrame = new JFrame("Available Products");
        productsFrame.setSize(500, 400);
        productsFrame.setLocationRelativeTo(null);
        
        String[] columns = {"Product", "Quantity", "Price"};
        Object[][] data = new Object[productNames.length][3];
        
        for (int i = 0; i < productNames.length; i++) {
            data[i][0] = productNames[i];
            data[i][1] = productQuantities[i];
            data[i][2] = String.format("GHS %.2f", productPrices[i]);
        }
        
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        
        productsFrame.add(scrollPane);
        productsFrame.setVisible(true);
    }
    
    // Add Sale Interface
    private static void openAddSaleInterface() {
        // Clear current sale data
        currentSaleProducts.clear();
        currentSaleQuantities.clear();
        currentSaleTotals.clear();
        
        showProductSelectionFrame();
    }
    
    private static void showProductSelectionFrame() {
        JFrame selectionFrame = new JFrame("Add Product to Sale");
        selectionFrame.setSize(400, 200);
        selectionFrame.setLocationRelativeTo(null);
        selectionFrame.setLayout(new GridLayout(3, 2, 10, 10));
        
        JLabel productLabel = new JLabel("Choose Product:");
        JComboBox<String> productComboBox = new JComboBox<>(productNames);
        
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        
        JButton cancelBtn = new JButton("Cancel");
        JButton nextBtn = new JButton("Next");
        
        // Add components to frame
        selectionFrame.add(productLabel);
        selectionFrame.add(productComboBox);
        selectionFrame.add(quantityLabel);
        selectionFrame.add(quantityField);
        selectionFrame.add(cancelBtn);
        selectionFrame.add(nextBtn);
        
        // Cancel button action
        cancelBtn.addActionListener(e -> selectionFrame.dispose());
        
        // Next button action
        nextBtn.addActionListener(e -> {
            try {
                String selectedProduct = (String) productComboBox.getSelectedItem();
                int quantity = Integer.parseInt(quantityField.getText());
                int productIndex = getProductIndex(selectedProduct);
                
                // Check if quantity is available
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(selectionFrame, "Please enter a valid quantity!");
                    return;
                }
                
                if (quantity > productQuantities[productIndex]) {
                    JOptionPane.showMessageDialog(selectionFrame, 
                        "Insufficient stock! Only " + productQuantities[productIndex] + " available.");
                    return;
                }
                
                // Add to current sale
                currentSaleProducts.add(selectedProduct);
                currentSaleQuantities.add(quantity);
                currentSaleTotals.add(quantity * productPrices[productIndex]);
                
                selectionFrame.dispose();
                showAddMoreOptions();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(selectionFrame, "Please enter a valid number for quantity!");
            }
        });
        
        selectionFrame.setVisible(true);
    }
    
    // Show options after adding product
    private static void showAddMoreOptions() {
        JFrame optionsFrame = new JFrame("Product Added");
        optionsFrame.setSize(350, 150);
        optionsFrame.setLocationRelativeTo(null);
        optionsFrame.setLayout(new BorderLayout());
        
        JLabel messageLabel = new JLabel("Added to current sale!", JLabel.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addMoreBtn = new JButton("Add More Products");
        JButton checkoutBtn = new JButton("Proceed to Checkout");
        
        addMoreBtn.addActionListener(e -> {
            optionsFrame.dispose();
            showProductSelectionFrame();
        });
        
        checkoutBtn.addActionListener(e -> {
            optionsFrame.dispose();
            showCheckoutInterface();
        });
        
        buttonPanel.add(addMoreBtn);
        buttonPanel.add(checkoutBtn);
        
        optionsFrame.add(messageLabel, BorderLayout.CENTER);
        optionsFrame.add(buttonPanel, BorderLayout.SOUTH);
        optionsFrame.setVisible(true);
    }
    
    // Checkout Interface
    private static void showCheckoutInterface() {
        JFrame checkoutFrame = new JFrame("Checkout");
        checkoutFrame.setSize(500, 400);
        checkoutFrame.setLocationRelativeTo(null);
        checkoutFrame.setLayout(new BorderLayout());
        
        // Display receipt-like information
        JTextArea receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        double totalAmount = 0;
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== G2 QUICKSTOCK RECEIPT ===\n\n");
        
        for (int i = 0; i < currentSaleProducts.size(); i++) {
            receipt.append(String.format("%-15s x%-3d GHS %-8.2f\n", 
                currentSaleProducts.get(i), 
                currentSaleQuantities.get(i), 
                currentSaleTotals.get(i)));
            totalAmount += currentSaleTotals.get(i);
        }
        
        final double grandTotal = totalAmount;
        
        receipt.append("\nTotal Amount: GHS ").append(String.format("%.2f", totalAmount));
        receiptArea.setText(receipt.toString());
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton homeBtn = new JButton("Home");
        JButton paidBtn = new JButton("Paid");
        
        homeBtn.addActionListener(e -> {
            checkoutFrame.dispose();
            createSalesStaffInterface();
        });
        
        paidBtn.addActionListener(e -> {
            try {
                saveSaleRecord(grandTotal);
                updateProductQuantities();
                JOptionPane.showMessageDialog(checkoutFrame, "Sale record saved!");
                checkoutFrame.dispose();
                createSalesStaffInterface();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(checkoutFrame, "Error saving sale record!");
            }
        });
        
        buttonPanel.add(homeBtn);
        buttonPanel.add(paidBtn);
        
        checkoutFrame.add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        checkoutFrame.add(buttonPanel, BorderLayout.SOUTH);
        checkoutFrame.setVisible(true);
    }
    
    // Save sale record to file
    private static void saveSaleRecord(double totalAmount) throws IOException {
        FileWriter writer = new FileWriter(SALES_REPORT_FILE, true);
        
        for (int i = 0; i < currentSaleProducts.size(); i++) {
            writer.write("CustomerID: " + customerIdCounter + ", ");
            writer.write("SalesID: " + salesIdCounter + ", ");
            writer.write("Product: " + currentSaleProducts.get(i) + ", ");
            writer.write("Quantity: " + currentSaleQuantities.get(i) + ", ");
            writer.write("Total: " + currentSaleTotals.get(i) + "\n");
        }
        
        writer.close();
        customerIdCounter++;
        salesIdCounter++;
    }
    
    // Update product quantities after sale
    private static void updateProductQuantities() {
        for (int i = 0; i < currentSaleProducts.size(); i++) {
            int productIndex = getProductIndex(currentSaleProducts.get(i));
            productQuantities[productIndex] -= currentSaleQuantities.get(i);
        }
        
        //Save updated quantities to file
        saveAllProductsToFile();
    }
    
    // Manager Interface
    private static void createManagerInterface() {
        JFrame frame = new JFrame("G2 QuickStock - Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Back button
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> {
            frame.dispose();
            createMainInterface();
        });
        
        // Title
        JLabel titleLabel = new JLabel("G2 QuickStock - Manager", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 30, 30));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 200, 100));
        
        JButton addProductBtn = new JButton("Add New Product");
        JButton updateProductBtn = new JButton("Update Product");
        JButton viewSalesBtn = new JButton("View Sales Report");
        
        addProductBtn.addActionListener(e -> showAddProductInterface());
        updateProductBtn.addActionListener(e -> showUpdateProductInterface());
        viewSalesBtn.addActionListener(e -> showSalesReport());
        
        optionsPanel.add(addProductBtn);
        optionsPanel.add(updateProductBtn);
        optionsPanel.add(viewSalesBtn);
        
        mainPanel.add(backBtn, BorderLayout.NORTH);
        mainPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(optionsPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    // Add New Product Interface
    private static void showAddProductInterface() {
        JFrame addFrame = new JFrame("Add New Product");
        addFrame.setSize(400, 250);
        addFrame.setLocationRelativeTo(null);
        addFrame.setLayout(new GridLayout(4, 2, 10, 10));
        
        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField();
        
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        
        JLabel priceLabel = new JLabel("Price:");
JTextField priceField = new JTextField();
        
        JButton cancelBtn = new JButton("Cancel");
        JButton addBtn = new JButton("Add Product");
        
        addFrame.add(nameLabel);
        addFrame.add(nameField);
        addFrame.add(quantityLabel);
        addFrame.add(quantityField);
        addFrame.add(priceLabel);
        addFrame.add(priceField);
        addFrame.add(cancelBtn);
        addFrame.add(addBtn);
        
        cancelBtn.addActionListener(e -> addFrame.dispose());
        
        addBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());
                
                // Add new product to arrays
                addNewProduct(name, quantity, price);
                JOptionPane.showMessageDialog(addFrame, "Product added successfully!");
                addFrame.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addFrame, "Please enter valid numbers!");
            }
        });
        
        addFrame.setVisible(true);
    }
    
    // Update Product Interface
    private static void showUpdateProductInterface() {
        JFrame updateFrame = new JFrame("Update Product");
        updateFrame.setSize(400, 250);
        updateFrame.setLocationRelativeTo(null);
        updateFrame.setLayout(new GridLayout(4, 2, 10, 10));
        
        JLabel productLabel = new JLabel("Choose Product:");
        JComboBox<String> productComboBox = new JComboBox<>(productNames);
        
        JLabel quantityLabel = new JLabel("New Quantity:");
        JTextField quantityField = new JTextField();
        
        JLabel priceLabel = new JLabel("New Price:");
        JTextField priceField = new JTextField();
        
        JButton cancelBtn = new JButton("Cancel");
        JButton updateBtn = new JButton("Update Product");
        
        updateFrame.add(productLabel);
        updateFrame.add(productComboBox);
        updateFrame.add(quantityLabel);
        updateFrame.add(quantityField);
        updateFrame.add(priceLabel);
        updateFrame.add(priceField);
        updateFrame.add(cancelBtn);
        updateFrame.add(updateBtn);
        
        cancelBtn.addActionListener(e -> updateFrame.dispose());
        
        updateBtn.addActionListener(e -> {
            try {
                String selectedProduct = (String) productComboBox.getSelectedItem();
                int productIndex = getProductIndex(selectedProduct);
                
                if (!quantityField.getText().isEmpty()) {
                    int newQuantity = Integer.parseInt(quantityField.getText());
                    if (newQuantity >= 0) {
                        productQuantities[productIndex] = newQuantity;
                    }
                }
                
                if (!priceField.getText().isEmpty()) {
                    double newPrice = Double.parseDouble(priceField.getText());
                    if (newPrice >= 0) {
                        productPrices[productIndex] = newPrice;
                    }
                }
                
                //Save Updated Products To File
                saveAllProductsToFile();
                
                JOptionPane.showMessageDialog(updateFrame, "Product updated successfully!");
                updateFrame.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updateFrame, "Please enter valid numbers!");
            }
        });
        
        updateFrame.setVisible(true);
    }
    
    // Display Sales Report
    private static void showSalesReport() {
        JFrame reportFrame = new JFrame("Sales Report");
        reportFrame.setSize(600, 400);
        reportFrame.setLocationRelativeTo(null);
        
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        try {
            File file = new File(SALES_REPORT_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                StringBuilder report = new StringBuilder("=== SALES REPORT ===\n\n");
                
                while ((line = reader.readLine()) != null) {
                    report.append(line).append("\n");
                }
                reader.close();
                reportArea.setText(report.toString());
            } else {
                reportArea.setText("No sales records found.");
            }
        } catch (IOException e) {
            reportArea.setText("Error reading sales report file.");
        }
        
        reportFrame.add(new JScrollPane(reportArea));
        reportFrame.setVisible(true);
    }
    
    // Helper method to get product index
    private static int getProductIndex(String productName) {
        for (int i = 0; i < productNames.length; i++) {
            if (productNames[i].equals(productName)) {
                return i;
            }
        }
        return -1;
    }
    
    // Add new product to arrays AND save to file
    private static void addNewProduct(String name, int quantity, double price) {
        // Create new arrays with increased size
        String[] newNames = new String[productNames.length + 1];
        int[] newQuantities = new int[productQuantities.length + 1];
        double[] newPrices = new double[productPrices.length + 1];
        
        // Copy old data
        System.arraycopy(productNames, 0, newNames, 0, productNames.length);
        System.arraycopy(productQuantities, 0, newQuantities, 0, productQuantities.length);
        System.arraycopy(productPrices, 0, newPrices, 0, productPrices.length);
        
        // Add new product
        newNames[productNames.length] = name;
        newQuantities[productQuantities.length] = quantity;
        newPrices[productPrices.length] = price;
        
        // Update arrays
        productNames = newNames;
        productQuantities = newQuantities;
        productPrices = newPrices;
        
        // SAVE TO FILE
        saveAllProductsToFile();
    }
    
    // Save all products from arrays to file
    private static void saveAllProductsToFile() {
        try {
            FileWriter writer = new FileWriter("products.txt");
            
            for (int i = 0; i < productNames.length; i++) {
                writer.write(productNames[i] + "," + 
                            productQuantities[i] + "," + 
                            productPrices[i] + "\n");
            }
            
            writer.close();
            System.out.println("All products saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving products to file: " + e.getMessage());
        }
    }
}
