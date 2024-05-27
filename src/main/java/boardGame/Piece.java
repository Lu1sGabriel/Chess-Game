package src.main.java.boardGame;

import java.util.Objects;

/**
 * Representa uma peça genérica do jogo de xadrez. Esta classe abstrata define comportamentos comuns
 * a todas as peças do jogo, como posição atual no tabuleiro, métodos para calcular movimentos possíveis
 * e verificação de disponibilidade de movimentos.
 */
public abstract class Piece {

    protected Position position;
    private final Board board;

    /**
     * Construtor que inicializa uma peça com o tabuleiro associado e posição nula.
     *
     * @param board O tabuleiro onde a peça está posicionada.
     * @throws NullPointerException Se o tabuleiro fornecido for nulo.
     */
    public Piece(final Board board) {
        this.board = Objects.requireNonNull(board, "O tabuleiro não pode ser nulo. ");
        position = null;
    }

    /**
     * Retorna o tabuleiro associado a esta peça.
     *
     * @return O tabuleiro associado a esta peça.
     */
    protected Board getBoard() {
        return board;
    }

    /**
     * Método abstrato que deve ser implementado por subclasses para calcular os movimentos possíveis da peça.
     *
     * @return Uma matriz booleana representando os movimentos possíveis da peça.
     */
    public abstract boolean[][] possibleMoves();

    /**
     * Verifica se a peça pode se mover para a posição especificada.
     *
     * @param position A posição para verificar se é um movimento possível.
     * @return true se a peça pode se mover para a posição especificada, false caso contrário.
     * @throws NullPointerException Se a posição fornecida for nula.
     */
    public boolean possibleMove(final Position position) {
        Objects.requireNonNull(position, "A posição não pode ser nula. ");
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    /**
     * Verifica se há pelo menos um movimento possível para a peça.
     *
     * @return true se há pelo menos um movimento possível, false caso contrário.
     */
    public boolean isThereAnyPossibleMove() {
        boolean[][] possibleMoves = possibleMoves();
        for (boolean[] row : possibleMoves) {
            for (boolean cell : row) {
                if (cell) {
                    return true;
                }
            }
        }
        return false;
    }

}