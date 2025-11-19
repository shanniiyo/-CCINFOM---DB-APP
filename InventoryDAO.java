import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // =====================================================
    // ADD PRODUCT
    // =====================================================
    public static void addProduct(Product product) {
        String sql = "INSERT INTO Product (ProductID, Brand, Quantity, Price, DateAdded, ExpirationDate, LowStockLimit) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getProductID());
            stmt.setString(2, product.getBrand());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setObject(5, product.getDateAdded());
            stmt.setObject(6, product.getExpirationDate());
            stmt.setInt(7, product.getLowStockLimit());

            stmt.executeUpdate();
            System.out.println("Product added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // GET PRODUCT BY ID
    // =====================================================
    public static Product getProductByID(int productID) {
        String sql = "SELECT * FROM Product WHERE ProductID = ?";
        Product product = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                product = new Product(
                    rs.getInt("ProductID"),
                    rs.getString("Brand"),
                    rs.getInt("Quantity"),
                    rs.getDouble("Price"),
                    rs.getDate("DateAdded").toLocalDate(),
                    rs.getDate("ExpirationDate").toLocalDate(),
                    rs.getInt("LowStockLimit")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    // =====================================================
    // UPDATE PRODUCT QUANTITY (AUTONOMOUS)
    // =====================================================
    public static void updateProductQuantity(int productID, int newQuantity) {
        String sql = "UPDATE Product SET Quantity = ? WHERE ProductID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productID);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Product quantity updated successfully.");
            } else {
                System.out.println("Product not found for update.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // UPDATE PRODUCT QUANTITY (INSIDE TRANSACTION)
    // =====================================================
    public static void updateProductQuantity(int productID, int newQuantity, Connection conn) throws SQLException {
        String sql = "UPDATE Product SET Quantity = ? WHERE ProductID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productID);
            stmt.executeUpdate();
        }
    }

    // =====================================================
    // UPDATE PRICE
    // =====================================================
    public static void updateProductPrice(int productID, double newPrice) {
        String sql = "UPDATE Product SET Price = ? WHERE ProductID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setInt(2, productID);

            stmt.executeUpdate();
            System.out.println("Product price updated.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================
    public static void deleteProduct(int productID) {
        String sql = "DELETE FROM Product WHERE ProductID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productID);
            stmt.executeUpdate();
            System.out.println("Product deleted.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // GET ALL PRODUCTS
    // =====================================================
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

             while (rs.next()) {
                Product product = new Product(
                    rs.getInt("ProductID"),
                    rs.getString("Brand"),
                    rs.getInt("Quantity"),
                    rs.getDouble("Price"),
                    rs.getDate("DateAdded").toLocalDate(),
                    rs.getDate("ExpirationDate").toLocalDate(),
                    rs.getInt("LowStockLimit")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}
