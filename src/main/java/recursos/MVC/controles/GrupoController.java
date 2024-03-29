package recursos.MVC.controles;

import recursos.MVC.modelos.Contato;
import recursos.ORM.Tabela;
import recursos.Persistencia;
import recursos.MVC.modelos.Grupo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrupoController implements Controller<Grupo> {
    private Persistencia db;

    public GrupoController(Persistencia db) {
        this.db = db;
    }

    public GrupoController() {
    }

    public void novoGrupo(String descricao) {
        Grupo newGrupo = new Grupo(descricao);
        this.adicionar(newGrupo);
    }

    public void vincularContato(Contato contato) {

    }

    @Override
    public void adicionar(Grupo grupo) {
        this.db.salvarObjeto(grupo);
    }

    @Override
    public void remover(Grupo grupo) {
        this.db.removerObjeto(grupo);
    }

    @Override
    public void atualiza(Grupo grupo) {

    }

    @Override
    public ArrayList<Grupo> procurar(String descricao) {
        this.db = new Persistencia();
        List<Tabela> tbs = this.db.getTabelaPorClasse(Grupo.class);
        ArrayList<Grupo> resultado = new ArrayList<>();

        if (tbs.size() == 1) {
            Tabela tb = tbs.get(0);
            ArrayList<Object> linhas = tb.getLinhas()
                    .stream()
                    .filter( linha -> ( (Grupo) linha).comparaGrupo(descricao) )
                    .collect(Collectors
                            .toCollection(ArrayList::new));

            linhas.forEach(o -> resultado.add((Grupo) o));

            return resultado;

        } else if(tbs.size() > 1) {
            System.err.println("Aviso em GrupoController::procurar. Foram encontradas mais de uma tabela com o mesmo nome.");
        } else {
            System.err.println("Aviso em GrupoController::procurar. Tabela não encontrada;");
        }

        return resultado;
    }

    public Grupo procurarPorId(String id) {
        ArrayList<Grupo> todos = this.procurar("");

        return todos.stream()
                .filter(c ->  String.valueOf(c.getId_grupo()).equals(id) )
                .collect(Collectors.toList()).get(0);
    }

    public ArrayList<Grupo> procurarPorTelefone(String numero) {
        ArrayList<Grupo> todos = this.procurar("");
        List<Grupo> result = todos.stream()
                .filter(grupo -> grupo.getContatos().stream()
                        .anyMatch(contato -> contato.getTelefones().stream()
                                    .anyMatch(telefone -> telefone.comparaTelefone(numero))))
                .collect(Collectors.toList());

        return new ArrayList<>(result);
    }

    public ArrayList<Grupo> procurarPorContato(String nome) {
        ArrayList<Grupo> todos = this.procurar("");
        List<Grupo> result = todos.stream()
                .filter(grupo -> grupo.getContatos().stream()
                        .anyMatch(contato -> contato.comparaNome(nome)))
                .collect(Collectors.toList());

        return new ArrayList<>(result);
    }
}
