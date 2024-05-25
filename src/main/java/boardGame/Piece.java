package src.main.java.boardGame;

public abstract class Piece {
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

    //  Hook Methods
    public boolean possibleMovie(Position position) {
        return possibleMovies()[position.getRow()][position.getColumn()];
    }


    public boolean isThereAnyPossibleMove() {
        var matrix = possibleMovies();
        for (boolean[] booleans : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                if (booleans[j]) {
                    return true;
                }
            }
        }
        return false;
    }


    public abstract boolean[][] possibleMovies();

}
