package src.main.java.chess;

/**
 * Enumeração para representar as cores dos jogadores em um jogo de xadrez.
 * As cores possíveis são preto (BLACK) e branco (WHITE).
 */
public enum PlayerColor {

    /**
     * Representa a cor preta.
     */
    BLACK,

    /**
     * Representa a cor branca.
     */
    WHITE;

    /**
     * Retorna a cor oposta à cor atual.
     *
     * @return a cor oposta
     */
    public PlayerColor opponent() {
        return this == WHITE ? BLACK : WHITE;
    }

}