
import java.time.LocalDate;

public class DeliveryOrder {
    private int orderID;
    private int clientID;
    private int productID;
    private int quantity;
    private double price;
    private LocalDate orderDate;
    private String deliveryLocation;
    private String status;

    public DeliveryOrder(int orderID, int clientID, int productID, int quantity, double price, LocalDate orderDate, String deliveryLocation, String status) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.orderDate = orderDate;
        this.deliveryLocation = deliveryLocation;
        this.status = status;
    }

    // Getters
    public int getOrderID() { return orderID; }
    public int getClientID() { return clientID; }
    public int getProductID() { return productID; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getDeliveryLocation() { return deliveryLocation; }
    public String getStatus() { return status; }
}
