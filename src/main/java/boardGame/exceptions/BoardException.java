package src.main.java.boardGame.exceptions;

import java.io.Serial;

/**
 * Exceção geral para erros relacionados ao tabuleiro de jogo.
 * Esta classe estende {@link RuntimeException} para fornecer
 * mensagens de erro específicas para problemas relacionados ao tabuleiro.
 */
public class BoardException extends RuntimeException {

    /**
     * Número de versão para serialização da classe.
     * Utilizado para verificar a compatibilidade durante a desserialização.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que aceita uma mensagem de erro específica.
     *
     * @param message A mensagem detalhada do erro.
     */
    public BoardException(String message) {
        super(message);
    }

}