import ORM.Tabela;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;

public class Persistencia {
    Connection conn;
    ArrayList<Tabela> tabelas = new ArrayList<>();

    public Persistencia(){
        conectar();
        carregarTodosOsDados();
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

    public ResultSet executarSelect(String comando) {
        try {
            Statement stm = this.conn.createStatement();

            ResultSet rst = stm.executeQuery(comando);

            /*while (rst.next()) {
                System.out.println(rst.getInt("id_contact") +  "\t" +
                        rst.getString("primeiro_nome") + "\t" +
                        rst.getString("ultimo_nome") + "\t" +
                        rst.getString("email") + "\t");
            }*/

            return rst;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo responsavel por carregar todos os dados do banco e os transformar em objetos.
     */
    private void carregarTodosOsDados() {
        String slctTodasAsTabelas = "SELECT * FROM sqlite_master WHERE type='table';";

        ResultSet rst = executarSelect(slctTodasAsTabelas);

        Object c = createClass("ORM.Coluna");
        System.out.println();
    }

    /**
     * https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor
     * @param nome Nome da classe a ser instanciada.
     * @return Objeto que é uma instancia da classe com o nome informado.
     */
    private Object createClass(String nome) {
        Object object = null;

        try {
            Class<?> clazz = Class.forName(nome);
            Constructor<?> ctor = clazz.getConstructor();
            object = ctor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;
    }

}
