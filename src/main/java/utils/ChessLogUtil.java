package src.main.java.utils;

import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.PlayerColor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilitário para registrar logs de eventos em partidas de xadrez.
 * <p>
 * Esta classe fornece métodos para registrar movimentos, capturas, promoções e vitórias de partidas de xadrez em um arquivo de log.
 */
public class ChessLogUtil {

    private static final String LOG_DIRECTORY_PATH = "C:\\Users\\luisb\\OneDrive\\Área de Trabalho";

    /**
     * Registra um movimento de peça no arquivo de log.
     *
     * @param matchId O identificador da partida.
     * @param player  A cor do jogador que realizou o movimento.
     * @param source  A posição de origem da peça.
     * @param target  A posição de destino da peça.
     * @param piece   A peça que foi movida.
     */
    public static void logMove(String matchId, PlayerColor player, ChessPosition source, ChessPosition target, ChessPiece piece) {
        String log = String.format("%s - Player %s move: %s %s -> %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), player, piece, source, target);
        writeLogToFile(matchId, log);
    }

    /**
     * Registra a captura de uma peça no arquivo de log.
     *
     * @param matchId       O identificador da partida.
     * @param player        A cor do jogador que realizou a captura.
     * @param capturedPiece A peça que foi capturada.
     * @param position      A posição onde a captura ocorreu.
     */
    public static void logCapture(String matchId, PlayerColor player, ChessPiece capturedPiece, ChessPosition position) {
        String log = String.format("%s - Player %s capture: %s %s at %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), player, capturedPiece, capturedPiece.getColor(), position);
        writeLogToFile(matchId, log);
    }

    /**
     * Registra a promoção de um peão no arquivo de log.
     *
     * @param matchId        O identificador da partida.
     * @param player         A cor do jogador que realizou a promoção.
     * @param promotedToType O tipo da peça para a qual o peão foi promovido.
     * @param position       A posição onde a promoção ocorreu.
     */
    public static void logPromotion(String matchId, PlayerColor player, String promotedToType, ChessPosition position) {
        String log = String.format("%s - Player %s promoted: Pawn to %s at %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), player, promotedToType, position);
        writeLogToFile(matchId, log);
    }

    /**
     * Registra a vitória de uma partida no arquivo de log.
     *
     * @param matchId O identificador da partida.
     * @param winner  O jogador vencedor.
     */
    public static void logWin(String matchId, String winner) {
        String log = String.format("%s - Game End: Winner is %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")), winner);
        writeLogToFile(matchId, log);
    }

    /**
     * Escreve uma mensagem de log no arquivo de log da partida.
     *
     * @param matchId O identificador da partida.
     * @param log     A mensagem de log a ser escrita.
     */
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
