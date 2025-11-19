
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderID;
    private Client client;
    private Staff staff;
    private String deliveryLocation;
    private String orderDate;
    private String status;

    private List<OrderItem> items;

    public Order(String orderID, Client client, Staff staff, 
                 String deliveryLocation, String orderDate) {
        this.orderID = orderID;
        this.client = client;
        this.staff = staff;
        this.deliveryLocation = deliveryLocation;
        this.orderDate = orderDate;
        this.status = "Pending";
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(OrderItem::getTotal).sum();
    }

    
    public String toString() {
        return "\nOrderID: " + orderID +
               "\nClient: " + client +
               "\nStaff: " + staff.getName() +
               "\nDate: " + orderDate +
               "\nDelivery: " + deliveryLocation +
               "\nStatus: " + status +
               "\nTotal Amount: ₱" + getTotalAmount() +
               "\nItems:\n" + listItems();
    }

    private String listItems() {
        StringBuilder sb = new StringBuilder();
        for (OrderItem item : items) {
            sb.append("  - ").append(item).append("\n");
        }
        return sb.toString();
    }
}
