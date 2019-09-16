package recursos.MVC.controles;

import recursos.MVC.modelos.Grupo;
import recursos.Persistencia;

import java.util.ArrayList;

public interface Controller<T> {

    public void adicionar(T tOb);

    public void remover();

    public void atualiza();

    public ArrayList<T> procurar(String ob); // Sobrescrever o tipo do ArrayList

}

