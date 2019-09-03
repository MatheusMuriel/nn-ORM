package ORM;

import java.util.StringJoiner;

/**
 * Documentação: https://www.sqlite.org/syntax/column-def.html
 */
public class Coluna{
    String nome;
    String tipoDeDado;
    String constraint;

    /**
     * Metodo construtor padrão.
     * Caso o parametro for vazio passar uma String vazia.
     * @param nome Nome da Coluna.
     * @param tipoDeDado DateType da coluna.(https://www.sqlite.org/syntax/type-name.html)
     * @param constraint Constraints da coluna(NotNull, Unique, PrimaryKey, etc..)(https://www.sqlite.org/syntax/column-constraint.html)
     */
    public Coluna(String nome, String tipoDeDado, String constraint) {
        this.nome = nome;
        this.tipoDeDado = tipoDeDado;
        this.constraint = constraint;
    }


    /**
     * Metodo que transforma o objeto em uma String.
     * Usado para criar tabelas por exemplo.
     * Sintaxe: https://www.sqlite.org/syntax/column-def.html
     * https://www.w3schools.com/sql/sql_create_table.asp
     */
    @Override
    public String toString(){
        StringJoiner sb = new StringJoiner(" ");

        //Nome é obrigatorio.
        if (nome != null){
            sb.add(this.nome);

            sb.add(this.tipoDeDado != null ? this.tipoDeDado : "");

            sb.add( this.constraint != null ? this.constraint : "");

            return sb.toString();
        } else {
            System.err.println("O nome é obrigatorio!");
            return "";
        }
    }

}
