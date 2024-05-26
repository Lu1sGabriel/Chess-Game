package src.main.java.boardGame;

import src.main.java.boardGame.exceptions.BoardException;

import java.util.Objects;

public class Board {

    private final int rows;
    private final int columns;
    private final Piece[][] pieces;

    public Board(final int rows, final int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Erro ao criar o tabuleiro: deve haver pelo menos uma linha e uma coluna. ");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(final int row, final int column) {
        if (!positionExists(row, column)) {
            throw new BoardException(String.format("Posição inexistente no tabuleiro: %d, %d", row, column));
        }
        return pieces[row][column];
    }

    public Piece piece(final Position position) {
        Objects.requireNonNull(position, "Posição não pode ser nula. ");
        if (!positionExists(position)) {
            throw new BoardException(String.format("Posição inexistente no tabuleiro: %s", position));
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(final Piece piece, final Position position) {
        Objects.requireNonNull(piece, "Peça não pode ser nula. ");
        Objects.requireNonNull(position, "Posição não pode ser nula. ");
        if (thereIsAPiece(position)) {
            throw new BoardException(String.format("Já existe uma peça nessa posição: %s", position));
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public Piece removePiece(final Position position) {
        Objects.requireNonNull(position, "Posição não pode ser nula. ");
        if (!positionExists(position)) {
            throw new BoardException(String.format("Posição inexistente no tabuleiro: %s", position));
        }
        if (piece(position) == null) {
            return null;
        }
        var auxiliary = piece(position);
        auxiliary.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return auxiliary;
    }

    private boolean positionExists(final int row, final int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExists(final Position position) {
        Objects.requireNonNull(position, "Posição não pode ser nula.");
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(final Position position) {
        Objects.requireNonNull(position, "Posição não pode ser nula. ");
        if (!positionExists(position)) {
            throw new BoardException(String.format("Posição inexistente no tabuleiro: %s", position));
        }
        return piece(position) != null;
    }

}
