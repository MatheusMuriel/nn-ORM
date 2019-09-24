package recursos;

import recursos.MVC.controles.Controller;
import recursos.MVC.modelos.Contato;
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
        carregarRelacionamentos();
        carregaDados();
        carregaDadosRelacionamento();
    }

    public Persistencia(boolean populate){

        if (populate){
            conectar();
        }
    }

    public void conectar(){
        try {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("sqlite-jdbc:banco.sqlite");
        } catch (SQLException | ClassNotFoundException e) {
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

        this.carregarTabelas();
        this.carregaDados();
        this.carregarRelacionamentos();
        this.carregaDadosRelacionamento();

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

        this.tabelas = tabs;
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

    private Tabela getTabelaRealacionamento(Tabela t1, Tabela t2) {
        ResultSet rst = getTabelasRelacionamento();
        List<Tabela> tabs = new ArrayList<>();
        try {
            while (rst.next()) {
                String nomeTabela = rst.getString("name");

                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("nome", nomeTabela);

                Class classeORMTabela = Tabela.class;
                Object objClass = Utils.createClass(classeORMTabela.getName(), parametros);

                tabs.add((Tabela) objClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String nomeRelation1 =  t1 + "_" + t2;
        String nomeRelation2 =  t2 + "_" + t1;

        tabs = tabs.stream()
                .filter(t -> t.comparaNome(nomeRelation1) || t.comparaNome(nomeRelation2))
                .collect(Collectors.toList());

        return tabs.get(0);
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

    private void carregaDadosRelacionamento() {
        for (Tabela tb : this.tabelas) {
            // Carrega relacionamentos
            this.relacionamentos.computeIfPresent(tb, (tbl, arL) -> {
                for (Tabela tbRl : arL) {
                    Tabela tbObj = getTabelaRealacionamento(tbl, tbRl);

                    String selectRelacionamentos = "SELECT * FROM " + tbObj + ";";
                    try {
                        ResultSet rstTabRel = executarSelect(selectRelacionamentos);
                        ResultSetMetaData rstTabRelmd = rstTabRel.getMetaData();

                        String nomeT1 = rstTabRelmd.getColumnName(1);
                        String nomeT2 = rstTabRelmd.getColumnName(2);

                        String finalNomeT = nomeT1;
                        Tabela t1 = this.getAllTabelas().stream()
                                .filter(t -> t.getNome().equals(finalNomeT.replaceAll("_fk", "")))
                                .collect(Collectors.toList()).get(0);

                        String finalNomeT1 = nomeT2;
                        Tabela t2 = this.getAllTabelas().stream()
                                .filter(t -> t.getNome().equals(finalNomeT1.replaceAll("_fk", "")))
                                .collect(Collectors.toList()).get(0);
                        while (rstTabRel.next()) {
                            String valorT1 = rstTabRel.getString(nomeT1);
                            String valorT2 = rstTabRel.getString(nomeT2);


                            Object o1 = t1.filtraLinhasPorId(valorT1);
                            Object o2 = t2.filtraLinhasPorId(valorT2);

                            if (o1 != null && o2 != null) {
                                Class<?> clazz1 = o1.getClass();
                                Class<?> clazz2 = o2.getClass();

                                Field[] f1 = clazz1.getDeclaredFields();
                                Field[] f2 = clazz2.getDeclaredFields();

                                try {
                                    for(Field field : clazz1.getDeclaredFields()) {
                                        String nomeAtt = field.getName();
                                        if (nomeAtt.contains(nomeT2.replaceAll("_fk",""))) {
                                            field.setAccessible(true);
                                            Object oAtt1 = field.get(o1);
                                            ArrayList l1 = (ArrayList)oAtt1;
                                            if ( l1.stream().noneMatch(o -> o.toString().equals(o2.toString())) ){
                                                l1.add(o2);
                                            }
                                            //this.tabelas.set(this.tabelas.indexOf(tb), tb);
                                        }
                                    }

                                    for(Field field : clazz2.getDeclaredFields()) {
                                        String nomeAtt = field.getName();
                                        if (nomeAtt.contains(nomeT1.replaceAll("_fk",""))) {
                                            field.setAccessible(true);
                                            Object oAtt2 = field.get(o2);
                                            ArrayList l2 = (ArrayList)oAtt2;
                                            if ( l2.stream().noneMatch(o -> o.toString().equals(o1.toString())) ){
                                                l2.add(o1);
                                            }
                                            //this.tabelas.set(this.tabelas.indexOf(tb), tb);
                                        }
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            System.out.println();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return arL;
            });
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

                this.relacionamentos.computeIfAbsent( t1, k -> new ArrayList<>() ).add(t2);
                this.relacionamentos.computeIfAbsent( t2, k -> new ArrayList<>() ).add(t1);

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
            //tabelaDoObjeto.adicionarObjeto(objeto);
        } else {
            System.err.println("Aviso em Tabela::salvarObjeto. Tabela desconhecida.");
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
        this.carregaDados();
    }

    public <T> void removerObjeto(T objeto) {
        String nomeTabela = Utils.relativeNomeClasse(objeto.getClass().getName()).toLowerCase();
        String id_Ob = "";

        Tabela tabelaDoObjeto = this.tabelas.stream()
                .filter(tb -> tb.getNome().equals(nomeTabela))
                .findAny()
                .orElse(null);

        if (tabelaDoObjeto != null) {
            id_Ob = tabelaDoObjeto.getIdPorObject(objeto);
            tabelaDoObjeto.removerObjeto(objeto);

            String deleteComand = "DELETE FROM " + nomeTabela + " WHERE id_" + nomeTabela + " = " + id_Ob + ";";

            if ( !id_Ob.isEmpty() ) {
                this.executar(deleteComand);
            }
            this.carregaDados();
            this.carregaDadosRelacionamento();
        } else {
            System.err.println("Aviso em Tabela::removerObjeto. Tabela desconhecida.");
        }
    }

    public <T> void salvarRelacao(T obj1, T obj2) {

        this.carregaDados();
        this.carregarRelacionamentos();
        this.carregaDadosRelacionamento();

        String nomeT1 = Utils.relativeNomeClasse(obj1.getClass().getName()).toLowerCase();
        String nomeT2 = Utils.relativeNomeClasse(obj2.getClass().getName()).toLowerCase();

        Tabela t1 = this.getAllTabelas().stream()
                .filter(t -> t.getNome().equals(nomeT1.replaceAll("_fk", "")))
                .collect(Collectors.toList()).get(0);

        Tabela t2 = this.getAllTabelas().stream()
                .filter(t -> t.getNome().equals(nomeT2.replaceAll("_fk", "")))
                .collect(Collectors.toList()).get(0);

        Object ob1 = null;
        List lOb1 = t1.getLinhas().stream()
                .filter(l -> l.toString().equals(obj1.toString()))
                .collect(Collectors.toList());

        Object ob2 = null;
        List lOb2 = t2.getLinhas().stream()
                .filter(l -> l.toString().equals(obj2.toString() ) )
                .collect(Collectors.toList());

        if ( lOb1.isEmpty() || lOb2.isEmpty() ) {
            System.err.println("Aviso em Persistencia::salvarRelacao. Objeto não existe na lista.");
            return;
        } else {
            ob1 = lOb1.get(0);
            ob2 = lOb2.get(0);
        }

        String nomeRelation1 =  nomeT1 + "_" + nomeT2;
        String nomeRelation2 =  nomeT2 + "_" + nomeT1;

        ResultSet rst = getTabelasRelacionamento();

        ArrayList<String> colunas = new ArrayList<>();
        colunas.add(nomeT1 + "_fk");
        colunas.add(nomeT2 + "_fk");

        ArrayList<String> valores = new ArrayList<>();

        List<Field> atributos1 = getAtributos(ob1);
        List<Field> atributos2 = getAtributos(ob2);

        atributos1.forEach(fi -> fi.setAccessible(true));
        atributos2.forEach(fi -> fi.setAccessible(true));

        Field f1 = atributos1.stream()
                .filter(field -> field.getName().contains("id_"))
                .collect(Collectors.toList())
                .get(0);

        Field f2 = atributos2.stream()
                .filter(field -> field.getName().contains("id_"))
                .collect(Collectors.toList())
                .get(0);

        f1.setAccessible(true);
        f2.setAccessible(true);

        String id1 = "";
        String id2 = "";

        try {
            id1 = String.valueOf(f1.get(ob1));
            id2 = String.valueOf(f2.get(ob2));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        valores.add(id1);
        valores.add(id2);

        try {
            while (rst.next()){
                String nomeTabela = rst.getString("name");

                if (nomeTabela.equals(nomeRelation1) || nomeTabela.equals(nomeRelation2)) {
                    genericInsert(nomeTabela, colunas, valores);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.carregaDados();
        this.carregarRelacionamentos();
        this.carregaDadosRelacionamento();
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
                        // TODO refatorar nome do metodo para Annotation.
                        System.err.println("Aviso em Persistencia::convertAnotation. Tentativa de conversão de annotation de tipo desconhecido.");
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

        private static Class getControllerPorNomeDoModelo(String nome) {
            StringJoiner sj = new StringJoiner(".");
            sj.add("recursos.MVC.controles");
            sj.add(capitalize(nome) + "Controller");

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
    }
}
