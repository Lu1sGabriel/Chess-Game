package src.main.java.boardGame;

import src.main.java.boardGame.exceptions.BoardException;

import java.util.Objects;

/**
 * Classe que representa o tabuleiro de xadrez.
 * Gerencia a posição e manipulação das peças no tabuleiro.
 */
public class Board {

    private static final String POSITION_CANNOT_BE_NULL = "Posição não pode ser nula. ";
    private static final String PIECE_CANNOT_BE_NULL = "Peça não pode ser nula. ";
    private static final String POSITION_NOT_EXIST = "Posição inexistente no tabuleiro: %d, %d ";
    private static final String PIECE_ALREADY_EXIST = "Já existe uma peça nessa posição: %s ";

    private final int rows;
    private final int columns;
    private final Piece[][] pieces;

    /**
     * Construtor que cria um tabuleiro com o número especificado de linhas e colunas.
     *
     * @param rows    Número de linhas do tabuleiro.
     * @param columns Número de colunas do tabuleiro.
     * @throws BoardException se o número de linhas ou colunas for menor que 1.
     */
    public Board(final int rows, final int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Erro ao criar o tabuleiro: deve haver pelo menos uma linha e uma coluna. ");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    /**
     * Retorna o número de linhas do tabuleiro.
     *
     * @return Número de linhas do tabuleiro.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Retorna o número de colunas do tabuleiro.
     *
     * @return Número de colunas do tabuleiro.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Retorna a peça na posição especificada por linha e coluna.
     *
     * @param row    Linha da posição da peça.
     * @param column Coluna da posição da peça.
     * @return A peça na posição especificada ou null se não houver peça.
     * @throws BoardException se a posição for inválida.
     */
    public Piece piece(final int row, final int column) {
        validatePosition(row, column);
        return pieces[row][column];
    }

    /**
     * Retorna a peça na posição especificada.
     *
     * @param position A posição da peça.
     * @return A peça na posição especificada ou null se não houver peça.
     * @throws NullPointerException se a posição for nula.
     * @throws BoardException       se a posição for inválida.
     */
    public Piece piece(final Position position) {
        Objects.requireNonNull(position, POSITION_CANNOT_BE_NULL);
        validatePosition(position);
        return pieces[position.getRow()][position.getColumn()];
    }

    /**
     * Coloca uma peça na posição especificada.
     *
     * @param piece    A peça a ser colocada no tabuleiro.
     * @param position A posição onde a peça será colocada.
     * @throws NullPointerException se a peça ou a posição forem nulas.
     * @throws BoardException       se já houver uma peça na posição ou se a posição for inválida.
     */
    public void placePiece(final Piece piece, final Position position) {
        Objects.requireNonNull(piece, PIECE_CANNOT_BE_NULL);
        Objects.requireNonNull(position, POSITION_CANNOT_BE_NULL);
        if (thereIsAPiece(position)) {
            throw new BoardException(String.format(PIECE_ALREADY_EXIST, position));
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    /**
     * Remove e retorna a peça na posição especificada.
     *
     * @param position A posição da peça a ser removida.
     * @return A peça removida ou null se não houver peça na posição.
     * @throws NullPointerException se a posição for nula.
     * @throws BoardException       se a posição for inválida.
     */
    public Piece removePiece(final Position position) {
        Objects.requireNonNull(position, POSITION_CANNOT_BE_NULL);
        validatePosition(position);
        if (piece(position) == null) {
            return null;
        }
        var auxiliary = piece(position);
        auxiliary.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return auxiliary;
    }

    /**
     * Verifica se a posição especificada por linha e coluna existe no tabuleiro.
     *
     * @param row    Linha da posição.
     * @param column Coluna da posição.
     * @return true se a posição existir no tabuleiro, caso contrário false.
     */
    private boolean positionExists(final int row, final int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    /**
     * Verifica se a posição especificada existe no tabuleiro.
     *
     * @param position A posição a ser verificada.
     * @return true se a posição existir no tabuleiro, caso contrário false.
     * @throws NullPointerException se a posição for nula.
     */
    public boolean positionExists(final Position position) {
        Objects.requireNonNull(position, POSITION_CANNOT_BE_NULL);
        return positionExists(position.getRow(), position.getColumn());
    }

    /**
     * Verifica se existe uma peça na posição especificada.
     *
     * @param position A posição a ser verificada.
     * @return true se houver uma peça na posição, caso contrário false.
     * @throws NullPointerException se a posição for nula.
     * @throws BoardException       se a posição for inválida.
     */
    public boolean thereIsAPiece(final Position position) {
        Objects.requireNonNull(position, POSITION_CANNOT_BE_NULL);
        validatePosition(position);
        return piece(position) != null;
    }

    /**
     * Valida se a posição especificada por linha e coluna existe no tabuleiro.
     *
     * @param row    Linha da posição.
     * @param column Coluna da posição.
     * @throws BoardException se a posição não existir no tabuleiro.
     */
    private void validatePosition(final int row, final int column) {
        if (!positionExists(row, column)) {
            throw new BoardException(String.format(POSITION_NOT_EXIST, row, column));
        }
    }

    /**
     * Valida se a posição especificada existe no tabuleiro.
     *
     * @param position A posição a ser validada.
     * @throws BoardException se a posição não existir no tabuleiro.
     */
    private void validatePosition(final Position position) {
        if (!positionExists(position)) {
            throw new BoardException(String.format(POSITION_NOT_EXIST, position.getRow(), position.getColumn()));
        }
    }

}