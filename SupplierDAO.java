import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    //Select supplier
    public static Supplier getSupplierByID(int supplierID) {
        String sql = "SELECT * FROM Supplier WHERE SupplierID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, supplierID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("Name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // all suppliers
    public static List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("Name")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}