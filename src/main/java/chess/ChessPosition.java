package src.main.java.chess;

import src.main.java.boardGame.Position;
import src.main.java.chess.exceptions.ChessException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


/**
 * Representa uma posição no tabuleiro de xadrez, definida por uma coluna e uma linha.
 * <p>
 * Esta classe implementa Serializable para permitir que o estado do objeto
 * seja salvo e carregado de um arquivo, ou transmitido pela rede.
 */
public record ChessPosition(char column, int row) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final char MIN_COLUMN = 'a';
    private static final char MAX_COLUMN = 'h';
    private static final int MIN_ROW = 1;
    private static final int MAX_ROW = 8;

    /**
     * Constrói uma nova posição de xadrez com base na coluna e linha fornecidas.
     *
     * @param column A coluna da posição de xadrez.
     * @param row    A linha da posição de xadrez.
     * @throws ChessException Se a coluna ou linha fornecida não estiverem dentro dos limites válidos.
     */
    public ChessPosition {
        if (column < MIN_COLUMN || column > MAX_COLUMN || row < MIN_ROW || row > MAX_ROW) {
            throw new ChessException("Erro ao instanciar ChessPosition. Os valores válidos são de a1 a h8.");
        }
    }

    /**
     * Converte a posição de xadrez para uma posição interna do tabuleiro.
     *
     * @return A posição interna equivalente à posição de xadrez.
     */
    public Position toPosition() {
        return new Position(MAX_ROW - row, column - MIN_COLUMN);
    }

    /**
     * Converte uma posição interna do tabuleiro para uma posição de xadrez.
     *
     * @param position A posição interna do tabuleiro a ser convertida.
     * @return A posição de xadrez equivalente à posição interna fornecida.
     * @throws NullPointerException Se a posição fornecida for nula.
     */
    public static ChessPosition fromPosition(Position position) {
        Objects.requireNonNull(position, "A posição não pode ser nula.");
        return new ChessPosition((char) (MIN_COLUMN + position.getColumn()), MAX_ROW - position.getRow());
    }

    /**
     * Retorna a representação em string desta posição de xadrez no formato "coluna + linha".
     *
     * @return A representação em string da posição de xadrez.
     */
    @Override
    public String toString() {
        return String.format("%c%d", column, row);
    }

}