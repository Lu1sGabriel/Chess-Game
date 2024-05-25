package src.main.java.boardGame;

public class Piece {
    protected Position position;
    private final Board board;

    public Piece(Board board) {
        this.board = board;
        // Por padrão o Java já coloca o valor de uma variável como null.
        // Apenas coloquei aqui por questão de clareza.
        this.position = null;
    }

    protected Board getBoard() {
        return board;
    }

    protected void setPosition(Position position) {
        this.position = position;
    }
}
