package recursos.MVC.views;

import java.util.ArrayList;
import java.util.Scanner;

public interface GenericView<T> {

    public void pegarEntrada(Scanner input);

    public void alterar(Scanner input);

    public void deletar(Scanner input);

    public void adicionar(Scanner input);

    public void consultar(Scanner input);

    void listarTodos();

    void printarResultado(ArrayList<T> result);

}
