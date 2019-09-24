import recursos.MVC.views.AgendaView;
import recursos.Persistencia;
import recursos.Populate;

public class Main{

    public static void main(String... args){
        System.out.println("Bem vindo a sua Agenda Eletronica!");

        //Persistencia.droparTodasAsTabelas();
        //Populate.criarTabelas();
        //Populate.populateTabelas();
        new Persistencia();
        new AgendaView().pegaEntrada();

        System.out.println("Volte sempre! :D");
    }
}