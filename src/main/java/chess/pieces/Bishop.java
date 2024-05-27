package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.PlayerColor;

import java.util.Objects;

/**
 * Classe Bishop que representa a peça de Bispo no jogo de xadrez.
 * Esta classe herda de ChessPiece.
 */
public class Bishop extends ChessPiece {

    /**
     * Matriz de direções possíveis para o movimento do Bispo.
     * O Bispo pode se mover nas direções noroeste, nordeste, sudoeste e sudeste.
     */
    private static final int[][] DIRECTIONS = {
            {-1, -1}, // Noroeste
            {-1, 1},  // Nordeste
            {1, -1},  // Sudoeste
            {1, 1}    // Sudeste
    };

    /**
     * Construtor da classe Bishop.
     *
     * @param board Tabuleiro do jogo.
     * @param playerColor Cor da peça.
     */
    public Bishop(Board board, PlayerColor playerColor) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo. "),
                Objects.requireNonNull(playerColor, "A cor não pode ser nula. "));
    }

    /**
     * Calcula os movimentos possíveis para o Bispo.
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
     * Verifica uma direção específica para os movimentos possíveis do Bispo.
     *
     * @param validMoves Matriz de movimentos válidos a ser preenchida.
     * @param rowOffset  Deslocamento na direção da linha.
     * @param colOffset  Deslocamento na direção da coluna.
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
     * Retorna a representação em String do Bispo.
     *
     * @return Uma string "B" que representa o Bispo.
     */
    @Override
    public String toString() {
        return "B";
    }

}