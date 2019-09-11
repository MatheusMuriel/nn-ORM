package muriel.MVC.modelos;

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
}
