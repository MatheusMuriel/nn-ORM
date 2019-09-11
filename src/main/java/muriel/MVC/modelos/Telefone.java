package muriel.MVC.modelos;

public class Telefone {
    @ChavePrimaria
    int id_telefone;

    @Obrigatorio
    String telefone;

    /**
     * Construtor padrão da classe.
     * @param telefone Numero do telefone.
     */
    public Telefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Construtor vazio para a população de tabela.
     */
    public Telefone() {

    }

    public int getId_telefone() {
        return id_telefone;
    }

    public void setId_telefone(int id_telefone) {
        this.id_telefone = id_telefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
