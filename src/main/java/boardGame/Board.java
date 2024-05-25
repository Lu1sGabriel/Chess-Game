package src.main.java.boardGame;

import src.main.java.boardGame.exceptions.BoardException;

public class Board {
    private final int rows;
    private final int columns;
    private final Piece[][] pieces;

    public Board(int rows, int columns) {
        validateBoardSize(rows, columns);
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {
        validatePosition(row, column);
        return pieces[row][column];
    }

    public Piece piece(Position position) {
        validatePosition(position);
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("Já existe uma peça ocupando a posição: " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.setPosition(position);
    }

    public Piece removePiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Essa posição não existe. ");
        }
        if (piece(position) == null) {
            return null;
        }
        var auxiliary = piece(position);
        auxiliary.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return auxiliary;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean thereIsAPiece(Position position) {
        validatePosition(position);
        return piece(position) != null;
    }

    private void validateBoardSize(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Erro criando o tabuleiro: É necessário que haja pelo menos uma linha e uma coluna.");
        }
    }

    private void validatePosition(int row, int column) {
        if (!positionExists(row, column)) {
            throw new BoardException("Posição inexistente no tabuleiro.");
        }
    }

    private void validatePosition(Position position) {
        validatePosition(position.getRow(), position.getColumn());
    }

}
