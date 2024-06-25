package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.PlayerColor;

import java.util.Objects;

/**
 * Classe Queen que representa a peça de Rainha no jogo de xadrez.
 * Esta classe herda de ChessPiece.
 */
public class Queen extends ChessPiece {

    /**
     * Matriz de direções possíveis para o movimento da Rainha.
     * A Rainha pode se mover nas oito direções: norte, sul, oeste, leste, noroeste, nordeste, sudoeste e sudeste.
     */
    private static final int[][] DIRECTIONS = {
            {-1, 0}, // Norte
            {1, 0},  // Sul
            {0, -1}, // Oeste
            {0, 1},  // Leste
            {-1, -1},// Noroeste
            {-1, 1}, // Nordeste
            {1, -1}, // Sudoeste
            {1, 1},  // Sudeste
    };

    /**
     * Construtor da classe Queen.
     *
     * @param board O tabuleiro do jogo.
     * @param playerColor A cor da peça.
     */
    public Queen(Board board, PlayerColor playerColor) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo. "),
                Objects.requireNonNull(playerColor, "A cor não pode ser nula. "));
    }

    /**
     * Calcula os movimentos possíveis para a Rainha.
     *
     * @return Uma matriz booleana indicando os movimentos possíveis.
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
     * Verifica uma direção específica para os movimentos possíveis da Rainha.
     *
     * @param validMoves Uma matriz de movimentos válidos a ser preenchida.
     * @param rowOffset  O deslocamento na direção da linha.
     * @param colOffset  O deslocamento na direção da coluna.
     */
    private void checkDirection(final boolean[][] validMoves, final int rowOffset, final int colOffset) {
        var currentPosition = new Position(position.getRow() + rowOffset, position.getColumn() + colOffset);
        while (getBoard().positionExists(currentPosition) && !getBoard().thereIsAPiece(currentPosition)) {
            validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
            currentPosition.setValues(currentPosition.getRow() + rowOffset, currentPosition.getColumn() + colOffset);
        }
        if (getBoard().positionExists(currentPosition) && isThereOpponentPiece(currentPosition)) {
            validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
        }
    }

    /**
     * Retorna a representação em String da Rainha.
     *
     * @return Uma string "Q" que representa a Rainha.
     */
    @Override
    public String toString() {
        return "Queen";
    }

}