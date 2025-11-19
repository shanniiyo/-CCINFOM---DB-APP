import java.time.LocalDate;

public class Product {

        // SQL COLUMNS 
            // PRIMARY KEY (productID)
            // brand VARCHAR(100)
            // quantity INT
            // price DOUBLE
            // dateAdded DATE
            // expiration DATE

    private int productID;
    private String brand;
    private int quantity;
    private double price;
    private LocalDate dateAdded;
    private LocalDate ExpirationDate;

    public Product(int productID, String brand, int quantity, double price, LocalDate dateAdded, LocalDate ExpirationDate) {
        this.productID = productID;
        this.brand = brand;
        this.quantity = quantity;
        this.price = price;
        this.dateAdded = dateAdded;
        this.ExpirationDate = ExpirationDate;
    }

    // === GETTERS AND SETTERS ===
    public int getProductID() 
        { return productID; }
    public String getBrand() 
        { return brand; }
    public int getQuantity()
        { return quantity; }
    public double getPrice()
        { return price; }
    public LocalDate getDateAdded()
        { return dateAdded; }
    public LocalDate getExpirationDate() 
        { return ExpirationDate; }

    public void setQuantity(int quantity) 
        { this.quantity = quantity; }
    public void setPrice(double price)
        { this.price = price; }

    // === BUSINESS METHODS ===

    public boolean isLowStock() {
        return quantity < 10;   // or any threshold
    }
   public void viewStockLevels() {
        System.out.println("=== Stock Information ===");
        System.out.println("Product ID: " + productID);
        System.out.println("Brand: " + brand);
        System.out.println("Stock: " + quantity);
        System.out.println("Expiration: " + ExpirationDate);

        if (isLowStock()) {
            System.out.println("LOW STOCK ALERT");
        }
        }
    
        public void viewTransaction(InventoryTransaction transaction) {
        System.out.println("=== Product Transaction Details ===");
        System.out.println("Transaction ID: " + transaction.getTransactionID());
        System.out.println("Product: " + this.brand);
        System.out.println("Quantity: " + transaction.getQuantity());
        System.out.println("Type: " + transaction.getTransactionType());
        System.out.println("Status: " + transaction.getStatus());
        System.out.println("Date: " + transaction.getTransactionDate());
    }
}