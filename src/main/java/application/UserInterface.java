package src.main.java.application;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.Color;
import src.main.java.utils.TerminalColor;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    public static ChessPosition readChessPosition(Scanner input) {
        try {
            var userPosition = input.nextLine().replaceAll("\\s", "").toLowerCase(); // Remove todos os espaços
            char column = userPosition.charAt(0);
            int row = Integer.parseInt(userPosition.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException exception) {
            throw new InputMismatchException("Erro lendo posições do Xadrez. Valores válidos são de a1 até a8. ");
        }
    }

    public static void printBoard(ChessPiece[][] pieces) {
        for (int row = 0; row < pieces.length; row++) {
            System.out.print((8 - row) + " ");
            for (int column = 0; column < pieces[row].length; column++) {
                printPiece(pieces[row][column], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int row = 0; row < pieces.length; row++) {
            System.out.print((8 - row) + " ");
            for (int column = 0; column < pieces[row].length; column++) {
                printPiece(pieces[row][column], possibleMoves[row][column]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(TerminalColor.ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print("-" + TerminalColor.ANSI_RESET);
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

    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> capturedPieces) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(capturedPieces);
        System.out.println();
        System.out.println("Turno: " + chessMatch.getTurn());
        System.out.println("Esperando o jogador: " + chessMatch.getCurrentPlayer());
    }

    private static void printCapturedPieces(List<ChessPiece> capturedPieces) {
        var white = capturedPieces.stream().filter(listElement -> listElement.getColor() == Color.WHITE).toList();
        var black = capturedPieces.stream().filter(listElement -> listElement.getColor() == Color.BLACK).toList();
        System.out.println("Peças capturadas: ");
        System.out.println("Brancas: ");
        System.out.print(TerminalColor.ANSI_WHITE);
        System.out.println(Arrays.toString(white.toArray()));
        System.out.print(TerminalColor.ANSI_RESET);
        System.out.println("Pretas: ");
        System.out.print(TerminalColor.ANSI_YELLOW);
        System.out.println(Arrays.toString(black.toArray()));
        System.out.print(TerminalColor.ANSI_RESET);
    }

}
