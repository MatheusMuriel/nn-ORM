package recursos.MVC.views;

import recursos.MVC.controles.GrupoController;
import recursos.MVC.modelos.Grupo;

import java.util.ArrayList;
import java.util.Scanner;

public class GrupoView implements GenericView<Grupo>{

    @Override
    public void pegarEntrada(Scanner input) {
        System.out.println();
        System.out.println("Escolha uma ação: ");

        System.out.println("1 - Consultar grupo.");
        System.out.println("2 - Adicionar grupo.");
        System.out.println("3 - Deletar grupo.");
        System.out.println("4 - Alterar grupo.");
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
        //TODO Fazer metodo Alterar grupo
    }

    @Override
    public void deletar(Scanner inp) {
        deletarPorDescricao(inp);
    }

    @Override
    public void adicionar(Scanner input) {
        System.out.println("Você esta adicionando um novo grupo.");
        String descricao = "";

        boolean confirmado = false;
        while (!confirmado){
            System.out.print("\nDescrição: ");
            descricao = input.nextLine();

            System.out.println("Você vai adicionar o grupo: " + descricao);

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
        new GrupoController().novoGrupo(descricao);
    }

    @Override
    public void consultar(Scanner inp) {
        System.out.println("Deseja consultar por:");

        System.out.println("1 - Descrição.");
        System.out.println("2 - Contato.");
        System.out.println("3 - Telefone.");
        System.out.println("4 - Listar Todos.");
        System.out.println("0 - Voltar.");

        boolean valido = false;
        while (!valido) {
            System.out.print("Escolha: ");
            String escolha = inp.nextLine();
            switch (escolha) {
                case "1":
                    valido = true;
                    consultarPorDescricao(inp);
                    break;
                case "2":
                    valido = true;
                    consultarPorContato(inp);
                    break;
                case "3":
                    valido = true;
                    consultarPorTelefone(inp);
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
        ArrayList <Grupo> result = new GrupoController().procurar("");
        printarResultado(result);
    }

    @Override
    public void printarResultado(ArrayList<Grupo> result) {
        if (result.size() < 1) {
            System.out.println("Nenhum grupo encontrado :c");
        } else {
            System.out.println(":::::                   :::::");
            result.forEach(grupo -> System.out.println(grupo.toString()));
            System.out.println(":::::                   :::::");
        }
    }

    private void consultarPorDescricao(Scanner inp) {
        System.out.println("Qual descrição você deseja consultar? ");
        System.out.print("Descrição: ");
        String desc = inp.nextLine();

        ArrayList <Grupo> result = new GrupoController().procurar(desc);
        printarResultado(result);
    }

    private void consultarPorTelefone(Scanner inp) {
        System.out.println("Qual numero você deseja consultar? ");
        System.out.print("Numero: ");
        String num = inp.nextLine();

        ArrayList <Grupo> result = new GrupoController().procurarPorTelefone(num);
        printarResultado(result);
    }

    private void consultarPorContato(Scanner inp) {
        System.out.println("Qual contato você deseja consultar? ");
        System.out.print("Nome: ");
        String nome = inp.nextLine();

        ArrayList <Grupo> result = new GrupoController().procurarPorContato(nome);
        printarResultado(result);
    }

    private void deletarPorDescricao(Scanner inp) {
        System.out.print("\nDescrição: ");
        String desc = inp.nextLine();

        ArrayList <Grupo> result = new GrupoController().procurar(desc);
        if ( result.size() < 1 ) {
            System.out.println("Não foi encontrado nenhum grupo com essa descrição.");
        } else {
            System.out.println("Deseja deletar o grupo: ");
            System.out.println(result.get(0).toString());
            System.out.println("S/N");
            System.out.print("\n> ");
            String escolha = inp.nextLine();

            boolean escolhaValida = false;
            while (!escolhaValida) {
                switch (escolha.toUpperCase()) {
                    case "S":
                        escolhaValida = true;
                        new GrupoController().remover(result.get(0));
                        break;
                    case "N":
                        escolhaValida = true;
                        System.out.println("Cancelado.");
                        break;
                    default:
                        escolhaValida = false;
                        break;
                }
            }
        }
    }

}
