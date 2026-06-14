
import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:cafe.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS menu_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "category TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "stock INTEGER NOT NULL)";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
