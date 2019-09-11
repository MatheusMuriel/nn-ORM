package muriel.ORM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

/**
 * Documentação: https://www.sqlite.org/lang_createtable.html
 */
public class Tabela {
    String schema;
    String nome;
    ArrayList<Coluna> colunas = new ArrayList<>();
    ArrayList<?> linhas;
    ArrayList<String> constr; //https://www.sqlite.org/syntax/table-constraint.html

    /**
     * Metodo construtor padrão.
     * Caso o parametro for vazio passar uma String vazia.
     * @param nome Nome da tabela, Sem espacos.
     * @param colunas ArrayList de objetos "Coluna".
     */
    public Tabela(String nome, ArrayList<Coluna> colunas) {
        this.nome = nome;
        this.colunas = colunas;
    }

    /**
     * Construtor vazio para instancia via carregamento da muriel.ORM.
     * (Consultar metodo em muriel.Persistencia.java)
     */
    public Tabela(HashMap<String, String> parametros) {

        try {
            if (parametros.containsKey("nome")) this.nome = parametros.get("nome");
            if (parametros.containsKey("modelo")) {
                Class<?> clazz = Class.forName(parametros.get("modelo"));
                this.linhas = instanceArray(clazz);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public <T> ArrayList<T> instanceArray(Class<T> classItem) {
        ArrayList<T> list = new ArrayList<T>();
        return list;
    }

    @Override
    public String toString(){
        return this.nome;
    }

    /**
     * Metodo que transforma o objeto em uma String.
     * Usado para criar tabelas por exemplo.
     * Sintaxe: https://www.sqlite.org/lang_createtable.html
     * https://www.w3schools.com/sql/sql_create_table.asp
     */
    public String toSQLCreate(){
        StringJoiner s = new StringJoiner(" ");

        s.add("CREATE");
        s.add("TABLE");
        s.add("IF NOT EXISTS");

        if (this.schema != null && !this.schema.equals("")){
            s.add(this.schema.concat(".").concat(this.nome));
        } else {
            s.add(this.nome);
        }

        StringJoiner str_colunas = new StringJoiner(",","(",")");

        for (Coluna c : this.colunas){
            str_colunas.add(c.toString());
        }

        s.add(str_colunas.toString());

        return s.toString();
    }

    public String getSchema() {
        return schema;
    }

    public String getNome() {
        return nome;
    }

    public void adicionarColuna(Coluna coluna) {
        this.colunas.add(coluna);
    }

    public ArrayList<Coluna> getColunas() {
        return colunas;
    }

    public ArrayList<String> getConstr() {
        return constr;
    }
}
