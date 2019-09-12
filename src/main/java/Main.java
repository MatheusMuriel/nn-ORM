import recursos.MVC.modelos.Contato;
import recursos.MVC.modelos.Grupo;
import recursos.MVC.modelos.Telefone;
import recursos.ORM.Tabela;
import recursos.ORM.Coluna;
import recursos.Persistencia;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Main{

    public static void main(String... args){
        System.out.println("Hello World!");
        Persistencia db = new Persistencia();

        db.droparTodasAsTabelas();

        //criarTabelas(db);

        //Populate.populateTabelas(db);
    }

    /**
     * Metodo que transforma os objetos modelos em tabelas no SQL.
     * @param db Instancia de persistencia.
     */
    public static void criarTabelas(Persistencia db){
        //Groups
        Tabela grupo = db.construirTabela(new Grupo());
        db.executar( grupo.toSQLCreate() );

        //Contacts
        Tabela contato = db.construirTabela(new Contato());
        db.executar( contato.toSQLCreate() );

        //Phones
        Tabela telefone = db.construirTabela(new Telefone());
        db.executar( telefone.toSQLCreate() );

        criarRelacoes(db, Relacao.N_N, contato, grupo);
        criarRelacoes(db, Relacao.N_N, contato, telefone);
    }

    /**
     * Metodo para criar uma tabela de relacionamento.
     * @param db Instancia da classe de persistencia.
     * @param rl Enum com o tipo de ralação.
     * @param tbs Vargs de tabelas da relação.
     */
    private static void criarRelacoes(Persistencia db, Relacao rl, Tabela... tbs){
        if ( rl.equals(Relacao.N_N) ) {

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
        } else {
            System.err.println("Falha ao criar tabela de relação. Tipo de relação invalida.");
        }
    }

    enum Relacao{
        N_N
    }

    static class Populate {

        /**
         * Metodo apenas para desenvolvimento.
         * Poputa as tabelas com alguns dados.
         * @param db Instancia de persistencia.
         */
        private static void populateTabelas(Persistencia db) {
            populateContatos(db);
            populateGrupos(db);
            populateTelefones(db);
        }

        /**
         * Metodo para popular a tabela de Contatos com alguns dados de exemplo.
         * @param db Instancia de persistencia.
         */
        public static void populateContatos(Persistencia db) {
            recursos.MVC.controles.Contato contControl = new recursos.MVC.controles.Contato();
            contControl.novoContato(db, "Jose", "Silva", "jose.silva@gmail.com");
            contControl.novoContato(db, "Maria", "Antonia", "maria_antonia@gmail.com");
            contControl.novoContato(db, "Roberto", "Souza", "robert-souza@gmail.com");
        }

        /**
         * Metodo para popular a tabela de Telefones com alguns dados de exemplo.
         * @param db Instancia de persistencia.
         */
        public static void populateTelefones (Persistencia db) {
            recursos.MVC.controles.Telefone telefControl = new recursos.MVC.controles.Telefone();
            telefControl.novoTelefone(db, "99999-9999");
            telefControl.novoTelefone(db, "88888-8888");
            telefControl.novoTelefone(db, "77777-7777");
            telefControl.novoTelefone(db, "66666-6666");
            telefControl.novoTelefone(db, "55555-5555");
        }

        /**
         * Metodo para popular a tabela de Grupos com alguns dados de exemplo.
         * @param db Instancia de persistencia.
         */
        public static void populateGrupos (Persistencia db) {
            recursos.MVC.controles.Grupo grupControl = new recursos.MVC.controles.Grupo();
            grupControl.novoGrupo(db, "Familia");
            grupControl.novoGrupo(db, "Escola");
            grupControl.novoGrupo(db, "Trabalho");
            grupControl.novoGrupo(db, "RPG");
            grupControl.novoGrupo(db, "Teatro");
        }

    }
}