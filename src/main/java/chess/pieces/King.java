package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    /**
     * Calcula os possíveis movimentos do rei.
     *
     * @return uma matriz booleana representando as casas onde o rei pode se mover
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] validMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // Define as oito direções possíveis (horizontal, vertical e diagonal)
        int[][] directions = {
                {-1, -1}, // Noroeste
                {-1, 0},  // Norte
                {-1, 1},  // Nordeste
                {0, -1},  // Oeste
                {0, 1},   // Leste
                {1, -1},  // Sudoeste
                {1, 0},   // Sul
                {1, 1}    // Sudeste
        };

        for (int[] moveDirection : directions) {
            int rowOffset = moveDirection[0];
            int colOffset = moveDirection[1];

            int newRow = position.getRow() + rowOffset;
            int newCol = position.getColumn() + colOffset;

            Position newPosition = new Position(newRow, newCol);

            // Verifica se a nova posição está dentro dos limites do tabuleiro
            if (getBoard().positionExists(newPosition) && canMove(newPosition)) {
                validMoves[newRow][newCol] = true;
            }
        }

        return validMoves;
    }

    private boolean canMove(Position newPosition) {
        var pieceAtNewPosition = (ChessPiece) getBoard().piece(newPosition);
        return pieceAtNewPosition == null || pieceAtNewPosition.getColor() != getColor();
    }

}
