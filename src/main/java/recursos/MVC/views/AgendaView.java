package recursos.MVC.views;
import recursos.Persistencia;

import java.util.Scanner;

public class AgendaView {
    private Persistencia db;

    public AgendaView(Persistencia db) {
        this.db = db;
    }

    public void pegaEntrada(){
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println("Escolha uma ação: ");

        System.out.println("1 - Consultar contatos.");
        System.out.println("2 - Adicionar contato.");
        System.out.println("3 - Deletar contato.");
        System.out.println("4 - Alterar contato.");

        System.out.println("5 - Consultar telefone.");
        System.out.println("6 - Adicionar telefone.");
        System.out.println("7 - Deletar telefone.");
        System.out.println("8 - Alterar telefone.");

        System.out.println("9 - Consultar grupo.");
        System.out.println("10 - Adicionar grupo.");
        System.out.println("11 - Deletar grupo.");
        System.out.println("12 - Alterar grupo.");

        boolean entradaValida = false;
        while (!entradaValida){
            System.out.print("Ação: ");
            String entrada = input.nextLine();
            switch (entrada){
                case "1":
                    entradaValida = true;
                    new ContatoView().consultaContato(input);
                    break;
                case "2":
                    entradaValida = true;
                    new ContatoView().adicionarContato(input);
                    break;
                case "3":
                    entradaValida = true;
                    new ContatoView().deletarContato(input);
                    break;
                case "4":
                    entradaValida = true;
                    new ContatoView().alterarContato(input);
                    break;
                default:
                    entradaValida = false;
                    System.err.println("Ação invalida, por favor escolha novamente.");
                    break;
            }
        }
    }
}
