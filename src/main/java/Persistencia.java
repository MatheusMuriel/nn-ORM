import ORM.Tabela;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
        String slctTodasAsTabelas = "SELECT * FROM sqlite_master WHERE type='table' AND name NOT LIKE '%sqlite%';";

        ResultSet rst = executarSelect(slctTodasAsTabelas);

        ArrayList<Object> objs = new ArrayList<>();

        try {
            while (rst.next()) {
                String nomeTabela = rst.getString("name");

                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("nome", nomeTabela);

                this.tabelas.add(Tabela.class.cast(createClass("ORM.Tabela", parametros)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor
     * @param nome Nome da classe a ser instanciada.
     * @return Objeto que é uma instancia da classe com o nome informado.
     */
    private Object createClass(String nome, HashMap<String, String> parametros) {
        Object object = null;

        try {
            Class<?> clazz = Class.forName(nome);
            Constructor<?> ctor = clazz.getConstructor(HashMap.class);
            object = ctor.newInstance(parametros);
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
