package src.main.java.chess;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Piece;
import src.main.java.boardGame.Position;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa uma peça de xadrez abstrata que estende a classe Piece.
 * Esta classe encapsula funcionalidades comuns a todas as peças de xadrez,
 * como cor, contagem de movimentos, conversão de posição para posição de xadrez e verificação de peças adversárias.
 * <p>
 * Esta classe implementa Serializable para permitir que o estado do objeto
 * seja salvo e carregado de um arquivo, ou transmitido pela rede.
 */
public abstract class ChessPiece extends Piece implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final PlayerColor playerColor;
    private int moveCount;

    /**
     * Construtor que inicializa uma peça de xadrez com o tabuleiro associado e a cor do jogador.
     *
     * @param board       O tabuleiro onde a peça está posicionada.
     * @param playerColor A cor do jogador que controla esta peça.
     * @throws NullPointerException Se o tabuleiro ou a cor do jogador fornecidos forem nulos.
     */
    public ChessPiece(Board board, PlayerColor playerColor) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo."));
        this.playerColor = Objects.requireNonNull(playerColor, "A cor não pode ser nula.");
    }

    /**
     * Retorna a cor do jogador que controla esta peça.
     *
     * @return A cor do jogador que controla esta peça.
     */
    public PlayerColor getColor() {
        return playerColor;
    }

    /**
     * Retorna o número de movimentos que esta peça já fez.
     *
     * @return O número de movimentos que esta peça já fez.
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Retorna a posição de xadrez desta peça.
     *
     * @return A posição de xadrez desta peça.
     */
    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    /**
     * Verifica se há uma peça adversária na posição fornecida.
     *
     * @param position A posição a ser verificada.
     * @return true se houver uma peça adversária na posição fornecida, false caso contrário.
     */
    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece piece = (ChessPiece) getBoard().piece(position);
        return piece != null && piece.getColor() != playerColor;
    }

    /**
     * Incrementa o contador de movimentos desta peça.
     */
    public void increaseMoveCount() {
        moveCount++;
    }

    /**
     * Decrementa o contador de movimentos desta peça.
     */
    public void decreaseMoveCount() {
        moveCount--;
    }

}