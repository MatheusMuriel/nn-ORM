import ORM.Coluna;
import ORM.Tabela;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Metodo que constroi tabelas para o banco.
     * @param modelo Instancia de uma classe do pacote de modelos, com os atributos já definidos.
     * @param <T> Classe generica.
     * Referencias de Annotations:
     * https://www.devmedia.com.br/como-criar-anotacoes-em-java/32461
     * https://www.guj.com.br/t/ler-annotation-de-uma-classe/100443
     */
    public <T> Tabela construirTabela(T modelo) {

        Field[] _f  =   modelo.getClass().getDeclaredFields();
        List<Field> f = Arrays.asList(_f);
        f.forEach(fi -> fi.setAccessible(true));

        /*ArrayList<Coluna> cols_groups = new ArrayList<>();
        cols_groups.add(new Coluna("id_grupo","INTEGER","PRIMARY KEY"));
        cols_groups.add(new Coluna("descricao","",""));
        ArrayList<String> conts_groups = new ArrayList<>();
        Tabela tb_groups = new Tabela("", "grupo", cols_groups, conts_groups);
        db.executar(tb_groups.toSQLCreate());*/

        String nomeTabela = relativeNomeClasse(modelo.getClass().getName()).toLowerCase();

        ArrayList<Coluna> colunas = new ArrayList<>();

        for (Field fi : f) {
            String nomeColuna = fi.getName();
            String tipoDeDado = converteTipo(fi.getType().getName());
            String constraints = convertAnotation(fi.getAnnotations());

            Coluna cl = new Coluna(nomeColuna, tipoDeDado, constraints);
            colunas.add(cl);
        }

        return new Tabela(nomeTabela, colunas);
    }

    private String relativeNomeClasse(String nm) {
        return nm.substring(nm.lastIndexOf(".") + 1);
    }

    private String convertAnotation(Annotation[] annotations) {
        StringJoiner sj = new StringJoiner(" ");

        for (Annotation an : annotations) {
            String nm = relativeNomeClasse(an.annotationType().getName());

            switch (nm.toUpperCase()){
                case "CHAVEPRIMARIA":
                    sj.add("PRIMARY KEY");
                    break;
                case "UNICO":
                    sj.add("UNIQUE");
                    break;
                case "OBRIGATORIO":
                    sj.add("NOT NULL");
                    break;
                default:
                    System.err.println("Tentativa de conversão de annotation de tipo desconhecido.");
                    break;
            }
        }

        return sj.toString();
    }

    private String converteTipo(String name) {
        name = relativeNomeClasse(name);
        switch (name.toUpperCase()){
            case "INT":
                return "INTEGER";
            case "STRING":
                return "TEXT";
            default:
                System.out.println("Tentativa de conversão de dado de tipo desconhecido.");
                return "";
        }
    }
}
