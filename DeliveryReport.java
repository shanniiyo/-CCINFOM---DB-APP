import java.util.List;

public class DeliveryReport {

    public static void generateReport() {
        System.out.println("\n====== DELIVERY TRANSACTION REPORT ======\n");

        List<DeliveryTransaction> deliveries = DeliveryTransactionDAO.getAllDeliveries();

        if (deliveries.isEmpty()) {
            System.out.println("No delivery transactions found.");
            return;
        }

        for (DeliveryTransaction dt : deliveries) {
            System.out.println("Delivery ID: " + dt.getDeliveryID());
            System.out.println("Date: " + dt.getDeliveryDate());
            System.out.println("Product: " + dt.getProduct().getProductName());
            System.out.println("Client: " + dt.getClient().getName());
            System.out.println("Staff: " + dt.getStaff().getName());
            System.out.println("Quantity: " + dt.getQuantity());
            System.out.println("Status: " + dt.getStatus());
            System.out.println("-----------------------------------------");
        }

        System.out.println("=========== END OF REPORT ===========");
    }
}
