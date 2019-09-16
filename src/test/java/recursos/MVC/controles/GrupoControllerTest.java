package recursos.MVC.controles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recursos.Persistencia;
import recursos.Populate;

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

    }
}