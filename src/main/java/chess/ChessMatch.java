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

/**
 * Representa uma partida de xadrez. Esta classe gerencia o estado do jogo,
 * incluindo o tabuleiro, as peças, o jogador atual, e as condições de check e checkmate.
 * <p>
 * Esta classe implementa Serializable para permitir que o estado do objeto
 * seja salvo e carregado de um arquivo, ou transmitido pela rede.
 */
public class ChessMatch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Board board;
    private PlayerColor currentPlayer;
    private final List<Piece> piecesOnTheBoard;
    private final List<Piece> capturedPieces;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;
    private final String matchId;

    /**
     * Construtor padrão que inicializa uma nova partida de xadrez com o tabuleiro padrão,
     * define o jogador atual como branco e configura as peças iniciais.
     */
    public ChessMatch() {
        board = new Board(8, 8);
        currentPlayer = PlayerColor.WHITE;
        piecesOnTheBoard = new ArrayList<>();
        capturedPieces = new ArrayList<>();
        this.matchId = UUID.randomUUID().toString();
        setupInitialPieces();
    }

    /**
     * Retorna a cor do jogador atual.
     *
     * @return A cor do jogador atual.
     */
    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Retorna a matriz de peças no tabuleiro.
     *
     * @return Uma matriz bidimensional de peças de xadrez representando o estado atual do tabuleiro.
     */
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

    /**
     * Retorna o estado de check atual.
     *
     * @return true se o jogador atual está em check, caso contrário, false.
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * Retorna o estado de checkmate atual.
     *
     * @return true se o jogador atual está em checkmate, caso contrário, false.
     */
    public boolean isCheckMate() {
        return checkMate;
    }

    /**
     * Retorna a peça vulnerável a captura en passant.
     *
     * @return A peça vulnerável a captura en passant.
     */
    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    /**
     * Realiza um movimento de xadrez de uma posição fonte para uma posição alvo,
     * incluindo a possibilidade de promoção de peões.
     *
     * @param sourcePosition A posição de origem da peça a ser movida.
     * @param targetPosition A posição de destino da peça a ser movida.
     * @param pieceType      O tipo de peça para promoção (caso aplicável).
     * @throws ChessException Se o movimento colocar o jogador atual em check.
     */
    public void performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition, String pieceType) {
        var source = sourcePosition.toPosition();
        var target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        var capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode se colocar em check!");
        }

        if (Objects.nonNull(capturedPiece)) {
            ChessLogUtil.logMove(matchId, currentPlayer, sourcePosition, targetPosition, (ChessPiece) board.piece(target));
            ChessLogUtil.logCapture(matchId, currentPlayer, (ChessPiece) capturedPiece, targetPosition);
        } else {
            ChessLogUtil.logMove(matchId, currentPlayer, sourcePosition, targetPosition, (ChessPiece) board.piece(target));
        }

        var movedPiece = (ChessPiece) board.piece(target);

        handlePromotion(target, movedPiece, pieceType);
        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
            ChessLogUtil.logWin(matchId, currentPlayer.toString());
        } else {
            nextTurn();
        }

        updateEnPassantVulnerability(source, target, movedPiece);
    }

    /**
     * Verifica se é possível promover um peão na posição alvo.
     *
     * @param source A posição de origem do peão.
     * @param target A posição de destino do peão.
     * @return true se a promoção é possível, caso contrário, false.
     */
    public boolean isPromotionPossible(ChessPosition source, ChessPosition target) {
        var piece = board.piece(source.toPosition());
        return piece instanceof Pawn && (target.row() == 1 || target.row() == 8);
    }

    /**
     * Substitui a peça promovida pelo tipo especificado.
     *
     * @param pieceType O tipo de peça para a promoção.
     * @throws IllegalStateException Se não há peça a ser promovida.
     */
    public void replacePromotedPiece(String pieceType) {
        if (promoted == null) {
            throw new IllegalStateException("Não há peça a ser promovida.");
        }

        if (!isValidPromotionPieceType(pieceType)) {
            return;
        }

        var position = promoted.getChessPosition().toPosition();
        board.removePiece(position);

        var newPiece = createPromotedPiece(pieceType, promoted.getColor());
        newPiece.setPosition(position);
        board.placePiece(newPiece, position);
        piecesOnTheBoard.remove(promoted);
        piecesOnTheBoard.add(newPiece);

        promoted = null;
    }

    /**
     * Retorna uma matriz booleana dos movimentos possíveis da peça na posição fornecida.
     *
     * @param sourcePosition A posição da peça para verificar movimentos possíveis.
     * @return Uma matriz booleana indicando movimentos possíveis.
     */
    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        var position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    /**
     * Configura as peças iniciais no tabuleiro.
     */
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
                } catch (Exception exception) {
                    throw new RuntimeException("Erro ao criar peça inicial.", exception);
                }
            }
        }
    }

    /**
     * Coloca uma nova peça no tabuleiro na coluna e linha especificadas.
     *
     * @param column A coluna onde a peça será colocada.
     * @param row    A linha onde a peça será colocada.
     * @param piece  A peça a ser colocada.
     */
    private void placeNewPiece(final char column, final int row, final ChessPiece piece) {
        Objects.requireNonNull(piece, "É obrigatório a seleção de uma peça.");
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    /**
     * Lida com a promoção de peões, substituindo a peça promovida pelo tipo especificado.
     *
     * @param target     A posição de destino do peão.
     * @param movedPiece A peça movida.
     * @param pieceType  O tipo de peça para a promoção.
     */
    private void handlePromotion(Position target, ChessPiece movedPiece, String pieceType) {
        if (movedPiece instanceof Pawn && isPawnPromotable(target, movedPiece.getColor())) {
            promoted = movedPiece;
            ChessLogUtil.logPromotion(matchId, currentPlayer, pieceType, ChessPosition.fromPosition(target));
            replacePromotedPiece(pieceType);
        }
    }

    /**
     * Verifica se um peão é promovível com base na posição de destino e na cor do peão.
     *
     * @param target A posição de destino do peão.
     * @param color  A cor do peão.
     * @return true se o peão é promovível, caso contrário, false.
     */
    private boolean isPawnPromotable(Position target, PlayerColor color) {
        return (color == PlayerColor.WHITE && target.getRow() == 0) || (color == PlayerColor.BLACK && target.getRow() == 7);
    }

    /**
     * Verifica se o tipo de peça fornecido é válido para promoção.
     *
     * @param pieceType O tipo de peça a ser verificado.
     * @return true se o tipo de peça é válido, caso contrário, false.
     */
    private boolean isValidPromotionPieceType(String pieceType) {
        return pieceType.equals("Bishop") || pieceType.equals("Knight") || pieceType.equals("Rook") || pieceType.equals("Queen");
    }

    /**
     * Cria uma nova peça promovida com base no tipo especificado.
     *
     * @param pieceType O tipo de peça para a promoção.
     * @param color     A cor da peça promovida.
     * @return A nova peça promovida.
     */
    private ChessPiece createPromotedPiece(String pieceType, PlayerColor color) {
        return switch (pieceType) {
            case "Queen" -> new Queen(board, color);
            case "Rook" -> new Rook(board, color);
            case "Bishop" -> new Bishop(board, color);
            case "Knight" -> new Knight(board, color);
            default -> throw new IllegalArgumentException("Tipo de peça inválido para promoção: " + pieceType);
        };
    }

    /**
     * Atualiza a vulnerabilidade de captura en passant para a peça movida, se aplicável.
     *
     * @param source     A posição de origem da peça movida.
     * @param target     A posição de destino da peça movida.
     * @param movedPiece A peça movida.
     */
    private void updateEnPassantVulnerability(Position source, Position target, ChessPiece movedPiece) {
        if (movedPiece instanceof Pawn && Math.abs(target.getRow() - source.getRow()) == 2) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }
    }

    /**
     * Valida a posição de origem, verificando se há uma peça na posição e se pertence ao jogador atual.
     *
     * @param position A posição de origem a ser validada.
     * @throws ChessException Se a posição de origem for inválida.
     */
    private void validateSourcePosition(final Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não há peça na posição de origem.");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida não é sua.");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("A peça escolhida não tem movimentos possíveis.");
        }
    }

    /**
     * Valida a posição de destino, verificando se o movimento é possível.
     *
     * @param source A posição de origem da peça.
     * @param target A posição de destino da peça.
     * @throws ChessException Se a posição de destino for inválida.
     */
    private void validateTargetPosition(final Position source, final Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode mover-se para a posição de destino.");
        }
    }

    /**
     * Realiza o movimento da peça no tabuleiro.
     *
     * @param source A posição de origem da peça.
     * @param target A posição de destino da peça.
     * @return A peça capturada, se houver.
     */
    private Piece makeMove(final Position source, final Position target) {
        var movingPiece = (ChessPiece) board.removePiece(source);
        movingPiece.increaseMoveCount();
        var capturedPiece = board.removePiece(target);
        board.placePiece(movingPiece, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        handleSpecialMoves(source, target, movingPiece, capturedPiece);

        return capturedPiece;
    }

    /**
     * Lida com movimentos especiais, como roque e captura en passant.
     *
     * @param source        A posição de origem da peça.
     * @param target        A posição de destino da peça.
     * @param movingPiece   A peça movida.
     * @param capturedPiece A peça capturada, se houver.
     */
    private void handleSpecialMoves(Position source, Position target, ChessPiece movingPiece, Piece capturedPiece) {
        if (movingPiece instanceof King) {
            handleCastling(source, target);
        }

        if (movingPiece instanceof Pawn) {
            handleEnPassant(source, target, movingPiece, capturedPiece);
        }
    }

    /**
     * Lida com o movimento de roque.
     *
     * @param source A posição de origem do rei.
     * @param target A posição de destino do rei.
     */
    private void handleCastling(Position source, Position target) {
        if (Math.abs(target.getColumn() - source.getColumn()) == 2) {
            var rookSource = target.getColumn() == source.getColumn() + 2 ?
                    new Position(source.getRow(), source.getColumn() + 3) :
                    new Position(source.getRow(), source.getColumn() - 4);

            var rookTarget = target.getColumn() == source.getColumn() + 2 ?
                    new Position(source.getRow(), source.getColumn() + 1) :
                    new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.removePiece(rookSource);
            board.placePiece(rook, rookTarget);
            rook.increaseMoveCount();
        }
    }

    /**
     * Lida com a captura en passant.
     *
     * @param source        A posição de origem do peão.
     * @param target        A posição de destino do peão.
     * @param movingPiece   A peça movida.
     * @param capturedPiece A peça capturada, se houver.
     */
    private void handleEnPassant(Position source, Position target, ChessPiece movingPiece, Piece capturedPiece) {
        if (source.getColumn() != target.getColumn() && capturedPiece == null) {
            var pawnPosition = movingPiece.getColor() == PlayerColor.WHITE ?
                    new Position(target.getRow() + 1, target.getColumn()) :
                    new Position(target.getRow() - 1, target.getColumn());
            capturedPiece = board.removePiece(pawnPosition);
            capturedPieces.add(capturedPiece);
            piecesOnTheBoard.remove(capturedPiece);
        }
    }

    /**
     * Testa se o jogador atual está em check.
     *
     * @param playerColor A cor do jogador a ser testada.
     * @return true se o jogador está em check, caso contrário, false.
     */
    private boolean testCheck(final PlayerColor playerColor) {
        var kingPosition = kingColor(playerColor).getChessPosition().toPosition();
        var opponentPieceList = piecesOnTheBoard.stream()
                .filter(pieces -> ((ChessPiece) pieces).getColor().equals(opponent(playerColor))).toList();
        for (var piece : opponentPieceList) {
            boolean[][] matrix = piece.possibleMoves();
            if (matrix[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna a peça do rei da cor especificada.
     *
     * @param playerColor A cor do jogador.
     * @return A peça do rei.
     * @throws IllegalStateException Se não houver um rei da cor especificada.
     */
    private ChessPiece kingColor(final PlayerColor playerColor) {
        return (ChessPiece) piecesOnTheBoard.stream()
                .filter(pieces -> ((ChessPiece) pieces).getColor().equals(playerColor))
                .filter(piece -> piece instanceof King)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Não existe o rei da cor: %s", playerColor)));
    }

    /**
     * Retorna a cor do jogador oponente.
     *
     * @param playerColor A cor do jogador atual.
     * @return A cor do jogador oponente.
     */
    private PlayerColor opponent(final PlayerColor playerColor) {
        return playerColor == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    /**
     * Desfaz um movimento de peça no tabuleiro.
     *
     * @param source        A posição de origem da peça.
     * @param target        A posição de destino da peça.
     * @param capturedPiece A peça capturada, se houver.
     */
    private void undoMove(final Position source, final Position target, Piece capturedPiece) {
        var piece = (ChessPiece) board.removePiece(target);
        piece.decreaseMoveCount();
        board.placePiece(piece, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        undoSpecialMoves(source, target, piece);
    }

    /**
     * Desfaz movimentos especiais, como roque e captura en passant.
     *
     * @param source A posição de origem da peça.
     * @param target A posição de destino da peça.
     * @param piece  A peça movida.
     */
    private void undoSpecialMoves(Position source, Position target, ChessPiece piece) {
        if (piece instanceof King) {
            undoCastling(source, target);
        }

        if (piece instanceof Pawn) {
            undoEnPassant(source, target, piece);
        }
    }

    /**
     * Desfaz o movimento de roque.
     *
     * @param source A posição de origem do rei.
     * @param target A posição de destino do rei.
     */
    private void undoCastling(Position source, Position target) {
        if (Math.abs(target.getColumn() - source.getColumn()) == 2) {
            var rookSource = target.getColumn() == source.getColumn() + 2 ?
                    new Position(source.getRow(), source.getColumn() + 3) :
                    new Position(source.getRow(), source.getColumn() - 4);

            var rookTarget = target.getColumn() == source.getColumn() + 2 ?
                    new Position(source.getRow(), source.getColumn() + 1) :
                    new Position(source.getRow(), source.getColumn() - 1);

            ChessPiece rook = (ChessPiece) board.removePiece(rookTarget);
            board.placePiece(rook, rookSource);
            rook.decreaseMoveCount();
        }
    }

    /**
     * Desfaz a captura en passant.
     *
     * @param source A posição de origem do peão.
     * @param target A posição de destino do peão.
     * @param piece  A peça movida.
     */
    private void undoEnPassant(Position source, Position target, ChessPiece piece) {
        if (source.getColumn() != target.getColumn() && enPassantVulnerable != null) {
            ChessPiece pawn = (ChessPiece) board.removePiece(target);
            var pawnPosition = piece.getColor() == PlayerColor.WHITE ?
                    new Position(3, target.getColumn()) :
                    new Position(4, target.getColumn());
            board.placePiece(pawn, pawnPosition);
        }
    }

    /**
     * Alterna o turno para o próximo jogador.
     */
    private void nextTurn() {
        currentPlayer = opponent(currentPlayer);
    }

    /**
     * Testa se o jogador está em checkmate.
     *
     * @param playerColor A cor do jogador a ser testado.
     * @return true se o jogador está em checkmate, caso contrário, false.
     */
    private boolean testCheckMate(final PlayerColor playerColor) {
        Objects.requireNonNull(playerColor, "A cor não pode ser nula.");

        if (!testCheck(playerColor)) {
            return false;
        }

        var pieceList = piecesOnTheBoard.stream()
                .filter(pieces -> ((ChessPiece) pieces).getColor().equals(playerColor)).toList();

        return pieceList.stream().noneMatch(piece -> canPieceAvoidCheck(piece, playerColor));
    }

    /**
     * Verifica se a peça pode evitar um check.
     *
     * @param piece       A peça a ser verificada.
     * @param playerColor A cor do jogador.
     * @return true se a peça pode evitar um check, caso contrário, false.
     */
    private boolean canPieceAvoidCheck(final Piece piece, final PlayerColor playerColor) {
        boolean[][] matrix = piece.possibleMoves();
        for (int row = 0; row < board.getRows(); row++) {
            for (int column = 0; column < board.getColumns(); column++) {
                if (matrix[row][column]) {
                    var source = ((ChessPiece) piece).getChessPosition().toPosition();
                    var target = new Position(row, column);
                    var capturedPiece = makeMove(source, target);
                    boolean testCheck = testCheck(playerColor);
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