package src.main.java.application;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.exceptions.ChessException;

import java.util.*;

/**
 * Classe principal do jogo de xadrez, que gerencia o fluxo do jogo.
 */
public class ChessGame {

    /**
     * Mensagem de entrada para seleção da peça promovida (B/N/R/Q).
     */
    private static final String PROMOTED_PIECE_INPUT_MESSAGE = "Digite a peça a ser promovida (B/N/R/Q): ";

    /**
     * Mensagem de valor inválido.
     */
    private static final String INVALID_VALUE_MESSAGE = "Valor inválido. Os valores válidos são B, N, R e Q.";

    /**
     * Prompt para entrada da posição de origem.
     */
    private static final String SOURCE_PROMPT = "Origem: ";

    /**
     * Prompt para entrada da posição de destino.
     */
    private static final String TARGET_PROMPT = "Destino: ";

    /**
     * Método principal que inicia o jogo de xadrez.
     *
     * @param args Argumentos de linha de comando.
     */
    public static void main(String[] args) {
        try (var input = new Scanner(System.in)) {
            var chessMatch = new ChessMatch();
            List<ChessPiece> capturedPiecesList = new ArrayList<>();

            while (!chessMatch.isCheckMate()) {
                playTurn(input, chessMatch, capturedPiecesList);
            }

            UserInterface.clearScreen();
            UserInterface.printMatch(chessMatch, capturedPiecesList);
        }
    }

    /**
     * Executa um turno do jogo, solicitando as posições de origem e destino do jogador.
     *
     * @param input              Scanner para ler as entradas do jogador.
     * @param chessMatch         Objeto que representa a partida de xadrez.
     * @param capturedPiecesList Lista de peças capturadas durante a partida.
     */
    private static void playTurn(Scanner input, ChessMatch chessMatch, List<ChessPiece> capturedPiecesList) {
        try {
            UserInterface.clearScreen();
            UserInterface.printMatch(chessMatch, capturedPiecesList);
            System.out.println();
            System.out.print(SOURCE_PROMPT);
            var source = UserInterface.readChessPosition(input);

            boolean[][] possibleMoves = chessMatch.possibleMoves(source);
            UserInterface.clearScreen();
            UserInterface.printBoard(chessMatch.getPieces(), possibleMoves);

            System.out.println();
            System.out.print(TARGET_PROMPT);
            var target = UserInterface.readChessPosition(input);

            String pieceType = null;
            if (chessMatch.isPromotionPossible(source, target)) {
                pieceType = choosePromotionPiece(input);
            }

            var capturedPiece = Optional.ofNullable(chessMatch.performChessMove(source, target, pieceType));
            capturedPiece.ifPresent(capturedPiecesList::add);

            if (chessMatch.getPromoted() != null && pieceType != null) {
                chessMatch.replacePromotedPiece(pieceType); // Ensure the promoted piece is correctly handled.
            }

        } catch (ChessException | InputMismatchException exception) {
            System.out.println(exception.getMessage());
            input.nextLine();
        }
    }


    /**
     * @param input Scanner para leitura da entrada do jogador.
     * @return String representando a peça escolhida para promoção.
     */
    private static String choosePromotionPiece(Scanner input) {
        System.out.print(PROMOTED_PIECE_INPUT_MESSAGE);
        String pieceType = input.next().toUpperCase();
        while (!pieceType.matches("[QRBN]")) {
            System.out.println(INVALID_VALUE_MESSAGE);
            pieceType = input.next().toUpperCase();
        }
        return pieceType;
    }

}