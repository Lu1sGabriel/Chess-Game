package src.main.java.boardGame;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Classe que representa uma peça genérica em um jogo de tabuleiro.
 * Gerencia a posição da peça no tabuleiro e fornece métodos para manipular e consultar essa posição.
 * <p>
 * Esta classe implementa Serializable para permitir que o estado do objeto
 * seja salvo e carregado de um arquivo, ou transmitido pela rede.
 */
public abstract class Piece implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected Position position;
    private final Board board;

    /**
     * Construtor que inicializa uma peça com o tabuleiro associado.
     *
     * @param board O tabuleiro onde a peça está posicionada.
     * @throws NullPointerException Se o tabuleiro fornecido for nulo.
     */
    public Piece(Board board) {
        this.board = Objects.requireNonNull(board, "O tabuleiro não pode ser nulo.");
        position = null;  // Posição inicial é nula
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Método abstrato que deve ser implementado por subclasses para definir os movimentos possíveis da peça.
     *
     * @return Uma matriz booleana indicando os movimentos possíveis.
     */
    public abstract boolean[][] possibleMoves();

    /**
     * Verifica se a peça pode mover-se para a posição fornecida.
     *
     * @param position A posição a ser verificada.
     * @return true se a peça pode mover-se para a posição fornecida, false caso contrário.
     */
    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    /**
     * Verifica se a peça tem pelo menos um movimento possível.
     *
     * @return true se a peça tem pelo menos um movimento possível, false caso contrário.
     */
    public boolean isThereAnyPossibleMove() {
        boolean[][] moves = possibleMoves();
        for (boolean[] row : moves) {
            for (boolean move : row) {
                if (move) {
                    return true;
                }
            }
        }
        return false;
    }

}