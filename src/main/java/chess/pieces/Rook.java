package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.PlayerColor;

import java.util.Objects;

/**
 * Classe Rook que representa a peça de Torre no jogo de xadrez.
 * Esta classe herda de ChessPiece.
 */
public class Rook extends ChessPiece {

    /**
     * Matriz de direções possíveis para o movimento da Torre.
     * A Torre pode se mover nas direções norte, sul, oeste e leste.
     */
    private static final int[][] DIRECTIONS = {
            {-1, 0}, // Norte
            {1, 0},  // Sul
            {0, -1}, // Oeste
            {0, 1}   // Leste
    };

    /**
     * Construtor da classe Rook.
     *
     * @param board Tabuleiro do jogo.
     * @param playerColor Cor da peça.
     */
    public Rook(final Board board, final PlayerColor playerColor) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo. "),
                Objects.requireNonNull(playerColor, "A cor não pode ser nula. "));
    }

    /**
     * Calcula os movimentos possíveis para a Torre.
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
     * Verifica uma direção específica para os movimentos possíveis da Torre.
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
     * Retorna a representação em String da Torre.
     *
     * @return Uma string "R" que representa a Torre.
     */
    @Override
    public String toString() {
        return "Rook";
    }

}