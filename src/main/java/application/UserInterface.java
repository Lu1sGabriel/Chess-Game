package src.main.java.application;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.PlayerColor;
import src.main.java.utils.TerminalColor;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsável pela interface do usuário no jogo de xadrez.
 * Contém métodos para leitura de posições, impressão do tabuleiro, limpeza da tela e exibição do estado atual do jogo.
 */
public class UserInterface {

    /**
     * Lê uma posição de xadrez a partir da entrada do usuário.
     *
     * @param input Scanner utilizado para ler a entrada do usuário.
     * @return Uma instância de ChessPosition correspondente à posição lida.
     * @throws InputMismatchException se a posição fornecida for inválida.
     */
    public static ChessPosition readChessPosition(final Scanner input) {
        try {
            var userPosition = input.nextLine().replaceAll("\\s", "").toLowerCase(); // Remove todos os espaços
            char column = userPosition.charAt(0);
            int row = Integer.parseInt(userPosition.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException exception) {
            throw new InputMismatchException("Erro lendo posições do Xadrez. Valores válidos são de a1 até h8.");
        }
    }

    /**
     * Imprime o tabuleiro de xadrez na tela.
     *
     * @param pieces Matriz de peças do tabuleiro de xadrez.
     */
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

    /**
     * Imprime o tabuleiro de xadrez na tela, destacando os movimentos possíveis.
     *
     * @param pieces        Matriz de peças do tabuleiro de xadrez.
     * @param possibleMoves Matriz de movimentos possíveis.
     */
    public static void printBoard(final ChessPiece[][] pieces, final boolean[][] possibleMoves) {
        var stringBuilder = new StringBuilder();
        for (int row = 0; row < pieces.length; row++) {
            stringBuilder.append((8 - row)).append(" ");
            for (int column = 0; column < pieces[row].length; column++) {
                printPiece(pieces[row][column], possibleMoves[row][column], stringBuilder);
            }
            stringBuilder.append(System.lineSeparator());
        }
        stringBuilder.append("  a b c d e f g h");
        System.out.println(stringBuilder);
    }

    /**
     * Imprime uma peça no tabuleiro, com a opção de destacar o fundo.
     *
     * @param piece         A peça de xadrez a ser impressa.
     * @param background    Indica se o fundo deve ser destacado.
     * @param stringBuilder StringBuilder usado para construir a saída.
     */
    private static void printPiece(final ChessPiece piece, final boolean background, final StringBuilder stringBuilder) {
        if (background) {
            stringBuilder.append(TerminalColor.ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            stringBuilder.append("-").append(TerminalColor.ANSI_RESET);
        } else {
            if (piece.getColor() == PlayerColor.WHITE) {
                stringBuilder.append(TerminalColor.ANSI_WHITE).append(piece).append(TerminalColor.ANSI_RESET);
            } else {
                stringBuilder.append(TerminalColor.ANSI_YELLOW).append(piece).append(TerminalColor.ANSI_RESET);
            }
        }
        stringBuilder.append(" ");
    }

    /**
     * Limpa a tela do terminal.
     */
    public static void clearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Imprime o estado atual da partida de xadrez, incluindo o tabuleiro e as peças capturadas.
     *
     * @param chessMatch     A partida de xadrez atual.
     * @param capturedPieces A lista de peças capturadas.
     */
    public static void printMatch(final ChessMatch chessMatch, final List<ChessPiece> capturedPieces) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(capturedPieces);
        System.out.println();
        System.out.printf("Turno: %d%n", chessMatch.getTurn());
        if (!chessMatch.getCheckMate()) {
            System.out.printf("Esperando o jogador: %s%n", chessMatch.getCurrentPlayer());
            if (chessMatch.getCheck()) {
                System.out.println("CHECK!");
            }
        } else {
            System.out.println("CHECKMATE!");
            System.out.printf("O vencedor é: %s%n", chessMatch.getCurrentPlayer());
        }
    }

    /**
     * Imprime as peças capturadas, separadas por cor.
     *
     * @param capturedPieces A lista de peças capturadas.
     */
    private static void printCapturedPieces(final List<ChessPiece> capturedPieces) {
        var white = capturedPieces.stream().filter(listElement -> listElement.getColor() == PlayerColor.WHITE).toList();
        var black = capturedPieces.stream().filter(listElement -> listElement.getColor() == PlayerColor.BLACK).toList();
        System.out.println("Peças capturadas:");
        System.out.println("Brancas:");
        System.out.print(TerminalColor.ANSI_WHITE);
        System.out.println(Arrays.toString(white.toArray()));
        System.out.print(TerminalColor.ANSI_RESET);
        System.out.println("Pretas:");
        System.out.print(TerminalColor.ANSI_YELLOW);
        System.out.println(Arrays.toString(black.toArray()));
        System.out.print(TerminalColor.ANSI_RESET);
    }

}