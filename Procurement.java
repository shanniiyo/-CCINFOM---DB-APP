import java.time.LocalDate;

public class Procurement {
    private int procurementID;
    private Product product;
    private Supplier supplier;
    private int quantity;
    private LocalDate transactionDate;

    public Procurement(int procurementID, Product product, Supplier supplier, int quantity, LocalDate transactionDate) {
        this.procurementID = procurementID;
        this.product = product;
        this.supplier = supplier;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    public int getProcurementID() {
        return procurementID;
    }

    public Product getProduct() {
        return product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }
}
