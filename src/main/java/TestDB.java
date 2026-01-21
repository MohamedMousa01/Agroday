
import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/MOHAMED?useSSL=false&serverTimezone=UTC",
                    "root",
                    "Mac.0123"
            );

            System.out.println("✅ Connessione riuscita!");
            conn.close();

        } catch (Exception e) {
            System.out.println("❌ Connessione FALLITA");
            e.printStackTrace();
        }
    }

}
