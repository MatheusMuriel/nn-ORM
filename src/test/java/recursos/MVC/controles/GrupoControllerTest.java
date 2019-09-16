package recursos.MVC.controles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recursos.MVC.modelos.Grupo;
import recursos.Persistencia;
import recursos.Populate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GrupoControllerTest {
    Persistencia db;

    @BeforeEach
    void setUp() {
        Persistencia.droparTodasAsTabelas();
        Populate.criarTabelas();
        this.db = new Persistencia();
        Populate.populateTabelas();
        this.db = new Persistencia();
    }

    @Test
    void novoGrupo() {
    }

    @Test
    void adicionar() {
        Populate.trucateGrupos();

        // Re instancia a percistencia porcausa do truncamento dos dados
        this.db = new Persistencia();
        GrupoController gCtrl = new GrupoController(db);
        assert ( gCtrl.procurar("").size() == 0 );

        Grupo grupo1 = new Grupo("Familia");
        Grupo grupo2 = new Grupo("Escola");
        Grupo grupo3 = new Grupo("Trabalho");
        Grupo grupo4 = new Grupo("RPG");

        gCtrl.adicionar(grupo1);
        assert ( gCtrl.procurar(grupo1.getDescricao_grupo())
                    .stream()
                    .anyMatch(gp -> gp.toString()
                            .equals(grupo1.toString())) );

        gCtrl.adicionar(grupo2);
        assert ( gCtrl.procurar(grupo2.getDescricao_grupo())
                .stream()
                .anyMatch(gp -> gp.toString()
                        .equals(grupo2.toString())) );

        gCtrl.adicionar(grupo3);
        assert ( gCtrl.procurar(grupo3.getDescricao_grupo())
                .stream()
                .anyMatch(gp -> gp.toString()
                        .equals(grupo3.toString())) );

        gCtrl.adicionar(grupo4);
        assert ( gCtrl.procurar(grupo4.getDescricao_grupo())
                .stream()
                .anyMatch(gp -> gp.toString()
                        .equals(grupo4.toString())) );

    }

    @Test
    void remover() {
    }

    @Test
    void atualiza() {
    }

    @Test
    void procurar() {
        GrupoController gCtrl = new GrupoController(this.db);

        Grupo g1 = new Grupo("Familia");
        String consultaFamilia1 = "Familia";
        String consultaFamilia2 = "familia";
        String consultaFamilia3 = "famílìa";
        String consultaFamilia4 = "fãmi";

        ArrayList<Grupo> rFamilia1 = gCtrl.procurar(consultaFamilia1);
        assert ( rFamilia1.stream().anyMatch(gp -> gp.toString().equals(g1.toString())) );

        ArrayList<Grupo> rFamilia2 = gCtrl.procurar(consultaFamilia2);
        assert ( rFamilia2.stream().anyMatch(gp -> gp.toString().equals(g1.toString())) );

        ArrayList<Grupo> rFamilia3 = gCtrl.procurar(consultaFamilia3);
        assert ( rFamilia3.stream().anyMatch(gp -> gp.toString().equals(g1.toString())) );

        ArrayList<Grupo> rFamilia4 = gCtrl.procurar(consultaFamilia4);
        assert ( rFamilia4.stream().anyMatch(gp -> gp.toString().equals(g1.toString())) );



        Grupo g2 = new Grupo("Trabalho");
        String consultaTrabalho1 = "Trabalho";
        String consultaTrabalho2 = "trabalho";
        String consultaTrabalho3 = "tràbálho";
        String consultaTrabalho4 = "trãb";

        ArrayList<Grupo> rTrabalho1 = gCtrl.procurar(consultaTrabalho1);
        assert ( rTrabalho1.stream().anyMatch(gp -> gp.toString().equals(g2.toString())) );

        ArrayList<Grupo> rTrabalho2 = gCtrl.procurar(consultaTrabalho2);
        assert ( rTrabalho2.stream().anyMatch(gp -> gp.toString().equals(g2.toString())) );

        ArrayList<Grupo> rTrabalho3 = gCtrl.procurar(consultaTrabalho3);
        assert ( rTrabalho3.stream().anyMatch(gp -> gp.toString().equals(g2.toString())) );

        ArrayList<Grupo> rTrabalho4 = gCtrl.procurar(consultaTrabalho4);
        assert ( rTrabalho4.stream().anyMatch(gp -> gp.toString().equals(g2.toString())) );
    }
}