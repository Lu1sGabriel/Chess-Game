package src.main.java.boardGame;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa o tabuleiro de um jogo de xadrez, contendo as peças e gerenciando as operações
 * de colocação, remoção e consulta de peças em posições específicas.
 * <p>
 * Esta classe implementa Serializable para permitir que o estado do objeto
 * seja salvo e carregado de um arquivo, ou transmitido pela rede.
 */
public class Board implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int rows;
    private final int columns;
    private final Piece[][] pieces;

    /**
     * Constrói uma instância de Board com o número especificado de linhas e colunas.
     *
     * @param rows    o número de linhas do tabuleiro
     * @param columns o número de colunas do tabuleiro
     * @throws IllegalArgumentException se o número de linhas ou colunas for menor ou igual a zero
     */
    public Board(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Erro ao criar o tabuleiro: é necessário que haja pelo menos 1 linha e 1 coluna.");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    /**
     * Retorna o número de linhas do tabuleiro.
     *
     * @return o número de linhas
     */
    public int getRows() {
        return rows;
    }

    /**
     * Retorna o número de colunas do tabuleiro.
     *
     * @return o número de colunas
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Retorna a peça na posição especificada.
     *
     * @param row    a linha da posição
     * @param column a coluna da posição
     * @return a peça na posição especificada, ou null se não houver peça
     * @throws IllegalArgumentException se a posição for inválida
     */
    public Piece piece(int row, int column) {
        validatePosition(row, column);
        return pieces[row][column];
    }

    /**
     * Retorna a peça na posição especificada.
     *
     * @param position a posição a ser consultada
     * @return a peça na posição especificada, ou null se não houver peça
     * @throws NullPointerException     se a posição for nula
     * @throws IllegalArgumentException se a posição for inválida
     */
    public Piece piece(Position position) {
        Objects.requireNonNull(position, "A posição não pode ser nula.");
        return piece(position.getRow(), position.getColumn());
    }

    /**
     * Coloca uma peça na posição especificada.
     *
     * @param piece    a peça a ser colocada
     * @param position a posição onde a peça será colocada
     * @throws NullPointerException     se a peça ou a posição forem nulas
     * @throws IllegalArgumentException se já houver uma peça na posição
     */
    public void placePiece(Piece piece, Position position) {
        Objects.requireNonNull(piece, "A peça não pode ser nula.");
        Objects.requireNonNull(position, "A posição não pode ser nula.");
        if (thereIsAPiece(position)) {
            throw new IllegalArgumentException("Já existe uma peça na posição " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    /**
     * Remove a peça na posição especificada.
     *
     * @param position a posição de onde a peça será removida
     * @return a peça removida, ou null se não houver peça
     * @throws NullPointerException     se a posição for nula
     * @throws IllegalArgumentException se a posição for inválida
     */
    public Piece removePiece(Position position) {
        Objects.requireNonNull(position, "A posição não pode ser nula.");
        validatePosition(position.getRow(), position.getColumn());
        if (!thereIsAPiece(position)) {
            return null;
        }
        Piece removedPiece = pieces[position.getRow()][position.getColumn()];
        pieces[position.getRow()][position.getColumn()] = null;
        if (removedPiece != null) {
            removedPiece.position = null;
        }
        return removedPiece;
    }

    /**
     * Verifica se existe uma peça na posição especificada.
     *
     * @param position a posição a ser verificada
     * @return true se houver uma peça na posição, false caso contrário
     * @throws NullPointerException     se a posição for nula
     * @throws IllegalArgumentException se a posição for inválida
     */
    public boolean thereIsAPiece(Position position) {
        Objects.requireNonNull(position, "A posição não pode ser nula.");
        return piece(position) != null;
    }

    /**
     * Verifica se a posição especificada está dentro dos limites do tabuleiro.
     *
     * @param position a posição a ser verificada
     * @return true se a posição estiver dentro dos limites, false caso contrário
     * @throws NullPointerException se a posição for nula
     */
    public boolean positionExists(Position position) {
        Objects.requireNonNull(position, "A posição não pode ser nula.");
        return position.getRow() >= 0 && position.getRow() < rows &&
                position.getColumn() >= 0 && position.getColumn() < columns;
    }

    /**
     * Valida se a posição especificada está dentro dos limites do tabuleiro.
     *
     * @param row    a linha da posição
     * @param column a coluna da posição
     * @throws IllegalArgumentException se a posição for inválida
     */
    private void validatePosition(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IllegalArgumentException("Posição fora do tabuleiro.");
        }
    }

}