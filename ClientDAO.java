
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO {

    public static Client getClientByID(int clientID) {
        String sql = "SELECT * FROM ClientMed WHERE ClientID = ?";
        Client client = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("ClientName"),
                        rs.getString("Address"),
                        rs.getString("ContactPerson"),
                        rs.getString("ContactInfo")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }
}
