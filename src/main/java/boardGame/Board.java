package src.main.java.boardGame;

public class Board {
    //  Quantidade de linhas
    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Piece piece(int row, int column) {
        return pieces[row][column];
    }

    public Piece piece(Position position) {
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        //  A peça na posição do tabuleiro recebe a peça que está sendo movida.
        pieces[position.getRow()][position.getColumn()] = piece;
        //  A peça recebe a posição.
        piece.position = position;
    }
}
