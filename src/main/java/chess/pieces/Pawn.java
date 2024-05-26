package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.PlayerColor;

import java.util.Objects;

/**
 * Classe Pawn que representa a peça de Peão no jogo de xadrez.
 * Esta classe herda de ChessPiece.
 */
public class Pawn extends ChessPiece {

    /**
     * Matriz de direções possíveis para o movimento do Peão.
     * O Peão pode se mover verticalmente para cima ou para baixo.
     */
    private static final int[][] DIRECTIONS = {
            {-1, 0}, // Movimento vertical (para cima)
            {1, 0}   // Movimento vertical (para baixo)
    };

    /**
     * Construtor da classe Pawn.
     *
     * @param board Tabuleiro do jogo.
     * @param playerColor Cor da peça.
     */
    public Pawn(final Board board, final PlayerColor playerColor) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nullo. "),
                Objects.requireNonNull(playerColor, "A cor não pode ser nulla. "));
    }

    /**
     * Calcula os movimentos possíveis para o Peão.
     *
     * @return Matriz booleana indicando os movimentos possíveis.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] validMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        if (getColor().equals(PlayerColor.WHITE)) {
            calculateWhitePawnMoves(validMoves);
        } else {
            calculateBlackPawnMoves(validMoves);
        }

        return validMoves;
    }

    /**
     * Calcula os movimentos possíveis para um Peão de cor branca.
     *
     * @param validMoves Matriz de movimentos válidos a ser preenchida.
     */
    private void calculateWhitePawnMoves(final boolean[][] validMoves) {
        checkVerticalMove(validMoves, -1);
        checkInitialMove(validMoves, -2, -1);
        checkDiagonalMoves(validMoves, -1);
    }

    /**
     * Calcula os movimentos possíveis para um Peão de cor preta.
     *
     * @param validMoves Matriz de movimentos válidos a ser preenchida.
     */
    private void calculateBlackPawnMoves(final boolean[][] validMoves) {
        checkVerticalMove(validMoves, 1);
        checkInitialMove(validMoves, 2, 1);
        checkDiagonalMoves(validMoves, 1);
    }

    /**
     * Verifica o movimento vertical do Peão.
     *
     * @param validMoves Matriz de movimentos válidos a ser preenchida.
     * @param direction  Direção do movimento.
     */
    private void checkVerticalMove(final boolean[][] validMoves, final int direction) {
        var currentPosition = new Position(position.getRow() + direction, position.getColumn());
        if (getBoard().positionExists(currentPosition) && !getBoard().thereIsAPiece(currentPosition)) {
            validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
        }
    }

    /**
     * Verifica o movimento inicial do Peão.
     *
     * @param validMoves            Matriz de movimentos válidos a ser preenchida.
     * @param direction             Direção do movimento.
     * @param freePositionDirection Direção da posição livre.
     */
    private void checkInitialMove(final boolean[][] validMoves, final int direction, final int freePositionDirection) {
        if (getMoveCount() == 0) {
            var currentPosition = new Position(position.getRow() + direction, position.getColumn());
            var positionFree = new Position(position.getRow() + freePositionDirection, position.getColumn());
            if (getBoard().positionExists(currentPosition) && !getBoard().thereIsAPiece(currentPosition)
                    && getBoard().positionExists(positionFree) && !getBoard().thereIsAPiece(positionFree)) {
                validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
            }
        }
    }

    /**
     * Verifica os movimentos diagonais do Peão.
     *
     * @param validMoves Matriz de movimentos válidos a ser preenchida.
     * @param direction  Direção do movimento.
     */
    private void checkDiagonalMoves(final boolean[][] validMoves, final int direction) {
        for (int[] dir : DIRECTIONS) {
            int rowOffset = dir[0];
            int colOffset = dir[1];
            var currentPosition = new Position(position.getRow() + rowOffset * direction, position.getColumn() + colOffset);
            if (getBoard().positionExists(currentPosition) && isThereOpponentPiece(currentPosition)) {
                validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
            }
        }
    }

    /**
     * Retorna a representação em String do Peão.
     *
     * @return Uma string "P" que representa o Peão.
     */
    @Override
    public String toString() {
        return "P";
    }

}