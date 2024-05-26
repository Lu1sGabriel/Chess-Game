package src.main.java.chess.exceptions;

import src.main.java.boardGame.exceptions.BoardException;

import java.io.Serial;

/**
 * Exceção específica para o jogo de xadrez.
 * Esta classe estende a {@link BoardException} para fornecer
 * mensagens de erro específicas para o contexto do xadrez.
 *
 * @serial Serializa a classe para permitir a transmissão de objetos
 * entre diferentes JVMs.
 */
public class ChessException extends BoardException {

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
    public ChessException(String message) {
        super(message);
    }

}

