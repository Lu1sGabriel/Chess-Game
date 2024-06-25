package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.PlayerColor;

import java.util.Objects;

/**
 * Classe Knight que representa a peça de Cavalo no jogo de xadrez.
 * Esta classe herda de {@link ChessPiece}.
 */
public class Knight extends ChessPiece {

    /**
     * Matriz de direções possíveis para o movimento do Cavalo.
     * O Cavalo pode se mover em oito direções diferentes, combinando 2 casas em uma direção e 1 casa em uma direção perpendicular.
     */
    private static final int[][] DIRECTIONS = {
            {-2, -1},
            {-2, 1},
            {-1, -2},
            {-1, 2},
            {1, -2},
            {1, 2},
            {2, -1},
            {2, 1}
    };

    /**
     * Construtor da classe Knight.
     *
     * @param board       Tabuleiro do jogo.
     * @param playerColor Cor da peça.
     */
    public Knight(Board board, PlayerColor playerColor) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo."),
                Objects.requireNonNull(playerColor, "A cor não pode ser nula."));
    }

    /**
     * Calcula os movimentos possíveis para o Cavalo.
     *
     * @return Matriz booleana indicando os movimentos possíveis.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] validMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        for (int[] moveDirection : DIRECTIONS) {
            checkDirection(validMoves, moveDirection[0], moveDirection[1]);
        }

        return validMoves;
    }

    /**
     * Verifica uma direção específica para os movimentos possíveis do Cavalo.
     *
     * @param validMoves Matriz de movimentos válidos a ser preenchida.
     * @param rowOffset  Deslocamento na direção da linha.
     * @param colOffset  Deslocamento na direção da coluna.
     */
    private void checkDirection(final boolean[][] validMoves, final int rowOffset, final int colOffset) {
        var currentPosition = new Position(position.getRow() + rowOffset, position.getColumn() + colOffset);
        if (getBoard().positionExists(currentPosition) && canMove(currentPosition)) {
            validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
        }
    }

    /**
     * Verifica se o Cavalo pode se mover para uma nova posição.
     *
     * @param newPosition A nova posição para a qual o Cavalo pode se mover.
     * @return Verdadeiro se o Cavalo pode se mover para a nova posição, falso caso contrário.
     */
    private boolean canMove(final Position newPosition) {
        var pieceAtNewPosition = (ChessPiece) getBoard().piece(newPosition);
        return pieceAtNewPosition == null || pieceAtNewPosition.getColor() != getColor();
    }

    /**
     * Retorna a representação em String do Cavalo.
     *
     * @return Uma string "N" que representa o Cavalo.
     */
    @Override
    public String toString() {
        return "Knight";
    }

}