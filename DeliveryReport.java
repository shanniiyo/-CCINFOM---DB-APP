import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

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

public static void exportToTXT() {
    List<DeliveryTransaction> deliveries = DeliveryTransactionDAO.getAllDeliveries();

    if (deliveries.isEmpty()) {
        System.out.println("No delivery transactions to export.");
        return;
    }

    String filename = "delivery_report.txt";

    try (FileWriter writer = new FileWriter(filename)) {

        writer.write("====== DELIVERY TRANSACTION REPORT ======\n\n");

        for (DeliveryTransaction dt : deliveries) {
            writer.write("Delivery ID: " + dt.getDeliveryID() + "\n");
            writer.write("Date: " + dt.getDeliveryDate() + "\n");
            writer.write("Product: " + dt.getProduct().getProductName() + "\n");
            writer.write("Client: " + dt.getClient().getName() + "\n");
            writer.write("Staff: " + dt.getStaff().getName() + "\n");
            writer.write("Quantity: " + dt.getQuantity() + "\n");
            writer.write("Status: " + dt.getStatus() + "\n");
            writer.write("-----------------------------------------\n");
        }

        writer.write("\n=========== END OF REPORT ===========\n");

        System.out.println("TXT report successfully exported as '" + filename + "'");

    } catch (IOException e) {
        System.err.println("Error exporting TXT report:");
        e.printStackTrace();
    }
}
