import ORM.Tabela;

import java.sql.*;

public class Persistencia {
    Connection conn;

    public Persistencia(){
        conectar();
    }

    public void conectar(){
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:banco.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executar(String... comandos){
        try {
            Statement stm = this.conn.createStatement();

            for (String comando : comandos){

                stm.execute(comando);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executarSelect(String comando) {
        try {
            Statement stm = this.conn.createStatement();

            ResultSet rst = stm.executeQuery(comando);

            while (rst.next()) {
                System.out.println(rst.getInt("id_contact") +  "\t" +
                        rst.getString("primeiro_nome") + "\t" +
                        rst.getString("ultimo_nome") + "\t" +
                        rst.getString("email") + "\t");
            }

            System.out.println(rst);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
