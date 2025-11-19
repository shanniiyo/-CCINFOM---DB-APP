import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductReturn {

    private String returnID;
    private LocalDate transactionDate;
    private Product product;
    private Client client;
    private Staff staff;
    private String reason;
    private int quantity;
    private LocalDate purchaseDate;
    //Added
    private double refundAmount;
    private LocalDate refundDate;

    public ProductReturn(String returnID, LocalDate transactionDate,
                         Product product, Client client, Staff staff,
                         String reason, int quantity, LocalDate purchaseDate) {

        this.returnID = returnID;
        this.transactionDate = transactionDate;
        this.product = product;
        this.client = client;
        this.staff = staff;
        this.reason = reason;
        this.quantity = quantity;
        this.purchaseDate = purchaseDate;
        this.refundAmount = 0.0 //Added
        this.refundDate = transactionDate;//Added
    }

    // VALIDATE RETURN
    public boolean validateReturnRequest() {

        System.out.println("\n=== VALIDATING PRODUCT RETURN ===");
        System.out.println("Product: " + product.getProductName());
        System.out.println("Quantity: " + quantity);
        System.out.println("Reason: " + reason);

        if (quantity <= 0) {
            System.out.println("INVALID: Quantity must be > 0.");
            return false;
        }

        if (!reason.equalsIgnoreCase("Defective") &&
            !reason.equalsIgnoreCase("Expired")) {

            System.out.println("INVALID: Reason must be 'Defective' or 'Expired'.");
            return false;
        }

        long daysSincePurchase =
                ChronoUnit.DAYS.between(purchaseDate, transactionDate);

        if (daysSincePurchase > 30) {
            System.out.println("INVALID: Refund allowed only within 30 days.");
            return false;
        }

        System.out.println("Return request VALIDATED.");
        return true;
    }

    // UPDATE STOCK LEVELS BASED ON EXPIRATION
    public void updateStockLevels() {

        LocalDate today = LocalDate.now();
        long daysUntilExpiry =
                ChronoUnit.DAYS.between(today, product.getExpirationDate());

        System.out.println("\n=== PROCESSING RETURN STOCK UPDATE ===");

        if (daysUntilExpiry >= 30) {
            product.addStock(quantity);
            System.out.println("Returned items restocked. New Stock: " +
                    product.getQuantity());
        } else {
            System.out.println("Returned items NOT restocked due to expiration.");
        }
    }

    public String getReturnID() { return returnID; }
    public String getReason() { return reason; }
    public int getQuantity() { return quantity; }
    public Product getProduct() { return product; }
    public Client getClient() { return client; }

    public double getRefundAmount() {return refundAmount;}//Added
    public LocalDate getRefundDate() {return refunDate;}//Added

    public void setRefundAmount(double refundAmount){ //Added
        this.refundDate = refundDate;
    }

    public void setRefundDate(LocalDate refundDate){ //Added
        this.refundDate = refundDate;
    }
    
}
