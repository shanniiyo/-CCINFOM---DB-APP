import java.time.LocalDate;

public class InventoryTransaction {

        // SQL Columns:
            // transactionID VARCHAR PRIMARY KEY
            // transactionDate DATE
            // productID VARCHAR (FK → Product)
            // supplierID VARCHAR (FK → Suppliers)
            // staffID VARCHAR (FK → Staff)
            // quantity INT
            // transactionType VARCHAR
            // status VARCHAR

    private String transactionID;
    private LocalDate transactionDate;
    private Product product;
    private Supplier supplier;
    private Client client;
    private Staff staff;
    private int quantity;
    private String transactionType; // "RECEIVE" or "DELIVERY"
    private String status;          // "SUCCESS" / "FAILED" 

    public InventoryTransaction(String transactionID, LocalDate transactionDate,
                                Product product, Supplier supplier, Client client,
                                Staff staff, int quantity, String transactionType, String status) {
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.product = product;
        this.supplier = supplier;
        this.client = client;
        this.staff = staff;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.status = status;
    }

    // === GETTERS ===
    public String getTransactionID() 
        { return transactionID; }
    public LocalDate getTransactionDate() 
        { return transactionDate; }
    public Product getProduct() 
        { return product; }
    public int getQuantity() 
        { return quantity; }
    public String getTransactionType() 
        { return transactionType; }
    public String getStatus() 
        { return status; }

    // === INVENTORY OPERATIONS ===
    public void receiveProduct() {
        int updatedQty = product.getQuantity() + quantity;
        product.setQuantity(updatedQty);
        status = "SUCCESS";
        transactionType = "RECEIVE";
    }

    public void deliveryProduct() {
        if (product.getQuantity() >= quantity) {
            int updatedQty = product.getQuantity() - quantity;
            product.setQuantity(updatedQty);
            status = "SUCCESS";
        } else {
            status = "FAILED";
        }
        transactionType = "DELIVERY";
    }

    public void updateInventoryProducts() {
        if (transactionType.equals("RECEIVE")) {
            receiveProduct();
        } else if (transactionType.equals("DELIVERY")) {
            deliveryProduct();
        }
    }
}