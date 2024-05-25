package src.main.java.boardGame.exception;

import java.io.Serial;

public class BoardException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BoardException(String message) {
        super(message);
    }
}
