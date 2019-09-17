import recursos.Persistencia;
import recursos.Populate;

public class Main{

    public static void main(String... args){
        System.out.println("Hello World!");

        //Populate.populateTabelas();

        Persistencia db = new Persistencia();

        //db.droparTodasAsTabelas();

        //criarTabelas(db);

        //Populate pplt = new Populate();

        //pplt.populateTabelas(db);
        System.out.println();
    }
}