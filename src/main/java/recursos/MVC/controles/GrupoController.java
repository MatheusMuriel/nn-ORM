package recursos.MVC.controles;

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

    public void novoGrupo(String descricao) {
        Grupo newGrupo = new Grupo(descricao);
        this.adicionar(newGrupo);
    }

    @Override
    public void adicionar(Grupo grupo) {
        this.db.salvarObjeto(grupo);
    }

    @Override
    public void remover() {

    }

    @Override
    public void atualiza() {

    }

    @Override
    public ArrayList<Grupo> procurar(String descricao) {
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
            System.err.println("Foram encontradas mais de uma tabela com o mesmo nome.");
        } else {
            System.err.println("Tabela não encontrada;");
        }

        return resultado;
    }

}