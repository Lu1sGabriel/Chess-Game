package src.main.java.boardGame;

import src.main.java.boardGame.exception.BoardException;

public class Board {
    //  Quantidade de linhas
    private final int rows;
    private final int columns;
    private final Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("""
                    Erro criando o tabuleiro: É necessário que haja pelo menos uma linha e uma coluna.""");
        }
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
        if (!positionExists(row, column)) {
            throw new BoardException("""
                    Posição inexistente no tabuleiro.""");
        }
        return pieces[row][column];
    }

    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("""
                    Posição inexistente no tabuleiro.""");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("""
                    Já existe uma peça ocupando a posição.\s""" + position);
        }
        //  A peça na posição do tabuleiro recebe a peça que está sendo movida.
        pieces[position.getRow()][position.getColumn()] = piece;
        //  A peça recebe a posição.
        piece.position = position;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("""
                    Posição inexistente no tabuleiro.""");
        }
        return piece(position) != null;
    }

}
