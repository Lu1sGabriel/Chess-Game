package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.Color;

import java.util.Objects;

/**
 * Classe King que representa a peça de Rei no jogo de xadrez.
 * Esta classe herda de ChessPiece.
 */
public class King extends ChessPiece {

    /**
     * Matriz de direções possíveis para o movimento do Rei.
     * O Rei pode se mover em todas as direções: noroeste, norte, nordeste, oeste, leste, sudoeste, sul e sudeste.
     */
    private static final int[][] DIRECTIONS = {
            {-1, -1}, // Noroeste
            {-1, 0},  // Norte
            {-1, 1},  // Nordeste
            {0, -1},  // Oeste
            {0, 1},   // Leste
            {1, -1},  // Sudoeste
            {1, 0},   // Sul
            {1, 1}    // Sudeste
    };

    /**
     * Construtor da classe King.
     *
     * @param board Tabuleiro do jogo.
     * @param color Cor da peça.
     */
    public King(final Board board, final Color color) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nullo. "),
                Objects.requireNonNull(color, "A cor não pode ser nulla. "));
    }

    /**
     * Calcula os movimentos possíveis para o Rei.
     *
     * @return Matriz booleana indicando os movimentos possíveis.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] validMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        for (int[] moveDirections : DIRECTIONS) {
            checkDirection(validMoves, moveDirections[0], moveDirections[1]);
        }

        return validMoves;
    }

    /**
     * Verifica uma direção específica para os movimentos possíveis do Rei.
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
     * Verifica se o Rei pode se mover para uma nova posição.
     *
     * @param newPosition A nova posição para a qual o Rei pode se mover.
     * @return Verdadeiro se o Rei pode se mover para a nova posição, falso caso contrário.
     */
    private boolean canMove(final Position newPosition) {
        var pieceAtNewPosition = (ChessPiece) getBoard().piece(newPosition);
        return pieceAtNewPosition == null || pieceAtNewPosition.getColor() != getColor();
    }

    /**
     * Retorna a representação em String do Rei.
     *
     * @return Uma string "K" que representa o Rei.
     */
    @Override
    public String toString() {
        return "K";
    }

}