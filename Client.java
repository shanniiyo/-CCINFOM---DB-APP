import java.util.List;
import java.util.ArrayList;

public class Client {
    private int clientID;
    private String clientName;
    private String address;
    private String contactPerson;
    private String contactInfo;
    
    private List<Order> orders = new ArrayList<>();

    public Client(int clientID, String clientName, String address,
                  String contactPerson, String contactInfo) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.address = address;
        this.contactPerson = contactPerson;
        this.contactInfo = contactInfo;
    }

    public void viewOrders(){
        if (orders.isEmpty()){
            System.out.println("No orders found");
        }
        for (Order order : orders){
            System.out.println(order.toString());
        }
    }

    public int getClientID() {
        return this.clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}