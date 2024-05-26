package src.main.java.chess;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Piece;
import src.main.java.boardGame.Position;
import src.main.java.chess.exceptions.ChessException;
import src.main.java.chess.pieces.Bishop;
import src.main.java.chess.pieces.King;
import src.main.java.chess.pieces.Pawn;
import src.main.java.chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChessMatch {

    private final Board board;
    private int turn;
    private Color currentPlayer;
    private final List<Piece> piecesOnTheBoard;
    private final List<Piece> capturedPieces;
    private boolean check;
    private boolean checkMate;

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        piecesOnTheBoard = new ArrayList<>();
        capturedPieces = new ArrayList<>();
        setupInitialPieces();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] pieceMatrix = new ChessPiece[board.getRows()][board.getColumns()];
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                ChessPiece currentPiece = (ChessPiece) board.piece(row, col);
                pieceMatrix[row][col] = currentPiece;
            }
        }
        return pieceMatrix;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    private void setupInitialPieces() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
    }

    private void placeNewPiece(final char column, final int row, final ChessPiece piece) {
        Objects.requireNonNull(piece, "É obrigatório a seleção de uma peça. ");
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    public ChessPiece performChessMove(final ChessPosition sourcePosition, final ChessPosition targetPosition) {
        var source = sourcePosition.toPosition();
        var target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        var capturedPiece = makeMove(source, target);
        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode se colocar em check! ");
        }
        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    public boolean[][] possibleMoves(final ChessPosition sourcePosition) {
        var position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    private void validateSourcePosition(final Position position) {
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

    private void validateTargetPosition(final Position source, final Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode mover-se para a posição de destino. ");
        }
    }

    private Piece makeMove(final Position source, final Position target) {
        var movingPiece = (ChessPiece) board.removePiece(source);
        movingPiece.increaseMoveCount();
        var capturedPiece = board.removePiece(target);
        board.placePiece(movingPiece, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        return capturedPiece;
    }

    private boolean testCheck(final Color color) {
        var kingPosition = kingColor(color).getChessPosition().toPosition();
        var opponentPieceList = piecesOnTheBoard.stream()
                .filter(pieces -> ((ChessPiece) pieces).getColor().equals(opponent(color))).toList();
        for (Piece piece : opponentPieceList) {
            boolean[][] matrix = piece.possibleMoves();
            if (matrix[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private ChessPiece kingColor(final Color color) {
        return (ChessPiece) piecesOnTheBoard.stream()
                .filter(pieces -> ((ChessPiece) pieces).getColor().equals(color))
                .filter(piece -> piece instanceof King)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Não existe o rei da cor: %s", color)));
    }

    private Color opponent(final Color color) {
        return switch (color) {
            case WHITE -> Color.BLACK;
            case BLACK -> Color.WHITE;
        };
    }

    private void undoMove(final Position source, final Position target, final Piece capturedPiece) {
        var piece = (ChessPiece) board.removePiece(target);
        piece.decreaseMoveCount();
        board.placePiece(piece, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

    }

    private void nextTurn() {
        turn++;
        currentPlayer = opponent(currentPlayer);
    }

    private boolean testCheckMate(final Color color) {
        Objects.requireNonNull(color, "Color cannot be null");

        if (!testCheck(color)) {
            return false;
        }

        List<Piece> pieceList = piecesOnTheBoard.stream()
                .filter(pieces -> ((ChessPiece) pieces).getColor().equals(color)).toList();

        return pieceList.stream().anyMatch(piece -> !canPieceAvoidCheck(piece, color));
    }

    private boolean canPieceAvoidCheck(final Piece piece, final Color color) {
        boolean[][] matrix = piece.possibleMoves();
        for (int row = 0; row < board.getRows(); row++) {
            for (int column = 0; column < board.getColumns(); column++) {
                if (matrix[row][column]) {
                    var source = ((ChessPiece) piece).getChessPosition().toPosition();
                    var target = new Position(row, column);
                    var capturedPiece = makeMove(source, target);
                    boolean testCheck = testCheck(color);
                    undoMove(source, target, capturedPiece);
                    if (!testCheck) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}