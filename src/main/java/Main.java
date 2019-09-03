import ORM.Tabela;
import ORM.Coluna;

import java.util.ArrayList;

public class Main{

    public static void main(String... args){
        System.out.println("Hello World!");
        Persistencia db = new Persistencia();

        ArrayList<Coluna> cols = new ArrayList<>();
        cols.add(new Coluna("Teste","Varchar(25)","NOT NULL"));
        cols.add(new Coluna("Batata","Varchar(25)",""));
        cols.add(new Coluna("Abacaxi","Varchar(25)","UNIQUE"));

        ArrayList<String> conts = new ArrayList<>();

        Tabela tb = new Tabela("","teste",cols,conts);
        System.out.println(tb.toString());
        db.executar(tb.toString());
    }
}