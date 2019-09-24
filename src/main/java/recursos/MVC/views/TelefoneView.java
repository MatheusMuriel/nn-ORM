package recursos.MVC.views;

import recursos.MVC.modelos.Telefone;

import java.util.ArrayList;
import java.util.Scanner;

public class TelefoneView implements GenericView<Telefone>{

    @Override
    public void pegarEntrada(Scanner input) {
        System.out.println();
        System.out.println("Escolha uma ação: ");

        System.out.println("1 - Consultar telefone.");
        System.out.println("2 - Adicionar telefone.");
        System.out.println("3 - Deletar telefone.");
        System.out.println("4 - Alterar telefone.");
        System.out.println("0 - Voltar.");

        boolean entradaValida = false;
        while (!entradaValida){
            System.out.print("Ação: ");
            String entrada = input.nextLine();
            switch (entrada){
                case "1":
                    entradaValida = true;
                    consultar(input);
                    break;
                case "2":
                    entradaValida = true;
                    adicionar(input);
                    break;
                case "3":
                    entradaValida = true;
                    new ContatoView().deletar(input);
                    break;
                case "4":
                    entradaValida = true;
                    alterar(input);
                    break;
                case "0":
                    entradaValida = true;
                    break;
                default:
                    entradaValida = false;
                    System.err.println("Ação invalida, por favor escolha novamente.");
                    break;
            }
        }
    }

    @Override
    public void alterar(Scanner input) {
        //TODO Fazer metodo Alterar telefone
    }

    @Override
    public void deletar(Scanner inp) {

    }

    @Override
    public void adicionar(Scanner input) {

    }

    @Override
    public void consultar(Scanner inp) {

    }

    @Override
    public void listarTodos() {

    }

    @Override
    public void printarResultado(ArrayList<Telefone> result) {

    }
}
