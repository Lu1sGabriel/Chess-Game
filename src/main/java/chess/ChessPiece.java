package src.main.java.chess;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Piece;
import src.main.java.boardGame.Position;

import java.util.Objects;

public abstract class ChessPiece extends Piece {

    private final Color color;

    public ChessPiece(final Board board, final Color color) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nullo. "));
        this.color = Objects.requireNonNull(color, "A cor não pode ser nulla. ");
    }

    public Color getColor() {
        return color;
    }

    /**
     * Converts the position of this piece from a standard position to a chess position.
     *
     * @return the chess position of this piece.
     */
    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    /**
     * Checks if there is an opponent's piece at the given position.
     *
     * @param position the position to check.
     * @return true if there is an opponent's piece at the given position, false otherwise.
     */
    protected boolean isThereOpponentPiece(final Position position) {
        var piece = (ChessPiece) getBoard().piece(position);
        if (piece != null) {
            return piece.getColor() != color;
        } else {
            return false;
        }
    }

}