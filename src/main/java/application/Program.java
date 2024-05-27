package src.main.java.application;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.exceptions.ChessException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        var input = new Scanner(System.in);
        var chessMatch = new ChessMatch();
        List<ChessPiece> capturedPiecesList = new ArrayList<>();

        while (!chessMatch.getCheckMate()) {

            try {
                UserInterface.clearScreen();
                UserInterface.printMatch(chessMatch, capturedPiecesList);
                System.out.println();
                System.out.print("Source: ");
                var source = UserInterface.readChessPosition(input);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UserInterface.clearScreen();
                UserInterface.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.println();
                System.out.print("Target: ");
                var target = UserInterface.readChessPosition(input);

                var capturedPiece = chessMatch.performChessMove(source, target);

                if (capturedPiece != null) {
                    capturedPiecesList.add(capturedPiece);
                }

                if (chessMatch.getPromoted() != null) {
                    System.out.println("Digite a peça a ser escolhida (B/N/R/Q) :");
                    var pieceType = input.nextLine();
                    var cleanedPieceType = pieceType.toUpperCase().replaceAll("\\s", "");
                    chessMatch.replacePromotedPiece(cleanedPieceType);
                }

            } catch (ChessException | InputMismatchException exception) {
                System.out.println(exception.getMessage());
                input.nextLine();
            }

        }

        UserInterface.clearScreen();
        UserInterface.printMatch(chessMatch, capturedPiecesList);

    }
}