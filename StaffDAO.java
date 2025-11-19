import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffDAO {

    public static Staff getStaffByID(int staffID) {
        String sql = "SELECT * FROM Staff WHERE StaffID = ?";
        Staff staff = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("Status");           // read the ENUM column
                    boolean isActive = "Active".equalsIgnoreCase(status); // map to boolean

                    staff = new Staff(
                        rs.getInt("StaffID"),
                        rs.getString("Name"),
                        rs.getString("Credentials"),
                        isActive
                    );
                    staff.setQuota(rs.getInt("Quota")); // optional: set quota
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staff;
    }
}
