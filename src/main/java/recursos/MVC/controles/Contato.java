package recursos.MVC.controles;

import recursos.ORM.Tabela;
import recursos.Persistencia;

import java.util.List;


public class Contato {

    public void novoContato(Persistencia db, String nome, String sobreNome) {
        novoContato(db, nome, sobreNome, "");
    }

    public void novoContato(Persistencia db, String nome, String sobreNome, String email) {
        recursos.MVC.modelos.Contato newContato = new recursos.MVC.modelos.Contato(nome, sobreNome, email);
        db.salvarObjeto(newContato);
    }

    public void procurar(Persistencia db, String nome) {
        List<Tabela> tbs = db.getTabelaPorClasse(recursos.MVC.modelos.Contato.class);

    }

}
