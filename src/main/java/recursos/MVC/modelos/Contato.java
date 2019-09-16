package recursos.MVC.modelos;


import recursos.MVC.modelos.annotations.ChavePrimaria;
import recursos.MVC.modelos.annotations.Unico;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.StringJoiner;

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

    public Contato(HashMap<String, String > hashMap) {
        this.id_contato = Integer.parseInt(hashMap.get("id_contato"));
        this.primeiro_nome = hashMap.get("primeiro_nome");
        this.ultimo_nome = hashMap.get("ultimo_nome");
        this.email = hashMap.get("email");
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

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(this.primeiro_nome);
        sj.add(this.ultimo_nome);
        sj.add(" :: ");
        sj.add(this.email);
        return sj.toString();
    }

    /**
     *  Metodo que compara se a intancia do contato coresponde a um determinado nome.
     *
     *  Verifica o nome e o sobrenome.
     *
     * @param nome Nome a ser comparado.
     * @return true se corresponde ou false se não corresponde.
     */
    public boolean comparaNome(String nome) {

        String n1 = normalizaNome(this.primeiro_nome);
        String n2 = normalizaNome(this.ultimo_nome);
        nome = normalizaNome(nome);

        if (n1.contains(nome) || n2.contains(nome)) {
            return true;
        } else {
            return false;
        }
    }

    public String normalizaNome(String s1) {
        s1 = Normalizer
                .normalize(s1, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();

        return s1;
    }
}
