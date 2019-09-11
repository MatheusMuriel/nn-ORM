import MVC.modelos.Contato;
import MVC.modelos.Grupo;
import MVC.modelos.Telefone;
import ORM.Tabela;
import ORM.Coluna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Main{

    public static void main(String... args){
        System.out.println("Hello World!");
        Persistencia db = new Persistencia();

        populateTables(db);

        /*
        populateContacts(db);

        populateTelefones(db);

        populateGrupos(db);*/

        //getAllContatos(db, "contatos");

    }

    public static void populateTables(Persistencia db){
        //Groups
        Tabela grupo = db.construirTabela(new Grupo());
        db.executar( grupo.toSQLCreate() );

        //Contacts
        Tabela contato = db.construirTabela(new Contato());
        db.executar( contato.toSQLCreate() );

        //Phones
        Tabela telefone = db.construirTabela(new Telefone());
        db.executar( telefone.toSQLCreate() );

        createRelation(db, contato, grupo, Relacao.N_N);
        createRelation(db, contato, telefone, Relacao.N_N);
    }

    /**
     * Metodo para criar uma tabela de relacionamento.
     * @param db Instancia da classe de persistencia.
     * @param tb1 Tabela1 da relação.
     * @param tb2 Tabela2 da relação.
     * @param rl Enum com o tipo de ralação.
     */
    private static void createRelation(Persistencia db, Tabela tb1, Tabela tb2, Relacao rl){

        if (rl.equals(Relacao.N_N)){
            //Tabela relacionamento
            ArrayList<Coluna> colunas = new ArrayList<>();

            StringJoiner coluna01 = new StringJoiner(" ");
            coluna01.add("REFERENCES");
            coluna01.add( tb1.getNome() );
            colunas.add(new Coluna(tb1.getNome(),"", coluna01.toString()));

            StringJoiner coluna02 = new StringJoiner(" ");
            coluna02.add("REFERENCES");
            coluna02.add(tb2.getNome());
            colunas.add(new Coluna(tb2.getNome(),"", coluna02.toString()));

            Tabela tb_rel = new Tabela(tb1.getNome().concat("_").concat(tb2.getNome()), colunas);

            db.executar(tb_rel.toSQLCreate());
        }

    }

    public static void populateContacts(Persistencia db) {
        adicionarContato(db,"C", "1", "c1g.com");
        adicionarContato(db,"C", "2", "c2g.com");
        adicionarContato(db,"C", "3", "c3g.com");
        adicionarContato(db,"C", "4", "c4g.com");
    }

    public static void populateTelefones (Persistencia db) {
        adicionaNumero(db, "99999-9999");
        adicionaNumero(db, "88888-8888");
        adicionaNumero(db, "77777-7777");
        adicionaNumero(db, "66666-6666");
        adicionaNumero(db, "55555-5555");
    }

    public static void populateContatoTelefone (Persistencia db) {
        adicionaContatoNumero(db,"C1", "99999-9999");
    }

    public static void populateContatoGrupo (Persistencia db) {
        adicionaNumero(db, "99999-9999");
        adicionaNumero(db, "88888-8888");
        adicionaNumero(db, "77777-7777");
        adicionaNumero(db, "66666-6666");
        adicionaNumero(db, "55555-5555");
    }

    public static void populateGrupos (Persistencia db) {
        adicionaGrupo(db, "Familia");
        adicionaGrupo(db, "Escola");
        adicionaGrupo(db, "Trabalho");
        adicionaGrupo(db, "RPG");
        adicionaGrupo(db, "Teatro");
    }

    /**
     * Metodo que adiciona um contato.
     * @param db Objeto de persistencia.
     * @param nome Nome do contato.
     * @param sobreNome Sobre nome do contato.
     * @param email Email do contato.
     */
    public static void adicionarContato(Persistencia db, String nome, String sobreNome, String email) {

        ArrayList<String> colunas = new ArrayList<>();
        ArrayList<String> valores = new ArrayList<>();

        colunas.add("primeiro_nome");
        colunas.add("ultimo_nome");
        colunas.add("email");

        valores.add(nome);
        valores.add(sobreNome);
        valores.add(email);

        genericInsert(db,"contatos", colunas, valores);
    }

    /**
     * Metodo que adiciona um numero de telefone.
     * @param db Objeto de persistencia.
     * @param numero Numero a ser adicionado.
     */
    public static void adicionaNumero(Persistencia db, String numero) {

        ArrayList<String> colunas = new ArrayList<>();
        ArrayList<String> valores = new ArrayList<>();

        colunas.add("telefone");

        valores.add(numero);

        genericInsert(db,"telefones", colunas, valores);

    }

    /**
     * Adiciona na tabela de relacionamento uma relação entre um contato e um numero
     * @param db Objeto de persistencia.
     * @param contato Contato da relação
     * @param numero Numero da relação
     */
    public static void adicionaContatoNumero(Persistencia db, String contato, String numero) {
        ArrayList<String> colunas = new ArrayList<>();
        ArrayList<String> valores = new ArrayList<>();

        colunas.add("contatos");
        colunas.add("telefones");

        valores.add(contato);
        valores.add(numero);

        genericInsert(db,"contatos_grupos", colunas, valores);
    }

    /**
     * Adiciona na tabela de relacionamento uma relação entre um contato e um grupo
     * @param db Objeto de persistencia.
     * @param contato Contato da relação
     * @param grupo Grupo da relação
     */
    public static void adicionaContatoGrupo(Persistencia db, String contato, String grupo) {
        ArrayList<String> colunas = new ArrayList<>();
        ArrayList<String> valores = new ArrayList<>();

        colunas.add("contatos");
        colunas.add("grupos");

        valores.add(contato);
        valores.add(grupo);

        genericInsert(db,"contatos_grupos", colunas, valores);
    }

    /**
     * Metodo que adiciona um grupo de contatos.
     * @param db Objeto de persistencia.
     * @param descricao descrição do grupo a ser adicionado.
     */
    public static void adicionaGrupo(Persistencia db, String descricao) {

        ArrayList<String> colunas = new ArrayList<>();
        ArrayList<String> valores = new ArrayList<>();

        colunas.add("descricao");

        valores.add(descricao);

        genericInsert(db,"grupos", colunas, valores);

    }

    /**
     * Metodo generico para adicionar valores em tabelas.
     *
     * Sintaxe: https://www.sqlite.org/lang_insert.html
     */
    private static void genericInsert(Persistencia db, String tabela, ArrayList<String> colunas, ArrayList<String> valores) {

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
            db.executar(sj.toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void getContato(Persistencia db){

    }

    private static void getAllContatos(Persistencia db, String tabela){
        StringJoiner sj = new StringJoiner(" ");

        sj.add("SELECT");

        sj.add("*");

        sj.add("FROM");

        sj.add(tabela);

        try{
            db.executarSelect(sj.toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void getContatoPorTelefone(Persistencia db){

    }

    private static void getContatoPorNome(Persistencia db){

    }

    private static void genericSelect(Persistencia db, String tabela) {

    }

    enum Relacao{
        N_N
    }
}