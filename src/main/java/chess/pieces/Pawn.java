package src.main.java.chess.pieces;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Position;
import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.PlayerColor;

import java.util.Objects;

/**
 * Classe Pawn que representa um peão em um jogo de xadrez.
 * Esta classe herda de {@link ChessPiece}.
 * O peão tem movimentos específicos, como mover-se para frente, capturar peças na diagonal
 * e realizar o movimento especial "en passant".
 */
public class Pawn extends ChessPiece {

    /**
     * Partida de xadrez associada a este peão.
     */
    private final ChessMatch chessMatch;

    /**
     * Offsets usados para calcular movimentos diagonais.
     */
    private static final int[] DIAGONAL_OFF_SETS = {-1, 1};

    /**
     * Direção do movimento para peões brancos.
     */
    private static final int WHITE_DIRECTION = -1;

    /**
     * Direção do movimento para peões negros.
     */
    private static final int BLACK_DIRECTION = 1;

    /**
     * Linha inicial de movimento duplo para peões brancos.
     */
    private static final int WHITE_INITIAL_MOVE_ROW = -2;

    /**
     * Linha inicial de movimento duplo para peões negros.
     */
    private static final int BLACK_INITIAL_MOVE_ROW = 2;

    /**
     * Linha onde pode ocorrer "en passant" para peões brancos.
     */
    private static final int WHITE_EN_PASSANT_ROW = 3;

    /**
     * Linha onde pode ocorrer "en passant" para peões negros.
     */
    private static final int BLACK_EN_PASSANT_ROW = 4;

    /**
     * Constrói um peão com o tabuleiro, cor do jogador e partida de xadrez especificados.
     *
     * @param board       O tabuleiro onde o peão está localizado.
     * @param playerColor A cor do jogador (branco ou preto).
     * @param chessMatch  A partida de xadrez associada a este peão.
     * @throws NullPointerException se o tabuleiro ou a cor do jogador forem nulos.
     */
    public Pawn(final Board board, final PlayerColor playerColor, ChessMatch chessMatch) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo."),
                Objects.requireNonNull(playerColor, "A cor não pode ser nula."));
        this.chessMatch = chessMatch;
    }

    /**
     * Calcula os movimentos possíveis para este peão no tabuleiro atual.
     *
     * @return Uma matriz booleana indicando as posições válidas para movimento.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] validMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        if (getColor().equals(PlayerColor.WHITE)) {
            calculatePawnMoves(validMoves, WHITE_DIRECTION, WHITE_INITIAL_MOVE_ROW, WHITE_EN_PASSANT_ROW);
        } else {
            calculatePawnMoves(validMoves, BLACK_DIRECTION, BLACK_INITIAL_MOVE_ROW, BLACK_EN_PASSANT_ROW);
        }

        return validMoves;
    }

    /**
     * Calcula os movimentos possíveis para o peão considerando direção, movimento inicial e "en passant".
     *
     * @param validMoves     A matriz de movimentos válidos a ser preenchida.
     * @param direction      A direção do movimento do peão (para cima ou para baixo).
     * @param initialMoveRow A linha para o movimento inicial duplo.
     * @param enPassantRow   A linha onde o movimento "en passant" é possível.
     */
    private void calculatePawnMoves(final boolean[][] validMoves, final int direction, final int initialMoveRow, final int enPassantRow) {
        checkVerticalMove(validMoves, direction);
        checkInitialMove(validMoves, initialMoveRow, direction);
        checkDiagonalMoves(validMoves, direction, enPassantRow);
    }

    /**
     * Verifica e marca os movimentos verticais válidos para o peão.
     *
     * @param validMoves A matriz de movimentos válidos a ser preenchida.
     * @param direction  A direção do movimento do peão (para cima ou para baixo).
     */
    private void checkVerticalMove(final boolean[][] validMoves, final int direction) {
        var currentPosition = new Position(position.getRow() + direction, position.getColumn());
        if (getBoard().positionExists(currentPosition) && !getBoard().thereIsAPiece(currentPosition)) {
            validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
        }
    }

    /**
     * Verifica e marca o movimento inicial duplo válido para o peão.
     *
     * @param validMoves            A matriz de movimentos válidos a ser preenchida.
     * @param initialMoveRow        A linha para o movimento inicial duplo.
     * @param freePositionDirection A direção do movimento para verificar posições livres.
     */
    private void checkInitialMove(final boolean[][] validMoves, final int initialMoveRow, final int freePositionDirection) {
        if (getMoveCount() == 0) {
            var currentPosition = new Position(position.getRow() + initialMoveRow, position.getColumn());
            var positionFree = new Position(position.getRow() + freePositionDirection, position.getColumn());
            if (getBoard().positionExists(currentPosition) && !getBoard().thereIsAPiece(currentPosition)
                    && getBoard().positionExists(positionFree) && !getBoard().thereIsAPiece(positionFree)) {
                validMoves[currentPosition.getRow()][currentPosition.getColumn()] = true;
            }
        }
    }

    /**
     * Verifica e marca os movimentos diagonais válidos para captura de peças e "en passant".
     *
     * @param validMoves   A matriz de movimentos válidos a ser preenchida.
     * @param direction    A direção do movimento do peão (para cima ou para baixo).
     * @param enPassantRow A linha onde o movimento "en passant" é possível.
     */
    private void checkDiagonalMoves(final boolean[][] validMoves, final int direction, final int enPassantRow) {
        for (int offset : DIAGONAL_OFF_SETS) {
            var diagonalCapture = new Position(position.getRow() + direction, position.getColumn() + offset);
            if (getBoard().positionExists(diagonalCapture) && isThereOpponentPiece(diagonalCapture)) {
                validMoves[diagonalCapture.getRow()][diagonalCapture.getColumn()] = true;
            }

            checkEnPassant(validMoves, direction, offset, enPassantRow);
        }
    }

    /**
     * Verifica e marca o movimento "en passant" válido para o peão.
     *
     * @param validMoves   A matriz de movimentos válidos a ser preenchida.
     * @param direction    A direção do movimento do peão (para cima ou para baixo).
     * @param offset       O offset para o movimento diagonal.
     * @param enPassantRow A linha onde o movimento "en passant" é possível.
     */
    private void checkEnPassant(final boolean[][] validMoves, final int direction, final int offset, final int enPassantRow) {
        if (position.getRow() == enPassantRow) {
            var adjacentPawnPosition = new Position(position.getRow(), position.getColumn() + offset);
            var enPassantCapturePosition = new Position(position.getRow() + direction, position.getColumn() + offset);
            if (getBoard().positionExists(adjacentPawnPosition)
                    && getBoard().positionExists(enPassantCapturePosition)
                    && getBoard().piece(adjacentPawnPosition) != null
                    && getBoard().piece(adjacentPawnPosition) == chessMatch.getEnPassantVulnerable()) {
                validMoves[enPassantCapturePosition.getRow()][enPassantCapturePosition.getColumn()] = true;
            }
        }
    }

    /**
     * Retorna a representação em string deste peão.
     *
     * @return Uma string "P" que representa o Peão.
     */
    @Override
    public String toString() {
        return "Pawn";
    }

}