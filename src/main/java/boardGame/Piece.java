package src.main.java.boardGame;

import java.util.Objects;

public abstract class Piece {

    protected Position position;
    private final Board board;

    public Piece(final Board board) {
        this.board = Objects.requireNonNull(board, "Board cannot be null");
        position = null;
    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(final Position position) {
        Objects.requireNonNull(position, "Position cannot be null");
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove() {
        boolean[][] possibleMoves = possibleMoves();
        for (boolean[] row : possibleMoves) {
            for (boolean cell : row) {
                if (cell) {
                    return true;
                }
            }
        }
        return false;
    }
}