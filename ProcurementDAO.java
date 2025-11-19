import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Statement;
import java.sql.ResultSet;

public class ProcurementDAO {

    public static void addProcurement(Procurement procurement, Staff staff, int addedQty) {

        String procurementSql = "INSERT INTO Procurement (ProductID, SupplierID, Quantity, TransactionDate) "
                   + "VALUES (?, ?, ?, ?)";
        String updateProductSql = "UPDATE Product SET Quantity = Quantity + ? WHERE ProductID = ?";
        String inventoryTransactionSql = "INSERT INTO InventoryTransaction (ProductID, SupplierID, StaffID, Quantity, TransactionType, Status, TransactionDate) VALUES (?, ?, ?, ?, 'Incoming', 'Completed', ?)";


        Connection conn = null;

        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert into Procurement table
            try (PreparedStatement stmt = conn.prepareStatement(procurementSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, procurement.getProduct().getProductID());
                stmt.setInt(2, procurement.getSupplier().getSupplierID());
                stmt.setInt(3, addedQty);
                stmt.setObject(4, procurement.getTransactionDate());
                stmt.executeUpdate();
            }

            // 2. Update Product quantity
            try (PreparedStatement stmt = conn.prepareStatement(updateProductSql)) {
                stmt.setInt(1, addedQty);
                stmt.setInt(2, procurement.getProduct().getProductID());
                stmt.executeUpdate();
            }

            // 3. Record in InventoryTransaction table
            try (PreparedStatement stmt = conn.prepareStatement(inventoryTransactionSql)) {
                stmt.setInt(1, procurement.getProduct().getProductID());
                stmt.setInt(2, procurement.getSupplier().getSupplierID());
                stmt.setInt(3, staff.getStaffID());
                stmt.setInt(4, addedQty);
                stmt.setObject(5, procurement.getTransactionDate());
                stmt.executeUpdate();
            }


            conn.commit();
            System.out.println("Procurement transaction completed successfully.");

        } catch (SQLException e) {
            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to error.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}