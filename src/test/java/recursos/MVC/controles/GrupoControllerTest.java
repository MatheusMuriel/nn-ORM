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