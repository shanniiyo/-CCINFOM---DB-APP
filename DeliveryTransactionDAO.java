import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryTransactionDAO {

    // Insert a delivery record
    public static void insertDelivery(DeliveryTransaction delivery) {
        String sql = "INSERT INTO DeliveryTransaction (deliveryID, deliveryDate, productID, clientID, staffID, quantity, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, delivery.getDeliveryID());
            stmt.setDate(2, java.sql.Date.valueOf(delivery.getDeliveryDate()));
            stmt.setInt(3, delivery.getProduct().getProductID());
            stmt.setInt(4, delivery.getClient().getClientID());
            stmt.setInt(5, delivery.getStaff().getStaffID());
            stmt.setInt(6, delivery.getQuantity());
            stmt.setString(7, delivery.getStatus());

            stmt.executeUpdate();
            System.out.println("Delivery record successfully saved to the database.");

        } catch (SQLException e) {
            System.out.println("ERROR saving delivery record.");
            e.printStackTrace();
        }
    }

    // Fetch all delivery transactions
    public static List<DeliveryTransaction> getAllDeliveries() {
        List<DeliveryTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryTransaction";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = InventoryDAO.getProductByID(rs.getInt("productID"));
                Client client = ClientDAO.getClientByID(rs.getInt("clientID"));
                Staff staff = StaffDAO.getStaffByID(rs.getInt("staffID"));

                DeliveryTransaction dt = new DeliveryTransaction(
                    rs.getString("deliveryID"),
                    rs.getDate("deliveryDate").toLocalDate(),
                    product,
                    client,
                    staff,
                    rs.getInt("quantity")
                );

                // Set status manually because constructor defaults to PENDING
                if (!rs.getString("status").equalsIgnoreCase("PENDING")) {
                    dt.processDelivery();  // This updates stock; use only if desirable
                }

                list.add(dt);
            }

        } catch (SQLException e) {
            System.out.println("ERROR loading delivery records.");
            e.printStackTrace();
        }
        return list;
    }

    // Fetch deliveries by a specific client
    public static List<DeliveryTransaction> getDeliveriesByClient(int clientID) {
        List<DeliveryTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryTransaction WHERE clientID=?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DeliveryTransaction dt = new DeliveryTransaction(
                    rs.getString("deliveryID"),
                    rs.getDate("deliveryDate").toLocalDate(),
                    InventoryDAO.getProductByID(rs.getInt("productID")),
                    ClientDAO.getClientByID(rs.getInt("clientID")),
                    StaffDAO.getStaffByID(rs.getInt("staffID")),
                    rs.getInt("quantity")
                );
                list.add(dt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Fetch deliveries by staff member
    public static List<DeliveryTransaction> getDeliveriesByStaff(int staffID) {
        List<DeliveryTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM DeliveryTransaction WHERE staffID=?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DeliveryTransaction dt = new DeliveryTransaction(
                    rs.getString("deliveryID"),
                    rs.getDate("deliveryDate").toLocalDate(),
                    InventoryDAO.getProductByID(rs.getInt("productID")),
                    ClientDAO.getClientByID(rs.getInt("clientID")),
                    StaffDAO.getStaffByID(rs.getInt("staffID")),
                    rs.getInt("quantity")
                );
                list.add(dt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

   
    //                    REPORT GENERATORS
    // Summary of SUCCESS vs FAILED deliveries
    public static void generateDeliveryStatusSummary() {
        String sql = """
            SELECT status, COUNT(*) AS total
            FROM DeliveryTransaction
            GROUP BY status
        """;

        System.out.println("\n=== DELIVERY STATUS SUMMARY REPORT ===");

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getString("status") + " : " + rs.getInt("total"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Product delivery summary (how many units delivered per product)
    public static void generateProductDeliverySummary() {
        String sql = """
            SELECT productID, SUM(quantity) as totalDelivered
            FROM DeliveryTransaction
            WHERE status='SUCCESS'
            GROUP BY productID
        """;

        System.out.println("\n=== PRODUCT DELIVERY SUMMARY REPORT ===");

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product p = InventoryDAO.getProductByID(rs.getInt("productID"));
                System.out.println(p.getProductName() + " — " + rs.getInt("totalDelivered") + " units delivered");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Detailed list of all deliveries
    public static void generateFullDeliveryReport() {
        System.out.println("\n=========== FULL DELIVERY REPORT ===========");

        List<DeliveryTransaction> list = getAllDeliveries();

        if (list.isEmpty()) {
            System.out.println("No delivery records found.");
            return;
        }

        for (DeliveryTransaction dt : list) {
            dt.viewDeliveryDetails();
        }
    }
}
