package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.Color;

public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
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

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
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
