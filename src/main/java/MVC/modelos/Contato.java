package MVC.modelos;


public class Contato {
    @ChavePrimaria
    int id_contato;
    String primeiro_nome;
    String ultimo_nome;

    @Unico
    String email;

    /**
     * Construtor padrão da classe.
     * @param primeiro_nome Primeiro nome do Contato.
     * @param ultimo_nome Ultimo nome do contato.
     * @param email Email do contato.
     */
    public Contato(String primeiro_nome, String ultimo_nome, String email) {
        this.primeiro_nome = primeiro_nome;
        this.ultimo_nome = ultimo_nome;
        this.email = email;
    }

    /**
     * Construtor vazio para a população de tabela.
     */
    public Contato() {

    }

    public int getId_contato() {
        return id_contato;
    }

    public void setId_contato(int id_contato) {
        this.id_contato = id_contato;
    }

    public String getPrimeiro_nome() {
        return primeiro_nome;
    }

    public void setPrimeiro_nome(String primeiro_nome) {
        this.primeiro_nome = primeiro_nome;
    }

    public String getUltimo_nome() {
        return ultimo_nome;
    }

    public void setUltimo_nome(String ultimo_nome) {
        this.ultimo_nome = ultimo_nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
