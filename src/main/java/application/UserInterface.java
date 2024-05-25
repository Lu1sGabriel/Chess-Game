package src.main.java.application;

import src.main.java.chess.ChessPiece;
import src.main.java.chess.Color;
import src.main.java.utils.TerminalColor;

public class UserInterface {

    public static void printBoard(ChessPiece[][] pieces) {
        for (int row = 0; row < pieces.length; row++) {
            System.out.print((8 - row) + " ");
            for (var piece : pieces[row]) {
                printPiece(piece);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece) {
        if (piece == null) {
            System.out.print("-");
        } else {
            String colorCode = (piece.getColor() == Color.WHITE) ? TerminalColor.ANSI_WHITE : TerminalColor.ANSI_YELLOW;
            System.out.print(colorCode + piece + TerminalColor.ANSI_RESET);
        }
        System.out.print(" ");
    }

}
