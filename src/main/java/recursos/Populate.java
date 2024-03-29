package recursos;

import recursos.MVC.controles.ContatoController;
import recursos.MVC.controles.GrupoController;
import recursos.MVC.controles.TelefoneController;
import recursos.MVC.modelos.Contato;
import recursos.MVC.modelos.Grupo;
import recursos.MVC.modelos.Telefone;
import recursos.ORM.Coluna;
import recursos.ORM.Tabela;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Populate {

    public static void criarTabelas(){

        Persistencia db = new Persistencia(true);

        //Groups
        Tabela grupo = db.construirTabela(new Grupo());
        db.executar( grupo.toSQLCreate() );

        //Contacts
        Tabela contato = db.construirTabela(new Contato());
        db.executar( contato.toSQLCreate() );

        //Phones
        Tabela telefone = db.construirTabela(new Telefone());
        db.executar( telefone.toSQLCreate() );

        criarRelacoes(db, contato, grupo);
        criarRelacoes(db, contato, telefone);
    }

    private static void criarRelacoes(Persistencia db, Tabela... tbs){
        StringJoiner nomeTabela = new StringJoiner("_");
        ArrayList<Coluna> colunas = new ArrayList<>();

        for (Tabela t : tbs) {
            String nomeColuna = t.getNome().concat("_fk");
            String constraint = "REFERENCES " + t.getNome();

            Coluna c = new Coluna(nomeColuna,"", constraint);
            colunas.add(c);

            nomeTabela.add(t.getNome());
        }

        Tabela tabelaRelacionamento = new Tabela(nomeTabela.toString(), colunas);

        db.executar(tabelaRelacionamento.toSQLCreate());
    }

    /**
     * Metodo apenas para desenvolvimento.
     * Poputa as tabelas com alguns dados.
     */
    public static void populateTabelas() {
        Persistencia db = new Persistencia(true);
        populateContatos(db);
        populateGrupos(db);
        populateTelefones(db);
        populateContatoTelefone(db);
        populateContatoGrupo(db);
    }

    /**
     * Metodo para popular a tabela de Contatos com alguns dados de exemplo.
     * @param db Instancia de persistencia.
     */
    public static void populateContatos(Persistencia db) {
        ContatoController contControl = new ContatoController(db);
        contControl.novoContato("Jose", "Silva", "jose.silva@gmail.com");
        contControl.novoContato("Maria", "Antonia", "maria_antonia@gmail.com");
        contControl.novoContato("Roberto", "Souza", "robert-souza@gmail.com");
    }

    /**
     * Metodo para popular a tabela de Telefones com alguns dados de exemplo.
     * @param db Instancia de persistencia.
     */
    public static void populateTelefones (Persistencia db) {
        TelefoneController telefControl = new TelefoneController(db);

        telefControl.novoTelefone("99999-9999");
        telefControl.novoTelefone("88888-8888");
        telefControl.novoTelefone("77777-7777");
        telefControl.novoTelefone("66666-6666");
        telefControl.novoTelefone("55555-5555");
    }

    /**
     * Metodo para popular a tabela de Grupos com alguns dados de exemplo.
     * @param db Instancia de persistencia.
     */
    public static void populateGrupos (Persistencia db) {
        GrupoController grupControl = new GrupoController(db);
        grupControl.novoGrupo("Familia");
        grupControl.novoGrupo("Escola");
        grupControl.novoGrupo("Trabalho");
        grupControl.novoGrupo("RPG");
        grupControl.novoGrupo("Teatro");
    }

    public static void populateContatoTelefone(Persistencia db) {
        db = new Persistencia();
        ContatoController cCtrl = new ContatoController(db);
        TelefoneController tCtrl = new TelefoneController(db);

        Contato c1 = cCtrl.procurar("Jose").get(0);
        Contato c2 = cCtrl.procurar("Maria").get(0);

        Telefone t1 = tCtrl.procurar("9999").get(0);
        Telefone t2 = tCtrl.procurar("55555").get(0);

        cCtrl.vincularTelefone(c1, t1);
        cCtrl.vincularTelefone(c2, t2);
    }

    public static void populateContatoGrupo(Persistencia db) {
        db = new Persistencia();
        ContatoController cCtrl = new ContatoController(db);
        GrupoController gCtrl = new GrupoController(db);

        Contato c1 = cCtrl.procurar("Jose").get(0);
        Contato c2 = cCtrl.procurar("Maria").get(0);

        Grupo g1 = gCtrl.procurar("Familia").get(0);
        Grupo g2 = gCtrl.procurar("Trabalho").get(0);

        cCtrl.vincularGrupo(c1, g1);
        cCtrl.vincularGrupo(c2, g2);
    }

    /**
     * Metodo somente para desenvolvimento.
     */
    public static void trucateGrupos() {
        Persistencia.truncarTabelaPorClasse(Grupo.class);
    }

    /**
     * Metodo somente para desenvolvimento.
     */
    public static void trucateContatos() {
        Persistencia.truncarTabelaPorClasse(Contato.class);
    }

    /**
     * Metodo somente para desenvolvimento.
     */
    public static void trucateTelefones() {
        Persistencia.truncarTabelaPorClasse(Telefone.class);
    }
}
