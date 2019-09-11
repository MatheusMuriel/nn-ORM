package recursos.MVC.controles;

import recursos.Persistencia;

public class Telefone {

    public void novoTelefone(Persistencia db, String telefone) {
        recursos.MVC.modelos.Telefone newTelefone = new recursos.MVC.modelos.Telefone(telefone);
        db.salvarObjeto(newTelefone);
    }
}
