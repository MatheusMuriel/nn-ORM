package recursos.MVC.controles;

import recursos.MVC.modelos.Grupo;
import recursos.Persistencia;

import java.util.ArrayList;

public interface Controller<T> {

    public void adicionar(T tOb);

    public void remover(T tOb);

    public void atualiza(T tOb);

    public ArrayList<T> procurar(String ob); // Sobrescrever o tipo do ArrayList

}

