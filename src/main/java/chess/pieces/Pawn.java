package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.PlayerColor;

/**
 * Classe Pawn que representa um peão em um jogo de xadrez.
 * Esta classe herda de {@link ChessPiece}.
 * O peão tem movimentos específicos, como mover-se para frente, capturar peças na diagonal
 * e realizar o movimento especial "en passant".
 */
public class Pawn extends ChessPiece {

    private final ChessMatch chessMatch;

    public Pawn(Board board, PlayerColor color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        if (getColor() == PlayerColor.WHITE) {
            checkVerticalMove(mat, p, -1);
            checkInitialTwoSquaresMove(mat, -1, -2);
            checkDiagonalMove(mat, p, -1, -1);
            checkDiagonalMove(mat, p, -1, 1);
            checkEnPassantMove(mat, 3, -1);
        } else {
            checkVerticalMove(mat, p, 1);
            checkInitialTwoSquaresMove(mat, 1, 2);
            checkDiagonalMove(mat, p, 1, -1);
            checkDiagonalMove(mat, p, 1, 1);
            checkEnPassantMove(mat, 4, 1);
        }

        return mat;
    }

    private void checkVerticalMove(boolean[][] mat, Position p, int rowOffset) {
        p.setValues(position.getRow() + rowOffset, position.getColumn());
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }

    private void checkInitialTwoSquaresMove(boolean[][] mat, int rowOffset, int initialRowOffset) {
        Position p1 = new Position(position.getRow() + rowOffset, position.getColumn());
        Position p2 = new Position(position.getRow() + initialRowOffset, position.getColumn());
        if (getBoard().positionExists(p1) && !getBoard().thereIsAPiece(p1) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
            mat[p2.getRow()][p2.getColumn()] = true;
        }
    }

    private void checkDiagonalMove(boolean[][] mat, Position p, int rowOffset, int colOffset) {
        p.setValues(position.getRow() + rowOffset, position.getColumn() + colOffset);
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }

    private void checkEnPassantMove(boolean[][] mat, int row, int rowOffset) {
        if (position.getRow() == row) {
            Position left = new Position(position.getRow(), position.getColumn() - 1);
            if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                mat[left.getRow() + rowOffset][left.getColumn()] = true;
            }
            Position right = new Position(position.getRow(), position.getColumn() + 1);
            if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                mat[right.getRow() + rowOffset][right.getColumn()] = true;
            }
        }
    }

    @Override
    public String toString() {
        return "P";
    }
}
