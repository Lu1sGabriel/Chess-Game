package src.main.java.application;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.exceptions.ChessException;

import java.util.*;

public class ChessGame {

    private static final String PROMOTED_PIECE_INPUT_MESSAGE = "Digite a peça a ser promovida (B/N/R/Q): ";
    private static final String SOURCE_PROMPT = "Origem: ";
    private static final String TARGET_PROMPT = "Destino: ";

    public static void main(String[] args) {
        try (var input = new Scanner(System.in)) {
            var chessMatch = new ChessMatch();
            List<ChessPiece> capturedPiecesList = new ArrayList<>();

            while (!chessMatch.getCheckMate()) {
                playTurn(input, chessMatch, capturedPiecesList);
            }

            UserInterface.clearScreen();
            UserInterface.printMatch(chessMatch, capturedPiecesList);
        }
    }

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

            var capturedPiece = Optional.ofNullable(chessMatch.performChessMove(source, target));

            capturedPiece.ifPresent(capturedPiecesList::add);

            var promoted = Optional.ofNullable(chessMatch.getPromoted());

            if (promoted.isPresent()) {
                System.out.println(PROMOTED_PIECE_INPUT_MESSAGE);
                var pieceType = input.nextLine().trim().toUpperCase();
                while (!pieceType.matches("[BNRQ]")) {
                    System.out.println("Valor inválido! " + PROMOTED_PIECE_INPUT_MESSAGE);
                    pieceType = input.nextLine().replaceAll("\\s", "").toUpperCase();
                }
                chessMatch.replacePromotedPiece(pieceType);
            }

        } catch (ChessException | InputMismatchException exception) {
            System.out.println(exception.getMessage());
            input.nextLine();
        }
    }
}