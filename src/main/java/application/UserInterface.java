package src.main.java.application;

import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.Color;
import src.main.java.utils.TerminalColor;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface {

    public static ChessPosition readChessPosition(Scanner input) {
        try {
            var userPosition = input.nextLine().replaceAll("\\s", "").toLowerCase(); // Remove todos os espaços
            char column = userPosition.charAt(0);
            int row = Integer.parseInt(userPosition.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException exception) {
            throw new InputMismatchException("Erro lendo posições do Xadrez. Valores válidos são de a1 até a8.");
        }
    }

    public static void printBoard(ChessPiece[][] pieces) {
        for (int row = 0; row < pieces.length; row++) {
            System.out.print((8 - row) + " ");
            for (int column = 0; column < pieces[row].length; column++) {
                printPiece(pieces[row][column]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }


    private static void printPiece(ChessPiece piece) {
        if (piece == null) {
            System.out.print("-");
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(TerminalColor.ANSI_WHITE + piece + TerminalColor.ANSI_RESET);
            } else {
                System.out.print(TerminalColor.ANSI_YELLOW + piece + TerminalColor.ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    public static void clearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

}
