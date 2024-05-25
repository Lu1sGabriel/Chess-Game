package src.main.java.application;

import src.main.java.chess.ChessMatch;

public class Program {
    public static void main(String[] args) {
        var chessMatch = new ChessMatch();
        UserInterface.printBoard(chessMatch.getPieces());
    }
}
