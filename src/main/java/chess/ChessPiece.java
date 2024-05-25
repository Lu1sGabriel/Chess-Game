package src.main.java.chess;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Piece;

public class ChessPiece extends Piece {
    private final Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
