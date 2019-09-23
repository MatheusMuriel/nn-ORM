package recursos.MVC.views;

import recursos.Persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ListaContatos {
    private ArrayList<Contato> contatos = new ArrayList<>();
    private Persistencia persistencia;

    public ListaContatos(Persistencia persistencia) {
        this.persistencia = persistencia;
    }

    public void consultaContato(Scanner inp){
        System.out.println("Deseja consultar por:");

        System.out.println("1 - Nome");
        System.out.println("2 - Numero");
        System.out.println("3 - Listar todos");

        boolean valido = false;
        while (!valido){
            System.out.print("Escolha: ");
            String escolha = inp.nextLine();
            switch (escolha){
                case "1":
                    System.out.println("NOOOME");
                    //consultaNome(inp);
                    valido = true;
                    break;
                case "2":
                    System.out.println("Numeeero");
                    valido = true;
                    break;
                case "3":
                    valido = true;
                    //listarTodosContatos();
                    break;
                default:
                    valido = false;
                    System.err.println("Modo de consulta invalido. Por favor escolha outro.");
                    break;
            }
        }
    }

    public void adicionarContato(Scanner input) {
        System.out.println("Você esta adicionando um novo contato");
        String numero = "";
        String nome = "";

        boolean confirmado = false;
        while (!confirmado){
            System.out.print("\nNome: ");
            nome = input.nextLine();

            System.out.print("\nNumero: ");
            numero = input.nextLine();

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

        Contato novoContato = new Contato(nome,Long.parseLong(numero));
        this.contatos.add(novoContato);
        //persistencia.gravarContato(novoContato);
    }

    public void deletarContato(Scanner input) {
    }

    public void alterarContato(Scanner input) {
    }

    private String entradaGeral(Scanner input, String msgFinal, String... mensagens){

        for (String msg : mensagens){
            System.out.println(msg);
        }

        System.out.print(msgFinal);

        return input.nextLine();
    }

    public class Contato {
        String nome;
        long numero;

        public Contato(String nome, long numero) {
            this.nome = nome;
            this.numero = numero;
        }

        // Geters e Seters
        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public long getNumero() {
            return numero;
        }

        public void setNumero(long numero) {
            this.numero = numero;
        }
    }
}

