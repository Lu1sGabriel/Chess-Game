package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.PlayerColor;

import java.util.Objects;

/**
 * Classe King que representa a peça de Rei no jogo de xadrez.
 * Esta classe herda de {@link ChessPiece}.
 */
public class King extends ChessPiece {

    private final ChessMatch chessMatch;

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
     * @param board       Tabuleiro do jogo.
     * @param playerColor Cor da peça.
     * @param chessMatch  A partida de xadrez atual.
     */
    public King(final Board board, final PlayerColor playerColor, ChessMatch chessMatch) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo."),
                Objects.requireNonNull(playerColor, "A cor não pode ser nula."));
        this.chessMatch = chessMatch;
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

        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            smallCastling(validMoves);
            largeCastling(validMoves);
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
     * Realiza a verificação e execução do roque pequeno.
     * O roque pequeno é um movimento especial que envolve o rei e a torre.
     * Este método verifica se o roque pequeno é possível e, em caso afirmativo, realiza o movimento.
     *
     * @param validMoves Matriz de movimentos válidos a ser preenchida.
     */
    private void smallCastling(boolean[][] validMoves) {
        var positionRookRight = new Position(position.getRow(), position.getColumn() + 3);
        if (testRookCastling(positionRookRight)) {
            var positionOne = new Position(position.getRow(), position.getColumn() + 1);
            var positionTwo = new Position(position.getRow(), position.getColumn() + 2);
            if (getBoard().piece(positionOne) == null && getBoard().piece(positionTwo) == null) {
                validMoves[position.getRow()][position.getColumn() + 2] = true;
            }
        }
    }

    /**
     * Realiza a verificação e execução do roque grande.
     * O roque grande é um movimento especial que envolve o rei e a torre.
     * Este método verifica se o roque grande é possível e, em caso afirmativo, realiza o movimento.
     *
     * @param validMoves Matriz de movimentos válidos a ser preenchida.
     */
    private void largeCastling(boolean[][] validMoves) {
        var positionRookLeft = new Position(position.getRow(), position.getColumn() - 4);
        if (testRookCastling(positionRookLeft)) {
            var positionOne = new Position(position.getRow(), position.getColumn() - 1);
            var positionTwo = new Position(position.getRow(), position.getColumn() - 2);
            var positionThree = new Position(position.getRow(), position.getColumn() - 3);
            if (getBoard().piece(positionOne) == null && getBoard().piece(positionTwo) == null && getBoard().piece(positionThree) == null) {
                validMoves[position.getRow()][position.getColumn() - 2] = true;
            }
        }
    }

    /**
     * Verifica se é possível realizar um roque com a torre na posição fornecida.
     * Para que o roque seja possível, a torre deve ser da mesma cor que o rei e não deve ter se movido ainda.
     *
     * @param position A posição da torre a ser verificada.
     * @return Verdadeiro se o roque é possível, falso caso contrário.
     */
    private boolean testRookCastling(Position position) {
        var chessPiece = (ChessPiece) getBoard().piece(position);
        return Objects.nonNull(chessPiece) && chessPiece instanceof Rook && chessPiece.getColor().equals(getColor()) && chessPiece.getMoveCount() == 0;
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
