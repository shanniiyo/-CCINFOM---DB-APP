import java.time.LocalDate;

public class DeliveryTransaction {

    private String deliveryID;
    private LocalDate deliveryDate;
    private Product product;
    private Client client;
    private Staff staff;
    private int quantity;
    private String status;   // PENDING, SUCCESS, FAILED

    // Constructor
    public DeliveryTransaction(String deliveryID, LocalDate deliveryDate,
                               Product product, Client client, Staff staff,
                               int quantity) {

        this.deliveryID = deliveryID;
        this.deliveryDate = deliveryDate;
        this.product = product;
        this.client = client;
        this.staff = staff;
        this.quantity = quantity;
        this.status = "PENDING";
    }

    // MAIN FUNCTION – PROCESS DELIVERY
    public void processDelivery() {
        System.out.println("\n=== PROCESSING DELIVERY ===");
        System.out.println("Delivery ID: " + deliveryID);
        System.out.println("Product: " + product.getProductName());
        System.out.println("Client: " + client.getName());
        System.out.println("Quantity Requested: " + quantity);

        if (product.getQuantity() < quantity) {
            status = "FAILED";
            System.out.println("STATUS: FAILED — Not enough stock!");
            return;
        }

        product.deductStock(quantity);
        status = "SUCCESS";

        System.out.println("STATUS: SUCCESS — Delivered " + quantity + " units.");
        System.out.println("Remaining Stock: " + product.getQuantity());
    }

    // VIEW DELIVERY DETAILS
    public void viewDeliveryDetails() {
        System.out.println("\n=== DELIVERY TRANSACTION DETAILS ===");
        System.out.println("Delivery ID: " + deliveryID);
        System.out.println("Date: " + deliveryDate);
        System.out.println("Product: " + product.getProductName());
        System.out.println("Client: " + client.getName());
        System.out.println("Staff: " + staff.getName());
        System.out.println("Quantity: " + quantity);
        System.out.println("Status: " + status);
        System.out.println("====================================");
    }

    public String getStatus() { return status; }
    public String getDeliveryID() { return deliveryID; }
}
