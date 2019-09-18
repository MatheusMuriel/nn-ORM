package recursos.MVC.modelos;

import recursos.MVC.modelos.annotations.ChavePrimaria;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Grupo {
    @ChavePrimaria
    int id_grupo;

    String descricao_grupo;

     ArrayList<Contato> contatos = new ArrayList<>();

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

    /**
     *  Metodo que compara o grupo com uma determinada String.
     * @param descricao String a ser comparada.
     * @return true se a descrição normalizada do grupo corresponder a string.
     */
    public boolean comparaGrupo(String descricao) {
        String d1 = normalizaDescricao(this.getDescricao_grupo());
        String d2 = normalizaDescricao(descricao);

        return d1.contains(d2);
    }

    public String normalizaDescricao(String s1) {
        s1 = Normalizer
                .normalize(s1, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();

        return s1;
    }

    public ArrayList<Contato> getContatos() {
        return contatos;
    }
}
