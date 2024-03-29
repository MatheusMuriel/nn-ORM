package recursos.ORM;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Documentação: https://www.sqlite.org/lang_createtable.html
 */
public class Tabela {
    String schema;
    String nome;
    ArrayList<Coluna> colunas = new ArrayList<>();
    ArrayList<Object> linhas = new ArrayList<>();
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

        if (parametros.containsKey("nome")) this.nome = parametros.get("nome");
        if (parametros.containsKey("modelo")) {
            // Class<?> clazz = Class.forName(parametros.get("modelo"));
            //this.linhas = instanceArray(clazz);
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

    public void adicionarObjeto(Object objeto) {

        // Verifica e adiciona somente se o objeto não existir na linha
        if ( linhas.stream().noneMatch( l -> l.toString().equals(objeto.toString())) ) {
            this.linhas.add(objeto);
        } else {
            System.err.println("Aviso em Tabela::adicionarObjeto. Foi tentado adicior um objeto já existente.");
        }
    }

    public void removerObjeto(Object objeto) {
        this.linhas.remove(objeto);
    }

    public ArrayList<Object> getLinhas() {
        return linhas;
    }

    public boolean comparaNome(Tabela t2) {
        Tabela t1 = this;

        return t1.getNome().equals(t2.getNome());
    }

    public boolean comparaNome(String t2) {
        Tabela t1 = this;

        return t1.getNome().equals(t2);
    }

    public Object filtraLinhasPorId(String id) {
        ArrayList<Object> todasLinhas = this.getLinhas();

        Object oSaida = null;

        for (Object o : todasLinhas) {

            if (oSaida != null){
                break;
            }

            Class<?> clazz = o.getClass();

            for(Field field : clazz.getDeclaredFields()) {
                String nomeAtt = field.getName();
                if (nomeAtt.contains("id_")) {
                    try {
                        field.setAccessible(true);
                        String valorId = String.valueOf( field.get(o) );

                        if (valorId.equals(id)){
                            oSaida = o;
                            break;
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return oSaida;
    }

    public String getIdPorObject(Object object) {
        ArrayList<Object> todasLinhas = this.getLinhas();

        String saida = "";

        for (Object o : todasLinhas) {

            if (!saida.equals("")){
                break;
            }

            if ( object.toString().equals( o.toString() ) ) {
                Class<?> clazz = o.getClass();

                for(Field field : clazz.getDeclaredFields()) {
                    String nomeAtt = field.getName();
                    if (nomeAtt.contains("id_")) {
                        try {
                            field.setAccessible(true);
                            saida = String.valueOf( field.get(o) );
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return saida;
    }

}
