import recursos.Persistencia;
import recursos.Populate;

public class Main{

    public static void main(String... args){
        System.out.println("Hello World!");

        Persistencia.droparTodasAsTabelas();
        Populate.criarTabelas();
        Populate.populateTabelas();

        Persistencia db = new Persistencia();

        System.out.println();
    }
}