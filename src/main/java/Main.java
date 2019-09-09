import ORM.Tabela;
import ORM.Coluna;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
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

        adicionaNumero(db, "99999-9999");
        adicionaNumero(db, "88888-8888");
        adicionaNumero(db, "77777-7777");
        adicionaNumero(db, "66666-6666");
        adicionaNumero(db, "55555-5555");
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

    enum Relacao{
        N_N
    }
}