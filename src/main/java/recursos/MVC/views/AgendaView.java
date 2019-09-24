package recursos.MVC.views;

import java.util.Scanner;

public class AgendaView {

    public AgendaView() {
    }

    public void pegaEntrada(){
        Scanner input = new Scanner(System.in);
        boolean isFim = false;

        while (!isFim) {
            System.out.println();
            System.out.println("Escolha uma categoria: ");

            System.out.println("1 - Contatos.");
            System.out.println("2 - Telefones.");
            System.out.println("3 - Grupos.");
            System.out.println("9 - Sair.");

            boolean entradaValida = false;
            while (!entradaValida){
                System.out.print("Ação: ");
                String entrada = input.nextLine();
                switch (entrada){
                    case "1":
                        entradaValida = true;
                        new ContatoView().pegarEntrada(input);
                        break;
                    case "2":
                        entradaValida = true;
                        new TelefoneView().pegarEntrada(input);
                        break;
                    case "3":
                        entradaValida = true;
                        new GrupoView().pegarEntrada(input);
                        break;
                    case "9":
                        entradaValida = true;
                        isFim = true;
                        break;
                    default:
                        entradaValida = false;
                        System.err.println("Ação invalida, por favor escolha novamente.");
                        break;
                }
            }
        }
    }
}
