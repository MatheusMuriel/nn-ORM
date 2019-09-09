import ORM.Tabela;
import ORM.Coluna;
import org.sqlite.SQLiteException;
import sun.security.timestamp.TSRequest;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Main{

    public static void main(String... args){
        System.out.println("Hello World!");
        Persistencia db = new Persistencia();

        //populateTables(db);
        adicionarContato(db,"C", "1", "c1g.com");
        adicionarContato(db,"C", "2", "c2g.com");
        adicionarContato(db,"C", "3", "c3g.com");
        adicionarContato(db,"C", "4", "c4g.com");
    }

    public static void populateTables(Persistencia db){
        //Groups
        ArrayList<Coluna> cols_groups = new ArrayList<>();
        cols_groups.add(new Coluna("id_grupos","INTEGER","PRIMARY KEY"));
        cols_groups.add(new Coluna("descricao","",""));
        ArrayList<String> conts_groups = new ArrayList<>();
        Tabela tb_groups = new Tabela("", "grupos", cols_groups, conts_groups);
        db.executar(tb_groups.toString());

        //Contacts
        ArrayList<Coluna> cols_contacts = new ArrayList<>();
        cols_contacts.add(new Coluna("id_contact","INTEGER","PRIMARY KEY"));
        cols_contacts.add(new Coluna("primeiro_nome","","NOT NULL"));
        cols_contacts.add(new Coluna("ultimo_nome","",""));
        cols_contacts.add(new Coluna("email","","UNIQUE"));
        ArrayList<String> conts_contacts = new ArrayList<>();
        conts_contacts.add("");
        Tabela tb_contacts = new Tabela("", "contatos", cols_contacts, conts_contacts);
        db.executar(tb_contacts.toString());

        //Phones
        ArrayList<Coluna> cols_fones = new ArrayList<>();
        cols_fones.add(new Coluna("id_telefone","INTEGER","PRIMARY KEY"));
        cols_fones.add(new Coluna("telefone","","NOT NULL"));
        ArrayList<String> conts_fones = new ArrayList<>();
        conts_fones.add("");
        Tabela tb_telefones = new Tabela("", "telefones", cols_fones, conts_fones);
        db.executar(tb_telefones.toString());

        createRelation(db, tb_contacts,tb_groups,Relacao.N_N);
        createRelation(db, tb_contacts,tb_telefones,Relacao.N_N);
    }
    
    private static void createRelation(Persistencia db, Tabela tb1, Tabela tb2, Relacao rl){

        if (rl.equals(Relacao.N_N)){
            //Tabela relacionamento
            ArrayList<Coluna> conls_rel = new ArrayList<>();

            StringJoiner ref1 = new StringJoiner(" ");
            ref1.add("REFERENCES");
            ref1.add(tb1.getNome());
            conls_rel.add(new Coluna(tb1.getNome(),"", ref1.toString()));

            StringJoiner ref2 = new StringJoiner(" ");
            ref2.add("REFERENCES");
            ref2.add(tb2.getNome());
            conls_rel.add(new Coluna(tb2.getNome(),"", ref2.toString()));

            ArrayList<String> conts_rel = new ArrayList<>();

            Tabela tb_rel = new Tabela("", tb1.getNome().concat("_").concat(tb2.getNome()), conls_rel, conts_rel);

            db.executar(tb_rel.toString());
        }

    }

    /**
     * Metodo que adiciona um contato.
     * Obs: Os numeros são adicionados em uma tabela relação.
     * @param db Objeto de persistencia.
     * @param nome Nome do contato.
     * @param sobreNome Sobre nome do contato.
     * @param email Email do contato.
     *
     * Sintaxe: https://www.sqlite.org/lang_insert.html
     */
    public static void adicionarContato(Persistencia db, String nome, String sobreNome, String email) {

        StringJoiner sj = new StringJoiner(" ");

        sj.add("INSERT");
        sj.add("");

        sj.add("INTO");
        sj.add("contatos");
        sj.add("(");

        //Colunas
        StringJoiner sjColunas = new StringJoiner(",");
        sjColunas.add("primeiro_nome");
        sjColunas.add("ultimo_nome");
        sjColunas.add("email");

        sj.add(sjColunas.toString());
        sj.add(")");

        sj.add("VALUES");
        sj.add("(");

        StringJoiner sjValues = new StringJoiner(",");
        sjValues.add("'" + nome + "'");
        sjValues.add("'" + sobreNome + "'");
        sjValues.add("'" + email + "'");

        sj.add(sjValues.toString());
        sj.add(")");

        String str_insert = sj.toString();

        try{
            db.executar(sj.toString());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    enum Relacao{
        N_N
    }
}