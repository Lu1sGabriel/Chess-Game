package src.main.java.chess;

import src.main.java.boardGame.Position;
import src.main.java.chess.exceptions.ChessException;

public class ChessPosition {
    private final char column;
    private final int row;

    public ChessPosition(char column, int row) {
        positionDoesntExits(column, row);
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    private void positionDoesntExits(char colum, int row) {
        if (colum < 'a' || colum > 'h' || row < 1 || row > 8) {
            throw new ChessException("Valores válidos são de A1 até A8. ");
        }
    }

    protected Position toPosition() {
        return new Position(8 - row, column - 'a');
    }

    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char) ('a' - position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString() {
        return "" + column + row;
    }
}
