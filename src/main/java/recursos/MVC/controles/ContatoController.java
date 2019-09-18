package recursos.MVC.controles;

import recursos.MVC.modelos.Grupo;
import recursos.MVC.modelos.Telefone;
import recursos.ORM.Tabela;
import recursos.Persistencia;
import recursos.MVC.modelos.Contato;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ContatoController implements Controller<Contato> {
    private Persistencia db;

    public ContatoController(Persistencia db) {
        this.db = db;
    }

    public ContatoController() {

    }

    public void novoContato(String nome, String sobreNome, String email) {
        Contato newContato = new Contato(nome, sobreNome, email);
        this.adicionar(newContato);
    }

    public void vincularTelefone(Contato contato, Telefone telefone) {
        this.db.salvarRelacao(contato, telefone);
    }

    public void vincularGrupo(Contato contato, Grupo grupo) {
        this.db.salvarRelacao(contato, grupo);
    }

    @Override
    public void adicionar(Contato contato) {
        this.db.salvarObjeto(contato);
    }

    @Override
    public void remover() {

    }

    @Override
    public void atualiza() {

    }

    @Override
    public ArrayList<Contato> procurar(String nome) {
        List<Tabela> tbs = this.db.getTabelaPorClasse(Contato.class);
        ArrayList<Contato> resultado = new ArrayList<>();

        if (tbs.size() == 1) {
            Tabela tb = tbs.get(0);
            ArrayList<Object> linhas = tb.getLinhas()
                    .stream()
                    .filter( linha -> ((Contato) linha).comparaNome(nome))
                    .collect(Collectors
                            .toCollection(ArrayList::new));

            linhas.forEach(o -> resultado.add((Contato) o));

            return resultado;

        } else if(tbs.size() > 1) {
            System.err.println("Aviso em ContatoController::procurar. Foram encontradas mais de uma tabela com o mesmo nome.");
        } else {
            System.err.println("Aviso em ContatoController::procurar. Tabela não encontrada;");
        }

        return resultado;
    }

    public Contato procurarPorId(String id) {
        ArrayList<Contato> todos = this.procurar("");

        return todos.stream()
                .filter(c ->  String.valueOf(c.getId_contato()).equals(id) )
                .collect(Collectors.toList()).get(0);
    }
}
