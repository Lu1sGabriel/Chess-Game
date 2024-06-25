package src.main.java.utils;

import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.PlayerColor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChessLogUtil {
    private static final String LOG_DIRECTORY_PATH = "C:\\Users\\luisb\\OneDrive\\Área de Trabalho";

    public static void logMove(String matchId, PlayerColor player, ChessPosition source, ChessPosition target, ChessPiece piece) {
        String log = String.format("%s - Player %s move: %s from %s to %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), player, piece, source, target);
        writeLogToFile(matchId, log);
    }

    public static void logCapture(String matchId, PlayerColor player, ChessPiece capturedPiece, ChessPosition position) {
        String log = String.format("%s -  Player %s capture: %s %s at %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), player, capturedPiece, capturedPiece.getColor(), position);
        writeLogToFile(matchId, log);
    }

    public static void logPromotion(String matchId, PlayerColor player, String promotedToType, ChessPosition position) {
        String log = String.format("%s - Player %s promoted: p to %s at %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), player, promotedToType, position);
        writeLogToFile(matchId, log);
    }

    public static void logWin(String matchId, String winner) {
        String log = String.format("%s - Game End: Winner is %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), winner);
        writeLogToFile(matchId, log);
    }

    private static void writeLogToFile(String matchId, String log) {
        String fileName = String.format("%s\\chess_game_log_%s.txt", LOG_DIRECTORY_PATH, matchId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

}
