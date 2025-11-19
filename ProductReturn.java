import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductReturn {

    private String returnID;
    private LocalDate transactionDate;   // date of the return
    private Product product;
    private Client client;
    private Staff staff;
    private String reason;
    private int quantity;
    private LocalDate purchaseDate;      // NEW: required to validate refund window

    // Constructor
    public ProductReturn(String returnID, LocalDate transactionDate, Product product, Client client, Staff staff, String reason, int quantity, LocalDate purchaseDate) {

        this.returnID = returnID;
        this.transactionDate = transactionDate;
        this.product = product;
        this.client = client;
        this.staff = staff;
        this.reason = reason;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
    }

    // Getters
    public Product getProduct() 
    { return product; }
    public Client getClient() 
    { return client; }
    public Staff getStaff() 
    { return staff; }
    public String getReason() 
    { return reason; }
    public int getQuantity() 
    { return quantity; }
    public LocalDate getTransactionDate() 
    { return transactionDate; }
    public LocalDate getPurchaseDate() 
    { return purchaseDate; }

    // 1. VALIDATE RETURN REQUEST

    public void validateReturnRequest() {
    System.out.println("\n=== VALIDATING PRODUCT RETURN REQUEST ===");
    System.out.println("Return ID: " + returnID);
    System.out.println("Product: " + product.getBrand());
    System.out.println("Quantity Returned: " + quantity);
    System.out.println("Reason: " + reason);

    // Quantity must be positive
    if (quantity <= 0) {
        System.out.println("INVALID: Quantity must be greater than 0.");
        return;
    }

    // Reason must be "Defective"
    if (!reason.equalsIgnoreCase("Defective")) {
        System.out.println("INVALID: Refund reason must be 'Defective' or 'Expired' items only.");
        return;
    }

    // Must be within 1 month of purchase
    long daysSincePurchase = ChronoUnit.DAYS.between(purchaseDate, transactionDate);
    if (daysSincePurchase > 30) {
        System.out.println("INVALID: Refund requests are only allowed within 1 month of purchase.");
        return;
    }

    // Expiration check (EXPIRED RETURNS ARE ALLOWED)
    LocalDate today = LocalDate.now();
    long daysUntilExpiry = ChronoUnit.DAYS.between(today, product.getExpirationDate());

    if (daysUntilExpiry < 0) {
        System.out.println("Product is expired. Return accepted, BUT cannot be restocked.");
    } else if (daysUntilExpiry < 30) {
        System.out.println("Product expires in less than 1 month. Return accepted, BUT cannot be restocked.");
    } else {
        System.out.println("Product is valid and may be restocked.");
    }

    System.out.println("RETURN REQUEST ACCEPTED");
}



    // 2. UPDATE STOCK LEVELS AFTER RETURN

    public void updateStockLevels() {
    System.out.println("\n=== PROCESSING RETURN STOCK UPDATE ===");

    LocalDate today = LocalDate.now();
    long daysUntilExpiry = ChronoUnit.DAYS.between(today, product.getExpirationDate());

    if (daysUntilExpiry >= 30) {
        // Safe to restock
        product.setQuantity(product.getQuantity() + quantity);
        System.out.println("Product added back to inventory.");
        System.out.println("Updated Stock Level: " + product.getQuantity());
    }
    else {
        // Cannot restock expired or near-expiry products
        System.out.println("Product/s cannot be restocked.");
        if (daysUntilExpiry < 0) {
            System.out.println("Reason: Product is expired.");
        } else {
            System.out.println("Reason: Product expires in less than 1 month.");
        }
        System.out.println("Returned items must be sent to quality control or disposal.");
    }
    }
}