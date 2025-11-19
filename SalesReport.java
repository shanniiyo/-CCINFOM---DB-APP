import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SalesReport {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void generateReport() {

        String sql =
                "SELECT s.SalesID, st.Name AS StaffName, p.Brand AS ProductName, c.Name AS ClientName, " +
                "   s.Quantity, s.TotalPrice, SalesDate " +
                "FROM Sales s " +
                "JOIN Staff st ON s.StaffID = st.StaffID " +
                "JOIN Product p ON s.ProductID = p.ProductID " +
                "JOIN ClientMed c ON s.ClientID = c.ClientID " +
                "ORDER BY s.SalesDate ASC";

        StringBuilder report = new StringBuilder();
        boolean hasRecords = false;

        int totalTransactions = 0;
        int totalQuantity = 0;
        double totalRevenue = 0;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            String header = String.format(
                    "%-6s %-20s %-20s %-22s %-8s %-12s %-20s%n",
                    "ID", "Staff", "Product", "Client", "Qty", "Price", "Date"
            );

            String separator = "------ -------------------- -------------------- ---------------------- -------- ------------ --------------------\n";

            report.append("========= SALES REPORT =========\n\n");
            report.append(header);
            report.append(separator);

            while (rs.next()) {
                hasRecords = true;

                int id = rs.getInt("SalesID");
                String staff = rs.getString("StaffName");
                String product = rs.getString("ProductName");
                String client = rs.getString("ClientName");
                int qty = rs.getInt("Quantity");
                double price = rs.getDouble("TotalPrice");

                Timestamp ts = rs.getTimestamp("SalesDate");
                String date = dtf.format(ts.toLocalDateTime());

                totalTransactions++;
                totalQuantity += qty;
                totalRevenue += price;

                String line = String.format(
                        "%-6d %-20s %-20s %-22s %-8d ₱%-11.2f %-20s%n",
                        id, staff, product, client, qty, price, date
                );

                report.append(line);
            }

            if (!hasRecords) {
                System.out.println("No sales record found.");
            }

            // Append totals summary
            report.append("\n------------------------------------------------------------\n");
            report.append(String.format("Total Transactions: %d%n", totalTransactions));
            report.append(String.format("Total Quantity Sold: %d%n", totalQuantity));
            report.append(String.format("Total Revenue: ₱%.2f%n", totalRevenue));

            // Print to console
            System.out.println(report.toString());

            // Save to file
            saveReport(report.toString());

        } catch (SQLException e) {
            System.out.println("Error generating sales report.");
            e.printStackTrace();
        }
    }

    private static void saveReport(String content) {
        DateTimeFormatter fileDtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(fileDtf);
        String fileName = "SalesReport_" + timestamp + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            System.out.println("\nSales report saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing report file:");
            e.printStackTrace();
        }
    }
}