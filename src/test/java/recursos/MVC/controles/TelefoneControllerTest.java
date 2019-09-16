package recursos.MVC.controles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recursos.MVC.modelos.Telefone;
import recursos.Persistencia;
import recursos.Populate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TelefoneControllerTest {
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
    void novoTelefone() {
    }

    @Test
    void adicionar() {
        Populate.trucateTelefones();

        // Re instancia a percistencia porcausa do truncamento dos dados
        this.db = new Persistencia();
        TelefoneController tCtrl = new TelefoneController(db);
        assert ( tCtrl.procurar("").size() == 0 );
    }

    @Test
    void remover() {
    }

    @Test
    void atualiza() {
    }

    @Test
    void procurar() {
        TelefoneController tCtrl = new TelefoneController(this.db);

        String tlf1 = "999-999";
        String tlf2 = "999-";
        String tlf3 = "+9-99-999";

        Telefone tlResult = new Telefone("99999-9999");

        ArrayList<Telefone> r1 = tCtrl.procurar(tlf1);
        ArrayList<Telefone> r2 = tCtrl.procurar(tlf2);
        ArrayList<Telefone> r3 = tCtrl.procurar(tlf3);
        assert (r1.get(0).getTelefone().equals(tlResult.getTelefone()));
        assert (r2.get(0).getTelefone().equals(tlResult.getTelefone()));
        assert (r3.get(0).getTelefone().equals(tlResult.getTelefone()));


        // ------- Verificas entradas erradas e resultados vazios -------

        String tlf4 = "dasdasdadsa";
        String tlf5 = "999999999999999999999999999999999999999";

        ArrayList<Telefone> r4 = tCtrl.procurar(tlf4);
        ArrayList<Telefone> r5 = tCtrl.procurar(tlf5);

        assert (r4.size() == 0);
        assert (r5.size() == 0);

    }
}