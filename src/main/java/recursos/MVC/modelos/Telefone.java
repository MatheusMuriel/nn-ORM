package recursos.MVC.modelos;

import recursos.MVC.modelos.annotations.ChavePrimaria;
import recursos.MVC.modelos.annotations.Obrigatorio;

import java.util.HashMap;
import java.util.StringJoiner;

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

    public Telefone(HashMap<String, String > hashMap) {
        this.id_telefone = Integer.parseInt(hashMap.get("id_telefone"));
        this.telefone = hashMap.get("telefone");
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

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(this.telefone);
        return sj.toString();
    }

    /**
     *  Metodo que compara o telefone com uma determinada String.
     *  Desconsidera caracteres especiais
     * @param telefone String a ser comparada.
     * @return true se a string corresponder a um pedaço do telefone.
     */
    public boolean comparaTelefone(String telefone) {
        String t1 = normalize(this.telefone);
        String t2 = normalize(telefone);

        return t1.contains(t2);
    }

    public String normalize(String s) {
        return s.replaceAll("(\\s|\\(|\\)|\\-|\\+)", "");
    }
}
