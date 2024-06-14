package src.main.java.application;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.PlayerColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class ChessGUI extends JFrame {
    private static final int BOARD_SIZE = 8;
    private static final Color LIGHT_COLOR = Color.WHITE;
    private static final Color DARK_COLOR = Color.GRAY;
    private static final Color HIGHLIGHT_COLOR = Color.GREEN;
    private static final int BUTTON_SIZE = 60;

    private final JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
    private final JButton[][] boardSquares = new JButton[BOARD_SIZE][BOARD_SIZE];
    private final ChessMatch chessMatch = new ChessMatch();
    private ChessPosition sourcePosition;
    private boolean[][] possibleMoves;

    public ChessGUI() {
        setupGUI();
        updateBoard();
    }

    private void setupGUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeBoard();
        add(boardPanel, BorderLayout.CENTER);
        setWindowSizeAndLocation();

        setVisible(true);
    }

    private void setWindowSizeAndLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 3, screenSize.height / 2);
        setLocationRelativeTo(null);
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton button = createBoardButton(row, col);
                boardSquares[row][col] = button;
                boardPanel.add(button);
            }
        }
    }

    private JButton createBoardButton(int row, int col) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.addActionListener(e -> handleButtonClick(row, col));
        setButtonColor(button, row, col);
        return button;
    }

    private void handleButtonClick(int row, int col) {
        try {
            if (sourcePosition == null) {
                selectPiece(row, col);
            } else {
                movePiece(row, col);
            }
        } catch (Exception ex) {
            showErrorDialog("Invalid Move: " + ex.getMessage());
            resetSelection();
        }
        updateBoard();
    }

    private void selectPiece(int row, int col) {
        sourcePosition = new ChessPosition((char) ('a' + col), 8 - row);
        possibleMoves = chessMatch.possibleMoves(sourcePosition);
    }

    private void movePiece(int row, int col) {
        ChessPosition targetPosition = new ChessPosition((char) ('a' + col), 8 - row);
        chessMatch.performChessMove(sourcePosition, targetPosition);
        resetSelection();
    }

    private void resetSelection() {
        sourcePosition = null;
        possibleMoves = null;
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void updateBoard() {
        ChessPiece[][] pieces = chessMatch.getPieces();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                updateBoardSquare(row, col, pieces[row][col]);
            }
        }
    }

    private void updateBoardSquare(int row, int col, ChessPiece piece) {
        JButton button = boardSquares[row][col];
        button.setIcon(Optional.ofNullable(piece).map(this::getPieceIcon).orElse(null));
        setButtonColor(button, row, col);
        if (possibleMoves != null && possibleMoves[row][col]) {
            button.setBackground(HIGHLIGHT_COLOR);
        }
    }

    private void setButtonColor(JButton button, int row, int col) {
        button.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
    }

    private Icon getPieceIcon(ChessPiece piece) {
        String pieceName = piece.getClass().getSimpleName().toLowerCase();
        String color = piece.getColor() == PlayerColor.WHITE ? "white" : "black";
        String path = String.format("/src/main/java/resources/images/pieces-basic-png/%s-%s.png", color, pieceName);
        URL imageURL = getClass().getResource(path);
        return Optional.ofNullable(imageURL).map(this::loadImage).orElse(null);
    }

    private ImageIcon loadImage(URL imageURL) {
        try {
            BufferedImage originalImage = ImageIO.read(imageURL);
            Image scaledImage = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGUI::new);
    }

}