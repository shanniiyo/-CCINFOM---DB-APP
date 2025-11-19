import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InventoryReport {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void generateReport() {
        String sql = "SELECT ProductID, Brand, Quantity, Price, ExpirationDate, LowStockLimit FROM Product ORDER BY Brand";

        StringBuilder reportContent = new StringBuilder();
        StringBuilder lowStockAlerts = new StringBuilder("\n--- LOW STOCK ALERTS ---\n");
        StringBuilder nearExpiryAlerts = new StringBuilder("\n--- NEAR EXPIRY ALERTS (Next 3 Months) ---\n");
        boolean hasRecords = false;
        boolean hasLowStock = false;
        boolean hasNearExpiry = false;

        LocalDate today = LocalDate.now();
        LocalDate expiryWarningDate = today.plusMonths(3);

        reportContent.append("========== INVENTORY REPORT ==========\n");
        reportContent.append("Report Date: ").append(today.format(dtf)).append("\n\n");

        String header = String.format("%-5s  %-45s  %-10s  %-12s  %-15s  %s%n",
                "ID", "Brand", "Quantity", "Price", "Expires", "Status");
        String separator = String.format("%-5s  %-45s  %-10s  %-12s  %-15s  %s%n",
                "----", "---------------------------------------------", "----------", "------------", "---------------", "------");

        reportContent.append(header);
        reportContent.append(separator);

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                hasRecords = true;
                int id = rs.getInt("ProductID");
                String brand = rs.getString("Brand");
                int quantity = rs.getInt("Quantity");
                double price = rs.getDouble("Price");
                LocalDate expirationDate = rs.getDate("ExpirationDate").toLocalDate();
                int lowStockLimit = rs.getInt("LowStockLimit");
                String status = "OK";

                // Check for low stock
                if (quantity <= lowStockLimit) {
                    status = "LOW STOCK";
                    lowStockAlerts.append(String.format("LOW STOCK: %d | Brand: %s | Qty: %d (Limit: %d)%n",
                            id, brand, quantity, lowStockLimit));
                    hasLowStock = true;
                }

                // Check for near expiry
                if (!expirationDate.isAfter(expiryWarningDate)) {
                    nearExpiryAlerts.append(String.format("EXPIRING: %d | Brand: %s | Expires: %s%n",
                            id, brand, expirationDate.format(dtf)));
                    hasNearExpiry = true;
                }

                String line = String.format("%-5d  %-45s  %-10d  ₱%11.2f  %-15s  %s%n",
                        id, brand, quantity, price, expirationDate.format(dtf), status);
                reportContent.append(line);
            }

            if (!hasRecords) {
                reportContent.setLength(0); // Clear header
                reportContent.append("No products found in inventory.\n");
            } else {
                if (hasLowStock) {
                    reportContent.append(lowStockAlerts.toString());
                } else {
                    reportContent.append("\n--- LOW STOCK ALERTS ---\nNo low-stock products.\n");
                }

                if (hasNearExpiry) {
                    reportContent.append(nearExpiryAlerts.toString());
                } else {
                    reportContent.append("\n--- NEAR EXPIRY ALERTS (Next 3 Months) ---\nNo products nearing expiration.\n");
                }
            }

            // Print the full report to the console
            System.out.println(reportContent.toString());

            // Save to file only if there were records
            if (hasRecords) {
                saveReportToFile(reportContent.toString());
            }

        } catch (SQLException e) {
            System.out.println("Error generating inventory report:");
            e.printStackTrace();
        }
    }

    private static void saveReportToFile(String reportContent) {
        DateTimeFormatter fileDtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(fileDtf);
        String dirName = "reports";
        String fileName = dirName + "/InventoryReport_" + timestamp + ".txt";

        try {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(reportContent);
                System.out.println("\nSuccessfully saved report to " + fileName);
            }
        } catch (IOException e) {
            System.out.println("\nError saving report to file:");
            e.printStackTrace();
        }
    }
}
