import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SupplierReport {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

     public static void generateReport() {
        String sql = 
            "SELECT " + "s.SupplierID," + "s.Name AS SupplierName," + "COUNT(pr.ProcurementID) AS TotalProcurements, " +
            "COALESCE(SUM(pr.Quantity), 0) AS TotalQuantity, " + "COALESCE(MAX(pr.TransactionDate), NULL) AS LastDelivery, " +
            "COALESCE(GROUP_CONCAT(DISTINCT p.Brand SEPARATOR ', '), '') AS ProductsSupplied, " + "COALESCE(SUM(pr.Quantity * p.Price), 0) AS TotalValue " +
            "FROM Supplier s " + "LEFT JOIN Procurement pr ON s.SupplierID = pr.SupplierID " + "LEFT JOIN Product p ON pr.ProductID = p.ProductID " +
            "GROUP BY s.SupplierID, s.Name " + "ORDER BY TotalQuantity DESC";

        StringBuilder reportContent = new StringBuilder();
        boolean hasRecords = false;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            String header = String.format("%-5s  %-35s  %-6s  %-10s  %-19s  %-35s  %-12s%n",
                    "ID", "Supplier Name", "Txns", "Quantity", "Last Delivery", "Products", "Total Value");
            String separator = "----  -----------------------------------  ------  ----------  -------------------  -----------------------------------  ------------\n";

            reportContent.append(header);
            reportContent.append(separator);

            while (rs.next()) {
                hasRecords = true;
                int id = rs.getInt("SupplierID");
                String name = rs.getString("SupplierName");
                int txns = rs.getInt("TotalProcurements");
                int qty = rs.getInt("TotalQuantity");
                Timestamp lastDeliveryTs = rs.getTimestamp("LastDelivery");
                String lastDelivery = (lastDeliveryTs == null) ? "N/A" : dtf.format(lastDeliveryTs.toLocalDateTime());
                String products = rs.getString("ProductsSupplied");
                double totalValue = rs.getDouble("TotalValue");

                if (products.length() > 34) products = products.substring(0, 31) + "...";

                String line = String.format("%-5d  %-35s  %-6d  %-10d  %-19s  %-35s  ₱%10.2f%n",
                        id, name, txns, qty, lastDelivery, products, totalValue);
                reportContent.append(line);
            }

            if (!hasRecords) {
                reportContent.setLength(0); // Clear the header and separator
                reportContent.append("No supplier data found to generate a report.\n");
            }
            
            // Print the report to the console before saving
            System.out.println(reportContent.toString());
            
            if (hasRecords) {
                saveReportToFile(reportContent.toString());
            }

        } catch (SQLException e) {
            System.out.println("Error generating supplier report:");
            e.printStackTrace();
        }
    } 

    private static void saveReportToFile(String reportContent) {
        DateTimeFormatter fileDtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(fileDtf);
        String fileName = "SupplierReport_" + timestamp + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(reportContent);
            System.out.println("\nSuccessfully saved report to " + fileName);
        } catch (IOException e) {
            System.out.println("\nError saving report to file:");
            e.printStackTrace();
        }
    }
}
