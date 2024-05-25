package src.main.java.application;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.exceptions.ChessException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        var input = new Scanner(System.in);
        var chessMatch = new ChessMatch();

        while (true) {

            try {
                UserInterface.clearScreen();
                UserInterface.printBoard(chessMatch.getPieces());
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UserInterface.readChessPosition(input);

                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UserInterface.readChessPosition(input);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
            } catch (ChessException | InputMismatchException exception) {
                System.out.println(exception.getMessage());
                input.nextLine();
            }

        }
    }
}