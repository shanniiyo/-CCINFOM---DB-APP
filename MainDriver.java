
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList; // Required for getAllProducts

public class MainDriver {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Test DB Connection on startup
        try (Connection conn = DatabaseConnector.getConnection()) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.err.println("Database connection failed. Please check your configuration.");
            e.printStackTrace();
            return; // Exit if DB connection fails
        }

        boolean running = true;
        while (running) {
            printMenu();

            System.out.print("Enter choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAllProducts();
                    break;
                case 2:
                    addNewProduct();
                    break;
                case 3:
                    handleProcurement();
                    break;
                case 4:
                    handleDelivery();
                    break;
                case 5:
                    handleProductReturn();
                    break;
                case 6:
                    // This assumes a getFullReturnReport method exists in ProductReturnDAO
                    new ProductReturnDAO().getFullReturnReport();
                    break;
                case 0:
                    System.out.println("Exiting application...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n============================");
        System.out.println("  INVENTORY MANAGEMENT MENU ");
        System.out.println("============================");
        System.out.println("[1] View All Products");
        System.out.println("[2] Add New Product");
        System.out.println("[3] Procurement (Receive Items)");
        System.out.println("[4] Delivery Transaction");
        System.out.println("[5] Product Return");
        System.out.println("[6] Generate Return Report");
        System.out.println("[0] Exit");
        System.out.println("============================");
    }

    private static int getIntInput() {
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Please enter a whole number: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine(); // Consume newline
        return value;
    }

    private static double getDoubleInput() {
        while (!sc.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a number: ");
            sc.next();
        }
        double value = sc.nextDouble();
        sc.nextLine(); // Consume newline
        return value;
    }

    private static String getStringInput() {
        return sc.nextLine();
    }

    private static void viewAllProducts() {
        System.out.println("\n=== ALL PRODUCTS IN INVENTORY ===");
        List<Product> products = InventoryDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product product : products) {
            product.viewStockLevels();
            System.out.println("------------------------------");
        }
    }

    private static void addNewProduct() {
        System.out.println("\n=== ADD NEW PRODUCT ===");
        System.out.print("Enter Product ID: ");
        int id = getIntInput();
        System.out.print("Enter Brand Name: ");
        String brand = getStringInput();
        System.out.print("Enter Initial Quantity: ");
        int qty = getIntInput();
        System.out.print("Enter Price: ");
        double price = getDoubleInput();
        System.out.print("Enter Expiration Date (YYYY-MM-DD): ");
        LocalDate ExpirationDate = LocalDate.parse(getStringInput());

        Product newProduct = new Product(id, brand, qty, price, LocalDate.now(), ExpirationDate);
        InventoryDAO.addProduct(newProduct);
    }

    private static void handleProcurement() {
        System.out.println("\n=== PROCUREMENT TRANSACTION ===");
        System.out.print("Enter Product ID: ");
        int productID = getIntInput();
        Product product = InventoryDAO.getProductByID(productID);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter Supplier ID: ");
        int supplierID = getIntInput();
        Supplier supplier = SupplierDAO.getSupplierByID(supplierID); // Assumes SupplierDAO exists
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.print("Enter Staff ID: ");
        int staffID = getIntInput();
        Staff staff = StaffDAO.getStaffByID(staffID);
        if (staff == null) {
            System.out.println("Staff not found.");
            return;
        }

        System.out.print("Enter quantity received: ");
        int qty = getIntInput();

        Procurement procurement = new Procurement(
            0, // ProcurementID is auto-incremented by the DB
            product,
            supplier,
            qty,
            LocalDate.now()
        );

        ProcurementDAO.addProcurement(procurement, staff, qty);
    }

    private static void handleDelivery() {
        System.out.println("\n=== DELIVERY TRANSACTION ===");
        // Implementation for delivery, fetching Product, Client, Staff from DB
        System.out.println("Delivery handling is not fully implemented yet.");
    }

    private static void handleProductReturn() {
        System.out.println("\n=== PRODUCT RETURN ===");
        System.out.print("Enter Product ID: ");
        int productID = getIntInput();
        Product product = InventoryDAO.getProductByID(productID);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter Client ID: ");
        int clientID = getIntInput();
        Client client = ClientDAO.getClientByID(clientID);
        if (client == null) {
            System.out.println("Client not found.");
            return;
        }

        System.out.print("Enter Staff ID: ");
        int staffID = getIntInput();
        Staff staff = StaffDAO.getStaffByID(staffID);
        if (staff == null) {
            System.out.println("Staff not found.");
            return;
        }

        System.out.print("Enter Quantity Returned: ");
        int qty = getIntInput();

        System.out.print("Enter Reason (Defective/Expired): ");
        String reason = getStringInput();

        ProductReturn pr = new ProductReturn(
            null,
            LocalDate.now(),
            product,
            client,
            staff,
            reason,
            qty,
            LocalDate.now().minusDays(10)
        );

        pr.validateReturnRequest();
        pr.updateStockLevels();

        ProductReturnDAO.insertReturn(pr);
    }
}
