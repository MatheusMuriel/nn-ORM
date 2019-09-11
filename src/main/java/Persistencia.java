import ORM.Coluna;
import ORM.Tabela;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Persistencia {
    Connection conn;
    ArrayList<Tabela> tabelas = new ArrayList<>();

    public Persistencia(){
        conectar();
        //carregarTabelas();
        //carregaColunas();
        //getTabelaPorNome("contato");
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

    public ArrayList<Tabela> getAllTabelas() {
        ArrayList<Tabela> tbOriginal = this.tabelas;

        return (ArrayList<Tabela>)tbOriginal.clone();
    }

    public ArrayList<Tabela> getTabelaPorNome(String nome) {
        ArrayList<Tabela> tabelas = getAllTabelas();

        tabelas.removeIf( tb -> !(tb.getNome().equals(nome)));

        return tabelas;
    }

    /**
     * Metodo responsavel por carregar todos os dados do banco e os transformar em objetos.
     */
    private void carregarTabelas() {
        String slctTodasAsTabelas = "SELECT * FROM sqlite_master WHERE type='table' AND name NOT LIKE '%sqlite%';";
        ResultSet rst = executarSelect(slctTodasAsTabelas);

        try {
            while (rst.next()) {
                String nomeTabela = rst.getString("name");

                StringJoiner sj = new StringJoiner(".");
                sj.add("MVC");
                sj.add("modelos");
                sj.add(capitalize(nomeTabela));

                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("nome", nomeTabela);
                parametros.put("modelo", sj.toString());

                this.tabelas.add(Tabela.class.cast(createClass("ORM.Tabela", parametros)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregaColunas() {
        try {
            for (Tabela tb : this.tabelas) {
                String nomeTab = tb.getNome();
                String slctDadosTabela = "PRAGMA table_info(" + nomeTab + ");";
                ResultSet rst = executarSelect(slctDadosTabela);

                while (rst.next()) {
                    String nomeColuna = rst.getString("name");
                    String tipoColuna = rst.getString("type");

                    HashMap<String, String> parametros = new HashMap<>();
                    parametros.put("nome", nomeColuna);
                    parametros.put("tipoDeDado", tipoColuna);

                    tb.adicionarColuna(Coluna.class.cast(createClass("ORM.Coluna", parametros)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregaDados() {
        try {
            for (Tabela tb : this.tabelas) {
                String nomeTab = tb.getNome();
                String slctDadosTabela = "SELECT * FROM " + nomeTab + ";";
                ResultSet rst = executarSelect(slctDadosTabela);

                while (rst.next()) {

                    for (Coluna c : tb.getColunas()) {

                    }
                    String nomeColuna = rst.getString("name");
                    String tipoColuna = rst.getString("type");

                    HashMap<String, String> parametros = new HashMap<>();
                    parametros.put("nome", nomeColuna);
                    parametros.put("tipoDeDado", tipoColuna);

                    tb.adicionarColuna(Coluna.class.cast(createClass("ORM.Coluna", parametros)));
                }
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

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public <T> void construirTabela(T modelo) {

        Field[] f  =   modelo.getClass().getDeclaredFields();
        System.out.println("Os Campos da Classe São : ");

        for (int i = 0 ; i < f.length ; i++){
            f[i].setAccessible(true);
            try {
                Annotation[] ant = f[i].getAnnotations();
                System.out.println("Nome : " + f[i].getName() + "  Valor: " + f[i].get(modelo) + "  Anotantion: " + f[i].getAnnotations());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
