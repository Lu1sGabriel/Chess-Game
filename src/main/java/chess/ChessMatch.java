package src.main.java.chess;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Piece;
import src.main.java.boardGame.Position;
import src.main.java.chess.exceptions.ChessException;
import src.main.java.chess.pieces.King;
import src.main.java.chess.pieces.Rook;

public class ChessMatch {

    private final Board board;

    private int turn;

    private Color currentPlayer;

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        setupInitialPieces();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    private void setupInitialPieces() {
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    public ChessPiece[][] getPieces() {
        var pieceMatrix = new ChessPiece[board.getRows()][board.getColumns()];
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                ChessPiece currentPiece = (ChessPiece) board.piece(row, col);
                pieceMatrix[row][col] = currentPiece;
            }
        }
        return pieceMatrix;
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        var source = sourcePosition.toPosition();
        var target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        var capturedPiece = executeMove(source, target);
        nextTurn();
        return (ChessPiece) capturedPiece;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        var position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não há peça na posição de origem. ");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida não é sua. ");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("A peça escolhida não tem movimentos possíveis. ");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode mover-se para a posição de destino. ");
        }
    }

    private Piece executeMove(Position source, Position target) {
        var movingPiece = board.removePiece(source);
        var capturedPiece = board.removePiece(target);
        board.placePiece(movingPiece, target);
        return capturedPiece;
    }

    private void nextTurn() {
        turn++;
        if (currentPlayer == Color.WHITE) {
            currentPlayer = Color.BLACK;
        } else {
            currentPlayer = Color.WHITE;
        }
    }

}