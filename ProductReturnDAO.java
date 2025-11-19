import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProductReturnDAO {

    public static void insertReturn(ProductReturn returnObj) {
        String sql = "INSERT INTO ProductReturn (ProductID, ClientID, StaffID, Reason, Quantity, ReturnDate, PurchaseDate) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateProductSql = "UPDATE Product SET Quantity = ? WHERE ProductID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             PreparedStatement updateStmt = conn.prepareStatement(updateProductSql)) {

            pstmt.setInt(1, returnObj.getProduct().getProductID());
            pstmt.setInt(2, returnObj.getClient().getClientID());
            pstmt.setInt(3, returnObj.getStaff().getStaffID());
            pstmt.setString(4, returnObj.getReason());
            pstmt.setInt(5, returnObj.getQuantity());
            pstmt.setTimestamp(6, Timestamp.valueOf(returnObj.getTransactionDate().atStartOfDay()));
            pstmt.setDate(7, java.sql.Date.valueOf(returnObj.getPurchaseDate()));

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " row(s) inserted into ProductReturn table.");

        // Update inventory if product can be restocked
            long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(
                    java.time.LocalDate.now(), returnObj.getProduct().getExpirationDate());

            if (daysUntilExpiry >= 30) {
                int newQuantity = returnObj.getProduct().getQuantity() + returnObj.getQuantity();

                updateStmt.setInt(1, newQuantity);
                updateStmt.setInt(2, returnObj.getProduct().getProductID());  // match your parameter style
                int rowsUpdated = updateStmt.executeUpdate();

                System.out.println("Product inventory updated. New quantity: " + newQuantity +
                                   " (" + rowsUpdated + " row(s) affected).");
            } else {
                System.out.println("Returned product cannot be restocked due to expiration or near-expiry.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
         // REPORT 1: FULL REFUND/RETURN REPORT
    public void getFullReturnReport() {
        String sql = """
            SELECT rt.ReturnID, rt.ReturnDate, p.ProductName,
                   rt.Quantity, rt.Reason,
                   s.Name AS StaffName
            FROM ProductReturn rt
            JOIN Product p ON rt.ProductID = p.ProductID
            JOIN Staff s ON rt.StaffID = s.StaffID
            ORDER BY rt.ReturnDate DESC
        """;

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== FULL RETURN/REFUND REPORT ===");

            while (rs.next()) {
                System.out.println("Return ID: " + rs.getInt("ReturnID"));
                System.out.println("Date: " + rs.getDate("ReturnDate"));
                System.out.println("Product: " + rs.getString("ProductName"));
                System.out.println("Qty: " + rs.getInt("Quantity"));
                System.out.println("Reason: " + rs.getString("Reason"));
                System.out.println("Staff: " + rs.getString("StaffName"));
                System.out.println("---------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REPORT 2: TOTAL RETURNS HANDLED BY EACH STAFF
    public void getReturnsPerStaff() {
        String sql = """
            SELECT s.Name, COUNT(rt.ReturnID) AS TotalReturns
            FROM ProductReturn rt
            JOIN Staff s ON rt.StaffID = s.StaffID
            GROUP BY s.Name
            ORDER BY TotalReturns DESC
        """;

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== TOTAL RETURNS PER STAFF ===");

            while (rs.next()) {
                System.out.println("Staff: " + rs.getString("Name") +
                                   " | Total Returns: " + rs.getInt("TotalReturns"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REPORT 3: MOST RETURNED PRODUCTS
    public void getMostReturnedProducts() {
        String sql = """
            SELECT p.ProductName, SUM(rt.Quantity) AS TotalReturned
            FROM ProductReturn rt
            JOIN Product p ON rt.ProductID = p.ProductID
            GROUP BY p.ProductName
            ORDER BY TotalReturned DESC
        """;

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=== MOST RETURNED PRODUCTS ===");

            while (rs.next()) {
                System.out.println("Product: " + rs.getString("ProductName") +
                                   " | Returned Qty: " + rs.getInt("TotalReturned"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
