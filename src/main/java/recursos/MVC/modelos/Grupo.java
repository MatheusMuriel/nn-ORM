package recursos.MVC.modelos;

import java.util.HashMap;
import java.util.StringJoiner;

public class Grupo {
    @ChavePrimaria
    int id_grupo;

    String descricao_grupo;

    /**
     * Construtor padrão da classe.
     * @param descricao_grupo Descrição do grupo.
     */
    public Grupo(String descricao_grupo) {
        this.descricao_grupo = descricao_grupo;
    }

    /**
     * Construtor vazio para a população de tabela.
     */
    public Grupo() {

    }

    public Grupo(HashMap<String, String> hashMap) {
        this.id_grupo = Integer.parseInt(hashMap.get("id_grupo"));
        this.descricao_grupo = hashMap.get("descricao_grupo");
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getDescricao_grupo() {
        return descricao_grupo;
    }

    public void setDescricao_grupo(String descricao_grupo) {
        this.descricao_grupo = descricao_grupo;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(this.descricao_grupo);
        return sj.toString();
    }
}
