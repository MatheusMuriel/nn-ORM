package recursos.MVC.controles;

import recursos.Persistencia;


public class Contato {

    public void novoContato(Persistencia db, String nome, String sobreNome) {
        recursos.MVC.modelos.Contato newContato = new recursos.MVC.modelos.Contato(nome, sobreNome, "");
    }

    public void novoContato(Persistencia db, String nome, String sobreNome, String email) {
        recursos.MVC.modelos.Contato newContato = new recursos.MVC.modelos.Contato(nome, sobreNome, email);
        db.salvarObjeto(newContato);
    }

}
