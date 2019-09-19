package recursos.MVC.controles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recursos.MVC.modelos.Contato;
import recursos.MVC.modelos.Grupo;
import recursos.MVC.modelos.Telefone;
import recursos.Persistencia;
import recursos.Populate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ContatoControllerTest {
    Persistencia db;

    @BeforeEach
    void setUp() {
        //Persistencia.droparTodasAsTabelas();
        //Populate.criarTabelas();
        this.db = new Persistencia();
        //Populate.populateTabelas();
        //this.db = new Persistencia();
    }

    @Test
    void novoContato() {
        Populate.trucateContatos();

        // Re instancia a percistencia porcausa do truncamento dos dados
        this.db = new Persistencia();
        ContatoController cCtrl = new ContatoController(db);
        assert ( cCtrl.procurar("").size() == 0 );

        Contato c1 = new Contato("Fernando", "Pessoa", "jose.silva@gmail.com");
        Contato c2 = new Contato("Silvio", "Antonelo", "maria_antonia@gmail.com");
        Contato c3 = new Contato("Alfajor", "Chocq", "robert-souza@gmail.com");

        cCtrl.novoContato("Fernando", "Pessoa", "jose.silva@gmail.com");
        ArrayList<Contato> rConslt1 = cCtrl.procurar("Fernando Pessoa");
        assert ( rConslt1.stream().anyMatch(ct -> ct.toString().equals(c1.toString())) );

        cCtrl.novoContato("Silvio", "Antonelo", "maria_antonia@gmail.com");
        ArrayList<Contato> rConslt2 = cCtrl.procurar("Silvio Antonelo");
        assert ( rConslt2.stream().anyMatch(ct -> ct.toString().equals(c2.toString())) );

        cCtrl.novoContato("Alfajor", "Chocq", "robert-souza@gmail.com");
        ArrayList<Contato> rConslt3 = cCtrl.procurar("Alfajor Chocq");
        assert ( rConslt3.stream().anyMatch(ct -> ct.toString().equals(c3.toString())) );

    }

    @Test
    void adicionar() {
        Populate.trucateContatos();

        // Re instancia a percistencia porcausa do truncamento dos dados
        this.db = new Persistencia();
        ContatoController cCtrl = new ContatoController(db);
        assert ( cCtrl.procurar("").size() == 0 );

        Contato c1 = new Contato("Fernando", "Pessoa", "jose.silva@gmail.com");
        Contato c2 = new Contato("Silvio", "Antonelo", "maria_antonia@gmail.com");
        Contato c3 = new Contato("Alfajor", "Chocq", "robert-souza@gmail.com");

        cCtrl.adicionar(c1);
        ArrayList<Contato> rConslt1 = cCtrl.procurar("Fernando Pessoa");
        assert ( rConslt1.stream().anyMatch(ct -> ct.toString().equals(c1.toString())) );

        cCtrl.adicionar(c2);
        ArrayList<Contato> rConslt2 = cCtrl.procurar("Silvio Antonelo");
        assert ( rConslt2.stream().anyMatch(ct -> ct.toString().equals(c2.toString())) );

        cCtrl.adicionar(c3);
        ArrayList<Contato> rConslt3 = cCtrl.procurar("Alfajor Chocq");
        assert ( rConslt3.stream().anyMatch(ct -> ct.toString().equals(c3.toString())) );
    }

    @Test
    void remover() {
        Persistencia.droparTodasAsTabelas();
        Populate.criarTabelas();
        this.db = new Persistencia();

        ContatoController cCtrl = new ContatoController(db);
        TelefoneController tCtrl = new TelefoneController(db);
        GrupoController gCtrl = new GrupoController(db);



        // ------ Teste 1 ------
        Contato     c1 = new Contato("Fernando", "Pessoa", "jose.silva@gmail.com");
        Telefone    t1 = new Telefone("99854-1356");
        Grupo       g1 = new Grupo("Balada");

        cCtrl.adicionar(c1);
        tCtrl.adicionar(t1);
        gCtrl.adicionar(g1);

        cCtrl.vincularTelefone  (c1, t1);
        cCtrl.vincularGrupo     (c1, g1);

        cCtrl.remover(c1);


        ArrayList<Contato> rConslt1 = cCtrl.procurar(c1.getPrimeiro_nome());
        assert ( rConslt1.stream()
                .noneMatch(ct -> ct.toString().equals(c1.toString())) );

        tCtrl = new TelefoneController(new Persistencia());
        ArrayList<Telefone> rConsltTelefone1 = tCtrl.procurar(t1.getTelefone());
        assert ( rConsltTelefone1.stream()
                .anyMatch(tl -> tl.getContatos().stream()
                        .noneMatch(c -> c.toString().equals(c1.toString())) ) );

        ArrayList<Grupo> rConsltGrupo1 = gCtrl.procurar(g1.getDescricao_grupo());
        assert ( rConsltGrupo1.stream()
                .anyMatch(gp -> gp.getContatos().stream()
                        .noneMatch(c -> c.toString().equals(c1.toString())) ) );





        // ------ Teste 2 ------
        Contato     c2 = new Contato("Silvio", "Antonelo", "maria_antonia@gmail.com");
        Telefone    t2 = new Telefone("93265-4120");
        Grupo       g2 = new Grupo("Uno");

        cCtrl.adicionar(c2);
        tCtrl.adicionar(t2);
        gCtrl.adicionar(g2);

        cCtrl.vincularTelefone  (c2, t2);
        cCtrl.vincularGrupo     (c2, g2);

        cCtrl = new ContatoController(new Persistencia());
        cCtrl.remover(c2);

        this.db = new Persistencia();

        ArrayList<Contato> rConslt2 = cCtrl.procurar(c2.getPrimeiro_nome());
        assert ( rConslt2.stream()
                .noneMatch(ct -> ct.toString().equals(c2.toString())) );

        ArrayList<Telefone> rConsltTelefone2 = tCtrl.procurar(t2.getTelefone());
        assert ( rConsltTelefone2.stream()
                .anyMatch(tl -> tl.getContatos().stream()
                        .noneMatch(c -> c.toString().equals(c2.toString())) ) );

        ArrayList<Grupo> rConsltGrupo2 = gCtrl.procurar(g2.getDescricao_grupo());
        assert ( rConsltGrupo2.stream()
                .anyMatch(gp -> gp.getContatos().stream()
                        .noneMatch(c -> c.toString().equals(c2.toString())) ) );


        // ------ Teste 3 ------
        Contato c3 = new Contato("Alfajor", "Chocq", "robert-souza@gmail.com");
        Telefone t3 = new Telefone("75841-6587");
        Grupo g3 = new Grupo("Uno");

        this.db = new Persistencia();

        cCtrl.adicionar(c3);
        tCtrl.adicionar(t3);
        gCtrl.adicionar(g3);

        this.db = new Persistencia();

        cCtrl.vincularTelefone  (c3, t3);
        cCtrl.vincularGrupo     (c3, g3);


        this.db = new Persistencia();

        cCtrl = new ContatoController(new Persistencia());
        cCtrl.remover(c3);

        this.db = new Persistencia();

        ArrayList<Contato> rConslt3 = cCtrl.procurar(c3.getPrimeiro_nome());
        assert ( rConslt3.stream()
                .noneMatch(ct -> ct.toString().equals(c3.toString())) );

        ArrayList<Telefone> rConsltTelefone3 = tCtrl.procurar(t3.getTelefone());
        assert ( rConsltTelefone3.stream()
                .anyMatch(tl -> tl.getContatos().stream()
                        .noneMatch(c -> c.toString().equals(c3.toString())) ) );

        ArrayList<Grupo> rConsltGrupo3 = gCtrl.procurar(g3.getDescricao_grupo());
        assert ( rConsltGrupo3.stream()
                .anyMatch(gp -> gp.getContatos().stream()
                        .noneMatch(c -> c.toString().equals(c3.toString())) ) );
    }

    @Test
    void atualiza() {
        // TODO Fazer teste
    }

    @Test
    void procurar() {
        ContatoController cCtrl = new ContatoController(this.db);

        Contato c1 = new Contato("Maria", "Antonia", "maria_antonia@gmail.com");
        String consultaMaria1 = "Maria";
        String consultaMaria2 = "maria";
        String consultaMaria3 = "maría";
        String consultaMaria4 = "mãri";

        ArrayList<Contato> rMaria1 = cCtrl.procurar(consultaMaria1);
        assert ( rMaria1.stream().anyMatch(ct -> ct.toString().equals(c1.toString())) );

        ArrayList<Contato> rMaria2 = cCtrl.procurar(consultaMaria2);
        assert ( rMaria2.stream().anyMatch(ct -> ct.toString().equals(c1.toString())) );

        ArrayList<Contato> rMaria3 = cCtrl.procurar(consultaMaria3);
        assert ( rMaria3.stream().anyMatch(ct -> ct.toString().equals(c1.toString())) );

        ArrayList<Contato> rMaria4 = cCtrl.procurar(consultaMaria4);
        assert ( rMaria4.stream().anyMatch(ct -> ct.toString().equals(c1.toString())) );


        Contato c2 = new Contato("Roberto", "Souza", "robert-souza@gmail.com");
        String consultaRoberto1 = "Souza";
        String consultaRoberto2 = "Roberto";
        String consultaRoberto3 = "souZa";
        String consultaRoberto4 = "rober";

        ArrayList<Contato> rRoberto1 = cCtrl.procurar(consultaRoberto1);
        assert ( rRoberto1.stream().anyMatch(ct -> ct.toString().equals(c2.toString())) );

        ArrayList<Contato> rRoberto2 = cCtrl.procurar(consultaRoberto2);
        assert ( rRoberto2.stream().anyMatch(ct -> ct.toString().equals(c2.toString())) );

        ArrayList<Contato> rRoberto3 = cCtrl.procurar(consultaRoberto3);
        assert ( rRoberto3.stream().anyMatch(ct -> ct.toString().equals(c2.toString())) );

        ArrayList<Contato> rRoberto4 = cCtrl.procurar(consultaRoberto4);
        assert ( rRoberto4.stream().anyMatch(ct -> ct.toString().equals(c2.toString())) );
    }

    @Test
    void vincularTelefone() {
        Populate.trucateContatos();
        this.db = new Persistencia();
        Populate.populateContatos(db);

        this.db = new Persistencia();

        ContatoController cCtrl = new ContatoController(db);
        TelefoneController tCtrl = new TelefoneController(db);

        Contato c1 = cCtrl.procurar("Jose").get(0);
        Contato c2 = cCtrl.procurar("Maria").get(0);

        Telefone t1 = tCtrl.procurar("9999").get(0);
        Telefone t2 = tCtrl.procurar("55555").get(0);

        cCtrl.vincularTelefone(c1, t1);
        cCtrl.vincularTelefone(c2, t2);

        ArrayList<Contato> rConslt1 = cCtrl.procurar("Jose");
        assert ( rConslt1.stream()
                    .anyMatch(ct -> ct.getTelefones().stream()
                                        .anyMatch(t -> t.toString().equals(t1.toString())) ) );

        ArrayList<Contato> rConslt2 = cCtrl.procurar("Maria");
        assert ( rConslt2.stream()
                .anyMatch(ct -> ct.getTelefones().stream()
                        .anyMatch(t -> t.toString().equals(t2.toString())) ) );
    }

    @Test
    void vincularGrupo() {
        Populate.trucateContatos();
        this.db = new Persistencia();
        Populate.populateContatos(db);

        this.db = new Persistencia();

        ContatoController cCtrl = new ContatoController(db);
        GrupoController gCtrl = new GrupoController(db);

        Contato c1 = cCtrl.procurar("Jose").get(0);
        Contato c2 = cCtrl.procurar("Maria").get(0);

        Grupo g1 = gCtrl.procurar("Familia").get(0);
        Grupo g2 = gCtrl.procurar("Trabalho").get(0);

        cCtrl.vincularGrupo(c1, g1);
        cCtrl.vincularGrupo(c2, g2);

        ArrayList<Contato> rConslt1 = cCtrl.procurar("Jose");
        assert ( rConslt1.stream()
                .anyMatch(ct -> ct.getGrupos().stream()
                        .anyMatch(t -> t.toString().equals(g1.toString())) ) );

        ArrayList<Contato> rConslt2 = cCtrl.procurar("Maria");
        assert ( rConslt2.stream()
                .anyMatch(ct -> ct.getGrupos().stream()
                        .anyMatch(t -> t.toString().equals(g2.toString())) ) );
    }
}