package muriel.MVC.controles;

import muriel.Persistencia;


public class Contato {

    public void novoContato(Persistencia db, String nome, String sobreNome) {
        muriel.MVC.modelos.Contato newContato = new muriel.MVC.modelos.Contato(nome, sobreNome, "");
    }

    public void novoContato(Persistencia db, String nome, String sobreNome, String email) {
        muriel.MVC.modelos.Contato newContato = new muriel.MVC.modelos.Contato(nome, sobreNome, email);
        db.salvarObjeto(newContato);
    }

}
