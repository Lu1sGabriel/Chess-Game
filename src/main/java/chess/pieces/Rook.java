package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.Color;

import java.util.Objects;

public class Rook extends ChessPiece {

    // Define as quatro direções possíveis (vertical e horizontal)
    private static final int[][] DIRECTIONS = {
            {-1, 0}, // Norte
            {1, 0},  // Sul
            {0, -1}, // Oeste
            {0, 1}   // Leste
    };

    public Rook(final Board board, final Color color) {
        super(Objects.requireNonNull(board, "Board cannot be null"), Objects.requireNonNull(color, "Color cannot be null"));
    }

    @Override
    public String toString() {
        return "R";
    }

    /**
     * Calcula as posições possíveis de movimento para a peça.
     *
     * @return Uma matriz booleana indicando as posições possíveis.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] validMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];
        var currentPosition = new Position(0, 0);

        for (int[] dir : DIRECTIONS) {
            int rowOffset = dir[0];
            int colOffset = dir[1];

            currentPosition.setValues(this.position.getRow() + rowOffset, this.position.getColumn() + colOffset);
            while (getBoard().positionExists(currentPosition) && !getBoard().thereIsAPiece(currentPosition)) {
                validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
                currentPosition.setValues(currentPosition.getRow() + rowOffset, currentPosition.getColumn() + colOffset);
            }
            if (getBoard().positionExists(currentPosition) && isThereOpponentPiece(currentPosition)) {
                validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
            }
        }

        return validMoves;
    }

}
