package src.main.java.boardGame;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe que representa uma posição no tabuleiro de xadrez.
 * Gerencia as coordenadas de uma peça no tabuleiro.
 * <p>
 * Esta classe implementa Serializable para permitir que o estado do objeto
 * seja salvo e carregado de um arquivo, ou transmitido pela rede.
 */
public class Position implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int row;
    private int column;

    /**
     * Constrói uma instância de Position com a linha e coluna especificadas.
     *
     * @param row    a linha da posição
     * @param column a coluna da posição
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Retorna a linha da posição.
     *
     * @return a linha da posição
     */
    public int getRow() {
        return row;
    }

    /**
     * Retorna a coluna da posição.
     *
     * @return a coluna da posição
     */
    public int getColumn() {
        return column;
    }

    /**
     * Define os valores de linha e coluna para a posição.
     *
     * @param row    a nova linha da posição
     * @param column a nova coluna da posição
     */
    public void setValues(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Retorna uma representação em string da posição.
     *
     * @return uma string representando a posição no formato "linha, coluna"
     */
    @Override
    public String toString() {
        return String.format("%d, %d", row, column);
    }

}