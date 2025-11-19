import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryReport {

    // SQL ready fields (reportDate can be saved)
    private LocalDate reportDate;
    private List<Product> lowStockProducts = new ArrayList<>();

    public InventoryReport() {
        this.reportDate = LocalDate.now();
    }

    public void generateReport(List<Product> products) {

        System.out.println("\n========== INVENTORY REPORT ==========");
        System.out.println("Date: " + reportDate);

        for (Product p : products) {
            p.viewStockLevels();

            int initialSupply = 100;   // simulated
            int threshold = (int) (initialSupply * 0.20); // 20%

            if (p.getQuantity() <= threshold) {
                lowStockProducts.add(p);
            }
        }

        System.out.println("\n--- LOW STOCK ALERTS ---");

        if (lowStockProducts.isEmpty()) {
            System.out.println("No low-stock products.");
        } else {
            for (Product p : lowStockProducts) {
                System.out.println("LOW STOCK: " + p.getProductID() +
                        " | Brand: " + p.getBrand() +
                        " | Qty: " + p.getQuantity());
            }
        }
    }

    /*
    public void saveSummaryToDB(Connection conn) throws SQLException {
        String sql = "INSERT INTO InventoryReport (reportDate) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDate(1, Date.valueOf(reportDate));
        ps.executeUpdate();
    }
    */
}
