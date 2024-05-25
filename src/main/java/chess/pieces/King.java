package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
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

    //  TODO: Implementar lógica do Rei.
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return matrix;
    }

}
