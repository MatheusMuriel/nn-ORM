package recursos;

import recursos.MVC.modelos.Grupo;
import recursos.ORM.Coluna;
import recursos.ORM.Tabela;

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
    HashMap<Tabela, ArrayList<Tabela>> relacionamentos = new HashMap<>();

    public Persistencia(){
        conectar();
        carregarTabelas();
        carregaColunas();
        carregaDados();
        carregarRelacionamentos();
        System.out.println();
    }

    public Persistencia(boolean populate){

        if (populate){
            conectar();
        }
    }

    public void conectar(){
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:banco.sqlite");
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

            return rst;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo que faz uma copia da lista de tabelas.
     * @return Copia da this.tabelas
     */
    public ArrayList<Tabela> getAllTabelas() {
        ArrayList<Tabela> tbOriginal = this.tabelas;

        return (ArrayList<Tabela>)tbOriginal.clone();
    }

    public ArrayList<Tabela> getTabelaPorNome(String nome) {

        // Pega todas as tabelas
        ArrayList<Tabela> tabelasFiltradas = getAllTabelas();

        //Remove as que NÃO tem o nome igual.
        tabelasFiltradas.removeIf( tb -> !(tb.getNome().equals(nome)));

        return tabelasFiltradas;
    }

    public <T> ArrayList<Tabela> getTabelaPorClasse(Class<T> tClass) {

        // Relativiza o nome pq as tabelas só tem o nome relativo.
        // Transforma em LowerCase pq esse é o padrão de nome de tabela.
        String nomeTClass = Utils.relativeNomeClasse(tClass.getName()).toLowerCase();

        return getTabelaPorNome(nomeTClass);
    }

    /**
     * Metodo responsavel por carregar as tabelas do banco
     * (com exceção das tabelas de controle do sqlite), às
     * transformar em objetos e adicionar na lista de tabelas.
     *
     * Obs: As tabelas de referencias normalmente não vão para a lista de tabelas
     * pois elas abstraidas e assim ficam invisiveis ao programador.
     */
    private void carregarTabelas() {
        ArrayList<Tabela> tabs = carregarListaTabelas();
        this.tabelas.addAll(tabs);
    }

    /**
     * Metodo responsavel por retornar uma lista das tabelas do banco
     * (com exceção das tabelas de controle do sqlite)
     * transformadas em objetos.
     *
     * @return ArrayList contendo as instancias das classes dos modelos das tabelas do banco.
     */
    private ArrayList<Tabela> carregarListaTabelas() {
        ArrayList<Tabela> tabelaDeSaida = new ArrayList<>();
        String slctTodasAsTabelas = "SELECT * FROM sqlite_master WHERE type='table' AND name NOT LIKE '%sqlite%' AND name NOT LIKE '%\\_%' ESCAPE '\\'";

        ResultSet rst = executarSelect(slctTodasAsTabelas);

        try {
            while (rst.next()) {
                String nomeTabela = rst.getString("name");

                Object o = Objects.requireNonNull( Utils.getModeloPorNome(nomeTabela) );

                String nomeModelo = Utils.relativeNomeClasse(o.getClass().getName());

                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("nome", nomeTabela);
                parametros.put("modelo", nomeModelo);

                Class classeORMTabela = Tabela.class;
                Object objClass = Utils.createClass(classeORMTabela.getName(), parametros);

                tabelaDeSaida.add((Tabela) objClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tabelaDeSaida;
    }

    private ResultSet getTabelasOcultas() {
        String slctTodasAsTabelas = "SELECT * FROM sqlite_master WHERE type='table' AND name NOT LIKE '%sqlite%'";
        ResultSet rst = executarSelect(slctTodasAsTabelas);
        return rst;
    }

    private ResultSet getTabelasRelacionamento() {
        String slctTodasAsTabelas = "SELECT * FROM sqlite_master WHERE type='table'" +
                " AND name NOT LIKE '%sqlite%' AND name LIKE '%\\_%' ESCAPE '\\';";
        ResultSet rst = executarSelect(slctTodasAsTabelas);
        return rst;
    }

    /**
     * Metodo responsavel por carregar todos as colunas das tabelas e os transformar em objetos.
     */
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

                    Object classeInstanciada = Utils.createClass(Coluna.class.getName(), parametros);
                    tb.adicionarColuna( (Coluna) classeInstanciada );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo responsavel por carregar todas as linhas das tabelas e as transformar em objetos.
     */
    private void carregaDados() {
        try {
            for (Tabela tb : this.tabelas) {
                String nomeTab = tb.getNome();
                String slctDadosTabela = "SELECT * FROM " + nomeTab + ";";
                ResultSet rst = executarSelect(slctDadosTabela);

                HashMap<String, String> parametros = new HashMap<>();
                String nomeClaseObjeto = "recursos.MVC.modelos." + Utils.capitalize(nomeTab);

                ResultSetMetaData rsmd = rst.getMetaData();

                while (rst.next()) {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String nomeColua = rsmd.getColumnName(i);
                        String valorColuna = rst.getString(nomeColua);
                        parametros.put(nomeColua, valorColuna);
                    }
                    Object o = Utils.createClass(nomeClaseObjeto,parametros);
                    tb.adicionarObjeto(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo responsavel por carregar as tabelas de relacionamento.
     * As adiciona no hashmap de relacionamento.
     */
    private void carregarRelacionamentos() {

        ResultSet rst = getTabelasRelacionamento();

        try {
            while (rst.next()){
                String nomeTabela = rst.getString("name");
                String[] tabelasDoRelacionamento = nomeTabela.split("_");

                String nomeT1 = tabelasDoRelacionamento[0];
                String nomeT2 = tabelasDoRelacionamento[1];

                List<Tabela> tabs = getAllTabelas();

                Tabela t1 = tabs.stream()
                        .filter(tabela -> tabela.getNome().equals(nomeT1))
                        .collect(Collectors.toList()).get(0);

                Tabela t2 = tabs.stream()
                        .filter(tabela -> tabela.getNome().equals(nomeT2))
                        .collect(Collectors.toList()).get(0);

                this.relacionamentos.computeIfAbsent( t1, k -> new ArrayList<Tabela>() ).add(t2);
                this.relacionamentos.computeIfAbsent( t2, k -> new ArrayList<Tabela>() ).add(t1);

                this.relacionamentos.computeIfPresent( t1, (tb, arL ) -> {
                    if (arL.stream().noneMatch(tabela -> tabela.comparaNome(t2))) {
                        arL.add(t2);
                    }
                    return arL;
                });

                this.relacionamentos.computeIfPresent( t2, (tb, arL ) -> {
                    if (arL.stream().noneMatch(tabela -> tabela.comparaNome(t1))) {
                        arL.add(t1);
                    }
                    return arL;
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo apenas para ambiente de desenvolvimento.
     * Dropa todas as tabelas do banco.
     */
    public static void droparTodasAsTabelas() {
        try {
            String slctTodasAsTabelas = "SELECT * FROM sqlite_master WHERE type='table' AND name NOT LIKE '%sqlite%'";
            Connection conn = DriverManager.getConnection("jdbc:sqlite:banco.sqlite");
            Statement stm = conn.createStatement();
            ResultSet rst = stm.executeQuery(slctTodasAsTabelas);


            ArrayList<String> tabelas = new ArrayList<>();

            while (rst.next()) {
                String nomeTabela = rst.getString("name");
                tabelas.add(nomeTabela);
            }

            tabelas.forEach(s -> {
                StringJoiner comandoDrop = new StringJoiner(" ");
                comandoDrop.add("DROP");
                comandoDrop.add("TABLE");
                comandoDrop.add(s);

                try {
                    Statement stm_ = conn.createStatement();
                    stm_.execute(comandoDrop.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo generico para adicionar valores em tabelas.
     *
     * Sintaxe: https://www.sqlite.org/lang_insert.html
     */
    private void genericInsert(String tabela, ArrayList<String> colunas, ArrayList<String> valores) {

        StringJoiner sj = new StringJoiner(" ");

        sj.add("INSERT");

        sj.add("INTO");
        sj.add(tabela);
        sj.add("(");

        //Colunas
        StringJoiner sjColunas = new StringJoiner(",");
        for (String col : colunas) {
            sjColunas.add(col);
        }

        sj.add(sjColunas.toString());
        sj.add(")");

        sj.add("VALUES");
        sj.add("(");

        //Valores
        StringJoiner sjValores = new StringJoiner(",");
        for (String val : valores) {
            sjValores.add("'" + val + "'");
        }

        sj.add(sjValores.toString());
        sj.add(")");


        try{
            executar(sj.toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

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

        List<Field> f = getAtributos(modelo);
        f.forEach(fi -> fi.setAccessible(true));

        String nomeTabela = Utils.relativeNomeClasse(modelo.getClass().getName()).toLowerCase();

        ArrayList<Coluna> colunas = new ArrayList<>();

        for (Field fi : f) {
            String nomeColuna = fi.getName();
            String tipoDeDado = Utils.converteTipo(fi.getType().getName());
            String constraints = Utils.convertAnotation(fi.getAnnotations());

            Coluna cl = new Coluna(nomeColuna, tipoDeDado, constraints);
            colunas.add(cl);
        }

        return new Tabela(nomeTabela, colunas);
    }

    /**
     * Metodo que salva um objeto de um modelo em sua respectiva tabela.
     * @param objeto Instancia do modelo(Com os dados já definidos)
     * @param <T> Tipo generico do modelo.
     */
    public <T> void salvarObjeto(T objeto) {
        String nomeTabela = Utils.relativeNomeClasse(objeto.getClass().getName()).toLowerCase();

        Tabela tabelaDoObjeto = this.tabelas.stream()
            .filter(tb -> tb.getNome().equals(nomeTabela))
            .findAny()
            .orElse(null);

        if (tabelaDoObjeto != null) {
            tabelaDoObjeto.adicionarObjeto(objeto);
        } else {
            System.err.println("Tabela desconhecida.");
        }

        List<Field> f = getAtributos(objeto);
        f.forEach(fi -> fi.setAccessible(true));

        ArrayList<String> colunas = new ArrayList<>();
        ArrayList<String> valores = new ArrayList<>();

        for (Field fi : f) {
            String constraints = Utils.convertAnotation(fi.getAnnotations());
            try {
                String nomeColuna = fi.getName();
                boolean isId = nomeColuna.startsWith("id_");
                if (!isId) {
                    colunas.add(nomeColuna);
                    valores.add(String.valueOf(fi.get(objeto)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        genericInsert(nomeTabela, colunas, valores);
    }

    public <T> void salvarRelacao(T obj1, T obj2) {
        String nomeT1 = Utils.relativeNomeClasse(obj1.getClass().getName()).toLowerCase();
        String nomeT2 = Utils.relativeNomeClasse(obj2.getClass().getName()).toLowerCase();

        // TODO rever ordem dos nomes.
        String nomeRelation =  nomeT1 + "_" + nomeT2;

        //ArrayList<String>

    }

    /**
     * Metodo generico que retorna os atributos de uma classe.
     * @param clazz Classe a qual se deseja obter os atributos.
     * @param <T> Classe generica.
     * @return Lista de Fields, que são os atributos da classe.
     */
    private <T> List<Field> getAtributos(T clazz) {
        Field[] _f  =   clazz.getClass().getDeclaredFields();
        List<Field> f = Arrays.asList(_f);
        return f;
    }

    /**
     * https://www.sqlite.org/lang_delete.html
     * @param tClass
     * @param <T>
     */
    public static <T> void truncarTabelaPorClasse(Class<T> tClass) {
        Class clas = Utils.getModeloPorClasse(tClass);
        String nomeRelativo = Utils.relativeNomeClasse(clas.getName());
        try {
            StringJoiner comandoTruncate = new StringJoiner(" ");
            comandoTruncate.add("DELETE");
            comandoTruncate.add("FROM");
            comandoTruncate.add(nomeRelativo);

            Connection conn = DriverManager.getConnection("jdbc:sqlite:banco.sqlite");
            Statement stm = conn.createStatement();
            stm.execute(comandoTruncate.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class Utils {
        private static String capitalize(String _str) {
            String str = _str.substring(0, 1).toUpperCase() + _str.substring(1);
            return str;
        }

        private static String relativeNomeClasse(String nm) {
            String str = nm.substring(nm.lastIndexOf(".") + 1);
            return str;
        }

        private static String convertAnotation(Annotation[] annotations) {
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

        private static String converteTipo(String name) {
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

        /**
         * https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor
         * @param nome Nome da classe a ser instanciada.
         * @return Objeto que é uma instancia da classe com o nome informado.
         */
        private static Object createClass(String nome, HashMap<String, String> parametros) {
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

        private static Class getModeloPorNome(String nome) {
            StringJoiner sj = new StringJoiner(".");
            sj.add("recursos.MVC.modelos");
            sj.add(capitalize(nome));

            Class<?> clazz = null;
            try {
                clazz = Class.forName(sj.toString());
                Constructor<?> ctor = clazz.getConstructor();
                Object object = ctor.newInstance();
                return object.getClass();
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static <T> Class getModeloPorClasse(Class<T> tClass) {

            String nomeTClass = Utils.relativeNomeClasse(tClass.getName()).toLowerCase();

            return getModeloPorNome(nomeTClass);
        }
    }
}
