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

    public static ChessPosition readChessPosition(final Scanner input) {
        try {
            var userPosition = input.nextLine().replaceAll("\\s", "").toLowerCase(); // Remove todos os espaços
            char column = userPosition.charAt(0);
            int row = Integer.parseInt(userPosition.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException exception) {
            throw new InputMismatchException("Erro lendo posições do Xadrez. Valores válidos são de a1 até a8. ");
        }
    }

    public static void printBoard(final ChessPiece[][] pieces) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < pieces.length; row++) {
            sb.append((8 - row)).append(" ");
            for (int column = 0; column < pieces[row].length; column++) {
                printPiece(pieces[row][column], false, sb);
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");
        System.out.println(sb);
    }

    public static void printBoard(final ChessPiece[][] pieces, final boolean[][] possibleMoves) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < pieces.length; row++) {
            sb.append((8 - row)).append(" ");
            for (int column = 0; column < pieces[row].length; column++) {
                printPiece(pieces[row][column], possibleMoves[row][column], sb);
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");
        System.out.println(sb);
    }

    private static void printPiece(final ChessPiece piece, final boolean background, final StringBuilder sb) {
        if (background) {
            sb.append(TerminalColor.ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            sb.append("-").append(TerminalColor.ANSI_RESET);
        } else {
            if (piece.getColor() == Color.WHITE) {
                sb.append(TerminalColor.ANSI_WHITE).append(piece).append(TerminalColor.ANSI_RESET);
            } else {
                sb.append(TerminalColor.ANSI_YELLOW).append(piece).append(TerminalColor.ANSI_RESET);
            }
        }
        sb.append(" ");
    }

    public static void clearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    public static void printMatch(final ChessMatch chessMatch, final List<ChessPiece> capturedPieces) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(capturedPieces);
        System.out.println();
        System.out.printf("Turno: %d%n", chessMatch.getTurn());
        if (!chessMatch.getCheckMate()) {
            System.out.printf("Esperando o jogador: %s%n", chessMatch.getCurrentPlayer());
            if (chessMatch.getCheck()) {
                System.out.println("CHECK! ");
            }
        } else {
            System.out.println("CHECKMATE! ");
            System.out.printf("O vencedor é: %s%n", chessMatch.getCurrentPlayer());
        }

    }

    private static void printCapturedPieces(final List<ChessPiece> capturedPieces) {
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