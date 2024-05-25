package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
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
}
