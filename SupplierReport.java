import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
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

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-5s  %-35s  %-6s  %-10s  %-19s  %-35s  %-12s%n",
                    "ID", "Supplier Name", "Txns", "Quantity", "Last Delivery", "Products", "Total Value");
            System.out.println("----  -----------------------------------  ------  ----------  -------------------  -----------------------------------  ------------");

            while (rs.next()) {
                int id = rs.getInt("SupplierID");
                String name = rs.getString("SupplierName");
                int txns = rs.getInt("TotalProcurements");
                int qty = rs.getInt("TotalQuantity");
                Timestamp lastDeliveryTs = rs.getTimestamp("LastDelivery");
                String lastDelivery = (lastDeliveryTs == null) ? "N/A" : dtf.format(lastDeliveryTs.toLocalDateTime());
                String products = rs.getString("ProductsSupplied");
                double totalValue = rs.getDouble("TotalValue");

                if (products.length() > 34) products = products.substring(0, 31) + "...";

                System.out.printf("%-5d  %-35s  %-6d  %-10d  %-19s  %-35s  ₱%10.2f%n",
                        id, name, txns, qty, lastDelivery, products, totalValue);
            }

        } catch (SQLException e) {
            System.out.println("Error generating supplier report:");
            e.printStackTrace();
        }
    }
}
