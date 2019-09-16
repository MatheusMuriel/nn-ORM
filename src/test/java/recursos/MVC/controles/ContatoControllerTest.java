package recursos.MVC.controles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recursos.MVC.modelos.Contato;
import recursos.MVC.modelos.Telefone;
import recursos.Persistencia;
import recursos.Populate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ContatoControllerTest {
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
    void novoContato() {
    }

    @Test
    void novoContato1() {
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
}