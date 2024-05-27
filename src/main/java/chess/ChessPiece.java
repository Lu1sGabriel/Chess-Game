package src.main.java.chess;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Piece;
import src.main.java.boardGame.Position;

import java.util.Objects;

/**
 * Representa uma peça de xadrez abstrata que estende a classe Piece. Esta classe encapsula
 * funcionalidades comuns a todas as peças de xadrez, como cor, contagem de movimentos,
 * conversão de posição para posição de xadrez e verificação de peças adversárias.
 */
public abstract class ChessPiece extends Piece {

    /**
     * A cor do jogador que controla esta peça.
     */
    private final PlayerColor playerColor;

    /**
     * O número de movimentos que esta peça realizou.
     */
    private int moveCount;

    /**
     * Construtor que inicializa uma peça de xadrez com o tabuleiro associado e a cor do jogador.
     *
     * @param board       O tabuleiro onde a peça está posicionada.
     * @param playerColor A cor do jogador que controla esta peça.
     * @throws NullPointerException Se o tabuleiro ou a cor do jogador fornecidos forem nulos.
     */
    public ChessPiece(final Board board, final PlayerColor playerColor) {
        super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo. "));
        this.playerColor = Objects.requireNonNull(playerColor, "A cor não pode ser nula. ");
    }

    /**
     * Retorna a cor do jogador que controla esta peça.
     *
     * @return A cor do jogador.
     */
    public PlayerColor getColor() {
        return playerColor;
    }

    /**
     * Retorna o número de movimentos que esta peça realizou.
     *
     * @return O número de movimentos da peça.
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Converte a posição desta peça de uma posição padrão para uma posição de xadrez.
     *
     * @return A posição de xadrez desta peça.
     */
    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    /**
     * Verifica se há uma peça adversária na posição especificada.
     *
     * @param position A posição a ser verificada.
     * @return true se houver uma peça adversária na posição especificada, false caso contrário.
     */
    protected boolean isThereOpponentPiece(final Position position) {
        var piece = (ChessPiece) getBoard().piece(position);
        if (piece != null) {
            return piece.getColor() != playerColor;
        } else {
            return false;
        }
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