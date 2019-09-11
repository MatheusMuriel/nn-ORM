package recursos.MVC.controles;

import recursos.Persistencia;

public class Grupo {

    public void novoGrupo(Persistencia db, String descricao) {
        recursos.MVC.modelos.Grupo newGrupo = new recursos.MVC.modelos.Grupo(descricao);
        db.salvarObjeto(newGrupo);
    }
}
