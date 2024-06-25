package src.main.java.chess;

import src.main.java.boardGame.Board;
import src.main.java.boardGame.Piece;
import src.main.java.boardGame.Position;
import src.main.java.chess.exceptions.ChessException;
import src.main.java.chess.pieces.*;
import src.main.java.utils.ChessLogUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChessMatch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Board board;
    private int turn;
    private PlayerColor currentPlayer;
    private final List<Piece> piecesOnTheBoard;
    private final List<Piece> capturedPieces;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;
    private final String matchId;

    public ChessMatch() {
        this.board = new Board(8, 8);
        this.turn = 1;
        this.currentPlayer = PlayerColor.WHITE;
        this.piecesOnTheBoard = new ArrayList<>();
        this.capturedPieces = new ArrayList<>();
        this.matchId = UUID.randomUUID().toString();
        setupInitialPieces();
    }

    public int getTurn() {
        return turn;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] pieceMatrix = new ChessPiece[board.getRows()][board.getColumns()];
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                pieceMatrix[row][col] = (ChessPiece) board.piece(row, col);
            }
        }
        return pieceMatrix;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    private void setupInitialPieces() {
        PlayerColor[] colors = {PlayerColor.WHITE, PlayerColor.BLACK};
        int[][] initialPositions = {
                {1, 2},
                {8, 7}
        };

        Class<?>[][] pieceTypes = {
                {Rook.class, Knight.class, Bishop.class, Queen.class, King.class, Bishop.class, Knight.class, Rook.class},
                {Pawn.class, Pawn.class, Pawn.class, Pawn.class, Pawn.class, Pawn.class, Pawn.class, Pawn.class}
        };

        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    if (pieceTypes[0][j] == King.class) {
                        placeNewPiece((char) ('a' + j), initialPositions[i][0], new King(board, colors[i], this));
                    } else {
                        placeNewPiece((char) ('a' + j), initialPositions[i][0], (ChessPiece) pieceTypes[0][j].getConstructor(Board.class, PlayerColor.class).newInstance(board, colors[i]));
                    }
                    placeNewPiece((char) ('a' + j), initialPositions[i][1], new Pawn(board, colors[i], this));
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao criar peça inicial.", e);
                }
            }
        }
    }

    private void placeNewPiece(final char column, final int row, final ChessPiece piece) {
        Objects.requireNonNull(piece, "É obrigatório a seleção de uma peça.");
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    public ChessPiece performChessMove(final ChessPosition sourcePosition, final ChessPosition targetPosition, String pieceType) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (isCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode se colocar em check!");
        }

        ChessLogUtil.logMove(matchId, currentPlayer, sourcePosition, targetPosition, (ChessPiece) board.piece(target));

        var movedPiece = (ChessPiece) board.piece(target);
        handlePromotion(target, movedPiece, pieceType);

        updateCheckAndCheckMateStatus();
        updateEnPassantVulnerability(source, target, movedPiece);

        return (ChessPiece) capturedPiece;
    }

    private void handlePromotion(Position target, ChessPiece movedPiece, String pieceType) {
        if (movedPiece instanceof Pawn && isPawnPromotable(target, movedPiece.getColor())) {
            promoted = movedPiece;
            replacePromotedPiece(pieceType);
        }
    }

    private boolean isPawnPromotable(Position target, PlayerColor color) {
        return (color == PlayerColor.WHITE && target.getRow() == 0) ||
                (color == PlayerColor.BLACK && target.getRow() == 7);
    }

    private void updateCheckAndCheckMateStatus() {
        check = isCheck(opponent(currentPlayer));
        checkMate = isCheckMate(opponent(currentPlayer));
        if (!checkMate) {
            nextTurn();
        }
    }

    private void updateEnPassantVulnerability(Position source, Position target, ChessPiece movedPiece) {
        if (movedPiece instanceof Pawn && Math.abs(target.getRow() - source.getRow()) == 2) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }
    }

    public boolean isPromotionPossible(ChessPosition source, ChessPosition target) {
        Piece piece = board.piece(source.toPosition());
        return piece instanceof Pawn && (target.row() == 1 || target.row() == 8);
    }

    public void replacePromotedPiece(String pieceType) {
        if (promoted == null) {
            throw new IllegalStateException("Não há peça a ser promovida.");
        }

        if (!isValidPromotionPieceType(pieceType)) {
            return;
        }

        Position position = promoted.getChessPosition().toPosition();
        board.removePiece(position); // Remover a peça antiga

        ChessPiece newPiece = createPromotedPiece(pieceType, promoted.getColor());
        newPiece.setPosition(position); // Set the position for the new piece
        board.placePiece(newPiece, position);
        piecesOnTheBoard.remove(promoted);
        piecesOnTheBoard.add(newPiece);

        promoted = null; // Reset promoted after promotion
    }

    private boolean isValidPromotionPieceType(String pieceType) {
        return pieceType.equals("B") || pieceType.equals("N") || pieceType.equals("R") || pieceType.equals("Q");
    }

    private ChessPiece createPromotedPiece(String pieceType, PlayerColor color) {
        return switch (pieceType) {
            case "Q" -> new Queen(board, color);
            case "R" -> new Rook(board, color);
            case "B" -> new Bishop(board, color);
            case "N" -> new Knight(board, color);
            default -> throw new IllegalArgumentException("Tipo de peça inválido para promoção: " + pieceType);
        };
    }

    public boolean[][] possibleMoves(final ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    private void validateSourcePosition(final Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não há peça na posição de origem.");
        }
        ChessPiece piece = (ChessPiece) board.piece(position);
        if (currentPlayer != piece.getColor()) {
            throw new ChessException("A peça escolhida não é sua.");
        }
        if (!piece.isThereAnyPossibleMove()) {
            throw new ChessException("A peça escolhida não tem movimentos possíveis.");
        }
    }

    private void validateTargetPosition(final Position source, final Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode mover-se para a posição de destino.");
        }
    }

    private Piece makeMove(final Position source, final Position target) {
        ChessPiece movingPiece = (ChessPiece) board.removePiece(source);
        movingPiece.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        if (capturedPiece != null) {
            capturedPiece.setPosition(target);  // Garanta que a posição da peça capturada é preservada
        }
        board.placePiece(movingPiece, target);

        handleSpecialMoves(source, target, movingPiece, capturedPiece);

        return capturedPiece;
    }

    private void handleSpecialMoves(Position source, Position target, ChessPiece movingPiece, Piece capturedPiece) {
        if (movingPiece instanceof King) {
            handleCastling(source, target);
        }

        if (movingPiece instanceof Pawn) {
            handleEnPassant(source, target, movingPiece, capturedPiece);
        }
    }

    private void handleCastling(Position source, Position target) {
        if (Math.abs(target.getColumn() - source.getColumn()) == 2) {
            Position rookSource = target.getColumn() == source.getColumn() + 2 ?
                    new Position(source.getRow(), source.getColumn() + 3) :
                    new Position(source.getRow(), source.getColumn() - 4);

            Position rookTarget = target.getColumn() == source.getColumn() + 2 ?
                    new Position(source.getRow(), source.getColumn() + 1) :
                    new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.removePiece(rookSource);
            board.placePiece(rook, rookTarget);
            rook.increaseMoveCount();
        }
    }

    private void handleEnPassant(Position source, Position target, ChessPiece movingPiece, Piece capturedPiece) {
        if (source.getColumn() != target.getColumn() && capturedPiece == null) {
            Position pawnPosition = movingPiece.getColor() == PlayerColor.WHITE ?
                    new Position(target.getRow() + 1, target.getColumn()) :
                    new Position(target.getRow() - 1, target.getColumn());
            capturedPiece = board.removePiece(pawnPosition);
            capturedPieces.add(capturedPiece);
            piecesOnTheBoard.remove(capturedPiece);
        }
    }

    private boolean isCheck(final PlayerColor playerColor) {
        Position kingPosition = findKing(playerColor).getChessPosition().toPosition();
        return piecesOnTheBoard.stream()
                .filter(piece -> ((ChessPiece) piece).getColor() == opponent(playerColor))
                .map(Piece::possibleMoves)
                .anyMatch(moves -> moves[kingPosition.getRow()][kingPosition.getColumn()]);
    }

    private ChessPiece findKing(final PlayerColor playerColor) {
        return (ChessPiece) piecesOnTheBoard.stream()
                .filter(piece -> ((ChessPiece) piece).getColor() == playerColor)
                .filter(piece -> piece instanceof King)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Não existe o rei da cor: %s", playerColor)));
    }

    private PlayerColor opponent(final PlayerColor playerColor) {
        return playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    private void undoMove(final Position source, final Position target, Piece capturedPiece) {
        ChessPiece piece = (ChessPiece) board.removePiece(target);
        piece.decreaseMoveCount();
        board.placePiece(piece, source);

        if (capturedPiece != null) {
            capturedPiece.setPosition(target); // Restore position for the captured piece
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        undoSpecialMoves(source, target, piece);
    }

    private void undoSpecialMoves(Position source, Position target, ChessPiece piece) {
        if (piece instanceof King) {
            undoCastling(source, target);
        }

        if (piece instanceof Pawn) {
            undoEnPassant(source, target, piece);
        }
    }

    private void undoCastling(Position source, Position target) {
        if (Math.abs(target.getColumn() - source.getColumn()) == 2) {
            Position rookSource = target.getColumn() == source.getColumn() + 2 ?
                    new Position(source.getRow(), source.getColumn() + 3) :
                    new Position(source.getRow(), source.getColumn() - 4);

            Position rookTarget = target.getColumn() == source.getColumn() + 2 ?
                    new Position(source.getRow(), source.getColumn() + 1) :
                    new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.removePiece(rookTarget);
            board.placePiece(rook, rookSource);
            rook.decreaseMoveCount();
        }
    }

    private void undoEnPassant(Position source, Position target, ChessPiece piece) {
        if (source.getColumn() != target.getColumn() && enPassantVulnerable != null) {
            ChessPiece pawn = (ChessPiece) board.removePiece(target);
            Position pawnPosition = piece.getColor() == PlayerColor.WHITE ?
                    new Position(3, target.getColumn()) :
                    new Position(4, target.getColumn());
            board.placePiece(pawn, pawnPosition);
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = opponent(currentPlayer);
    }

    private boolean isCheckMate(final PlayerColor playerColor) {
        if (!isCheck(playerColor)) {
            return false;
        }

        return piecesOnTheBoard.stream()
                .filter(piece -> ((ChessPiece) piece).getColor() == playerColor)
                .noneMatch(piece -> canPieceAvoidCheck(piece, playerColor));
    }

    private boolean canPieceAvoidCheck(final Piece piece, final PlayerColor playerColor) {
        boolean[][] possibleMoves = piece.possibleMoves();
        for (int row = 0; row < board.getRows(); row++) {
            for (int column = 0; column < board.getColumns(); column++) {
                if (possibleMoves[row][column]) {
                    Position source = ((ChessPiece) piece).getChessPosition().toPosition();
                    Position target = new Position(row, column);
                    Piece capturedPiece = makeMove(source, target);
                    boolean checkAfterMove = isCheck(playerColor);
                    undoMove(source, target, capturedPiece);
                    if (!checkAfterMove) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}