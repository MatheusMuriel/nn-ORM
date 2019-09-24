package recursos.MVC.views;

import recursos.MVC.controles.TelefoneController;
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
        //TODO
    }

    @Override
    public void adicionar(Scanner input) {
        System.out.println("Você esta adicionando um novo telefone.");
        String telefone = "";

        boolean confirmado = false;
        while (!confirmado){
            System.out.print("\nTelefone: ");
            telefone = input.nextLine();

            System.out.println("Você vai adicionar o telefone: " + telefone);

            boolean confirmacaoValida = false;
            while (!confirmacaoValida){
                System.out.print("Confirmar? (S/N) ");
                String confirmacao = input.nextLine();
                switch (confirmacao.toUpperCase()){
                    case "S":
                        confirmado = true;
                        confirmacaoValida = true;
                        break;
                    case "N":
                        confirmado = false;
                        confirmacaoValida = true;
                        break;
                    default:
                        confirmacaoValida = false;
                        System.err.println("Confirmação invalida. Por favor digite novamente.");
                        break;
                }
            }
        }
        //TODO adicionar contatos ao criar
        new TelefoneController().novoTelefone(telefone);
    }

    @Override
    public void consultar(Scanner inp) {
        System.out.println("Deseja consultar por:");

        System.out.println("1 - Numero.");
        System.out.println("2 - Contato.");
        System.out.println("3 - Grupo.");
        System.out.println("4 - Listar Todos.");
        System.out.println("0 - Voltar.");

        boolean valido = false;
        while (!valido) {
            System.out.print("Escolha: ");
            String escolha = inp.nextLine();
            switch (escolha) {
                case "1":
                    valido = true;
                    consultarPorNumero(inp);
                    break;
                case "2":
                    valido = true;
                    consultarPorContato(inp);
                    break;
                case "3":
                    valido = true;
                    consultarPorGrupo(inp);
                    break;
                case "4":
                    valido = true;
                    listarTodos();
                    break;
                case "0":
                    valido = true;
                    break;
                default:
                    valido = false;
                    System.err.println("Modo de consulta invalido. Por favor escolha outro.");
                    break;
            }
        }
    }

    @Override
    public void listarTodos() {
        ArrayList <Telefone> result = new TelefoneController().procurar("");
        printarResultado(result);
    }

    @Override
    public void printarResultado(ArrayList<Telefone> result) {
        if (result.size() < 1) {
            System.out.println("Nenhum telefone encontrado :c");
        } else {
            System.out.println(":::::                   :::::");
            result.forEach(telefone -> System.out.println(telefone.toString()));
            System.out.println(":::::                   :::::");
        }
    }

    private void consultarPorNumero(Scanner inp) {
        System.out.println("Qual numero você deseja consultar? ");
        System.out.print("Numero: ");
        String num = inp.nextLine();

        ArrayList <Telefone> result = new TelefoneController().procurar(num);
        printarResultado(result);
    }

    private void consultarPorGrupo(Scanner inp) {
        //TODO
    }

    private void consultarPorContato(Scanner inp) {
        //TODO
    }
}
