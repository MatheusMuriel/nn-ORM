import java.sql.*;

public class Persistencia {

    public Persistencia(){
        conectar();
    }

    public static void conectar(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:banco.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
