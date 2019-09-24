package recursos.MVC.views;

import recursos.MVC.controles.ContatoController;
import recursos.MVC.modelos.Contato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ContatoView implements GenericView<Contato> {

    @Override
    public void pegarEntrada(Scanner input) {
        System.out.println();
        System.out.println("Escolha uma ação: ");

        System.out.println("1 - Consultar contato.");
        System.out.println("2 - Adicionar contato.");
        System.out.println("3 - Deletar contato.");
        System.out.println("4 - Alterar contato.");
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
        //TODO Fazer metodo Alterar contato
    }

    @Override
    public void deletar(Scanner inp) {

    }

    @Override
    public void adicionar(Scanner input) {
        System.out.println("Você esta adicionando um novo contato");
        System.out.println("Se o campo não se aplicar, basta deixar em branco.");
        String numero = "";
        String nome = "";
        String sobreNnome = "";
        String email = "";

        boolean confirmado = false;
        while (!confirmado){
            System.out.print("\nNome: ");
            nome = input.nextLine();

            System.out.print("\nSobrenome: ");
            sobreNnome = input.nextLine();

            System.out.print("\nNumero: ");
            numero = input.nextLine();

            System.out.print("\nEmail: ");
            email = input.nextLine();

            System.out.println("Você vai adicionar o contato: " + nome + " com o numero: " + numero);

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
        //TODO adicionar telefone e vincular grupo
        new ContatoController().novoContato(nome, sobreNnome, email);
    }

    @Override
    public void consultar(Scanner inp) {

    }

    @Override
    public void listarTodos() {

    }

    public void alterarContato(Scanner input) {
    }

    public void deletarContato(Scanner inp) {
        System.out.println("Qual contato você deseja deletar? ");
        System.out.println("Deseja consultar por:");

        System.out.println("1 - Nome");
        System.out.println("2 - Numero");

        boolean valido = false;
        while (!valido) {
            System.out.print("Escolha: ");
            String escolha = inp.nextLine();
            switch (escolha) {
                case "1":
                    deletarPorNome(inp);
                    valido = true;
                    break;
                case "2":
                    deletarPorNumero(inp);
                    valido = true;
                    break;
                default:
                    valido = false;
                    System.err.println("Modo de consulta invalido. Por favor escolha outro.");
                    break;
            }
        }
    }

    public void deletarPorNome(Scanner inp) {
        System.out.print("\nNome: ");
        String nome = inp.nextLine();

        ArrayList <Contato> result = new ContatoController().procurar(nome);
        if ( result.size() < 1 ) {
            System.out.println("Não foi encontrado nenhum contato com esse numero.");
        } else {
            new ContatoController().remover( result.get(0) );
        }
    }

    public void deletarPorNumero(Scanner inp) {
        System.out.print("\nNumero: ");
        String numero = inp.nextLine();
        ArrayList <Contato> result = new ContatoController().procurarPorNumero(numero);
        if ( result.size() < 1 ) {
            System.out.println("Não foi encontrado nenhum contato com esse numero.");
        } else {
            new ContatoController().remover( result.get(0) );
        }
    }

    public void adicionarContato(Scanner input) {

    }

    public void consultaContato(Scanner inp) {
        System.out.println("Deseja consultar por:");

        System.out.println("1 - Nome");
        System.out.println("2 - Numero");
        System.out.println("3 - Listar todos");

        boolean valido = false;
        while (!valido) {
            System.out.print("Escolha: ");
            String escolha = inp.nextLine();
            switch (escolha) {
                case "1":
                    consultaNome(inp);
                    valido = true;
                    break;
                case "2":
                    consultaNumero(inp);
                    valido = true;
                    break;
                case "3":
                    valido = true;
                    listarTodosContatos();
                    break;
                default:
                    valido = false;
                    System.err.println("Modo de consulta invalido. Por favor escolha outro.");
                    break;
            }
        }
    }

    private void listarTodosContatos() {
        ArrayList<Contato> result = new ContatoController().procurar("");
        printarResultado(result);
    }

    private void consultaNumero(Scanner inp) {
        System.out.println("Qual numero você deseja consultar? ");
        System.out.print("Numero: ");
        String numeroConsulta = inp.nextLine();

        ArrayList <Contato> result = new ContatoController().procurarPorNumero(numeroConsulta);
        printarResultado(result);
    }

    private void consultaNome(Scanner inp) {
        System.out.println("Qual nome você deseja consultar? ");
        System.out.print("Nome: ");
        String nomeConsulta = inp.nextLine();

        ArrayList <Contato> result = new ContatoController().procurar(nomeConsulta);
        printarResultado(result);
    }

    public void printarResultado(ArrayList<Contato> result) {
        if (result.size() < 1) {
            System.out.println("Nenhum contato encontrado :c");
        } else {
            System.out.println(":::::                   :::::");
            result.forEach(contato -> System.out.println(contato.toString()));
            System.out.println(":::::                   :::::");
        }
    }
}