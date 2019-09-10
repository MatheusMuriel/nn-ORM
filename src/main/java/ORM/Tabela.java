package ORM;

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
    ArrayList<String> constr; //https://www.sqlite.org/syntax/table-constraint.html

    /**
     * Metodo construtor padrão.
     * Caso o parametro for vazio passar uma String vazia.
     * @param schema Schema da tabela.
     * @param nome Nome da tabela, Sem espacos.
     * @param colunas ArrayList de objetos "Coluna".
     * @param constr Constraints da tabela(PrimaryKey, ForeignKey)(https://www.sqlite.org/syntax/table-constraint.html)
     */
    public Tabela(String schema, String nome, ArrayList<Coluna> colunas, ArrayList<String> constr) {
        this.schema = schema;
        this.nome = nome;
        this.colunas = colunas;
        this.constr = constr;
    }

    /**
     * Construtor vazio para instancia via carregamento da ORM.
     * (Consultar metodo em Persistencia.java)
     */
    public Tabela(HashMap<String, String> parametros) {

        if (parametros.containsKey("nome")) this.nome = parametros.get("nome");

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
