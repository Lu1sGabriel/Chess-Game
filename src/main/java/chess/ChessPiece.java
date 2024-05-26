package src.main.java.chess;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Piece;
import src.main.java.boardGame.Position;

import java.util.Objects;

public abstract class ChessPiece extends Piece {

    private final PlayerColor playerColor;

    private int moveCount;

    public ChessPiece(final Board board, final PlayerColor playerColor) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nullo. "));
        this.playerColor = Objects.requireNonNull(playerColor, "A cor não pode ser nulla. ");
    }

    public PlayerColor getColor() {
        return playerColor;
    }

    public int getMoveCount() {
        return moveCount;
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
            return piece.getColor() != playerColor;
        } else {
            return false;
        }
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount--;
    }

}