package recursos.MVC.controles;

import recursos.MVC.modelos.Contato;
import recursos.ORM.Tabela;
import recursos.Persistencia;
import recursos.MVC.modelos.Telefone;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelefoneController implements Controller<Telefone> {
    private Persistencia db;

    public TelefoneController(Persistencia db) {
        this.db = db;
    }

    public TelefoneController() {

    }

    public void novoTelefone(String telefone) {
        Telefone newTelefone = new Telefone(telefone);
        this.adicionar(newTelefone);
    }

    public void vincularContato(Contato contato) {

    }

    @Override
    public void adicionar(Telefone telefone) {
        this.db.salvarObjeto(telefone);
    }

    @Override
    public void remover() {

    }

    @Override
    public void atualiza() {

    }

    @Override
    public ArrayList<Telefone> procurar(String telefone) {
        List<Tabela> tbs = this.db.getTabelaPorClasse(Telefone.class);
        ArrayList<Telefone> resultado = new ArrayList<>();

        if (tbs.size() == 1) {
            Tabela tb = tbs.get(0);
            ArrayList<Object> linhas = tb.getLinhas()
                    .stream()
                    .filter( linha -> ( (Telefone) linha).comparaTelefone(telefone) )
                    .collect(Collectors
                            .toCollection(ArrayList::new));

            linhas.forEach(o -> resultado.add((Telefone) o));

            return resultado;

        } else if(tbs.size() > 1) {
            System.err.println("Foram encontradas mais de uma tabela com o mesmo nome.");
        } else {
            System.err.println("Tabela não encontrada;");
        }

        return resultado;
    }

    public Telefone procurarPorId(String id) {
        ArrayList<Telefone> todos = this.procurar("");

        return todos.stream()
                .filter(c ->  String.valueOf(c.getId_telefone()).equals(id) )
                .collect(Collectors.toList()).get(0);
    }
}
