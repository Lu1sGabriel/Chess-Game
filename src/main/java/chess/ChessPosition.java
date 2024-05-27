package src.main.java.chess;

import src.main.java.boardGame.Position;
import src.main.java.chess.exceptions.ChessException;

import java.util.Objects;

public class ChessPosition {

    private static final char MIN_COLUMN = 'a';
    private static final char MAX_COLUMN = 'h';
    private static final int MIN_ROW = 1;
    private static final int MAX_ROW = 8;

    private final char column;
    private final int row;

    public ChessPosition(final char column, final int row) {
        if (column < MIN_COLUMN || column > MAX_COLUMN || row < MIN_ROW || row > MAX_ROW) {
            throw new ChessException("Erro ao instanciar ChessPosition. Os valores válidos são de a1 a h8. ");
        }
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    protected Position toPosition() {
        return new Position(MAX_ROW - row, column - MIN_COLUMN);
    }

    protected static ChessPosition fromPosition(final Position position) {
        Objects.requireNonNull(position, "A posição nao pode ser nula. ");
        return new ChessPosition((char) (MIN_COLUMN + position.getColumn()), MAX_ROW - position.getRow());
    }

    @Override
    public String toString() {
        return String.format("%c%d", column, row);
    }

}