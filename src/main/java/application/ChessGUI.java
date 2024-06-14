package src.main.java.application;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ChessGUI extends JFrame {
    private JPanel boardPanel;
    private final JButton[][] boardSquares = new JButton[8][8];
    private final ChessMatch chessMatch;
    private ChessPosition sourcePosition;
    private boolean[][] possibleMoves;

    public ChessGUI() {
        chessMatch = new ChessMatch();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Chess Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new JPanel(new GridLayout(8, 8));
        initializeBoard();

        add(boardPanel, BorderLayout.CENTER);

        setVisible(true);
        updateBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardSquares[row][col] = new JButton();
                boardSquares[row][col].setPreferredSize(new Dimension(60, 60));
                boardSquares[row][col].addActionListener(new BoardButtonListener(row, col));
                if ((row + col) % 2 == 0) {
                    boardSquares[row][col].setBackground(Color.WHITE);
                } else {
                    boardSquares[row][col].setBackground(Color.GRAY);
                }
                boardPanel.add(boardSquares[row][col]);
            }
        }
    }

    private void updateBoard() {
        ChessPiece[][] pieces = chessMatch.getPieces();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = pieces[row][col];
                if (piece != null) {
                    boardSquares[row][col].setIcon(getPieceIcon(piece));
                    boardSquares[row][col].setText("");
                } else {
                    boardSquares[row][col].setIcon(null);
                    boardSquares[row][col].setText("");
                }
                if ((row + col) % 2 == 0) {
                    boardSquares[row][col].setBackground(Color.WHITE);
                } else {
                    boardSquares[row][col].setBackground(Color.GRAY);
                }
                if (possibleMoves != null && possibleMoves[row][col]) {
                    boardSquares[row][col].setBackground(Color.GREEN);
                }
            }
        }
    }

    private Icon getPieceIcon(ChessPiece piece) {
        String pieceName = piece.getClass().getSimpleName().toLowerCase();
        String color = piece.getColor() == PlayerColor.WHITE ? "white" : "black";
        String path = String.format("/src/main/java/resources/images/pieces-basic-png/%s-%s.png", color, pieceName);
        URL imageURL = getClass().getResource(path);
        if (imageURL != null) {
            return new ImageIcon(imageURL);
        } else {
            System.err.println("Image not found: " + path);
            return null; // Fallback if image is not found
        }
    }

    private class BoardButtonListener implements ActionListener {
        private final int row;
        private final int col;

        public BoardButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle button click to update game state
            System.out.println("Button clicked at (" + row + ", " + col + ")");
            try {
                if (sourcePosition == null) {
                    // Select the piece
                    sourcePosition = new ChessPosition((char) ('a' + col), 8 - row);
                    possibleMoves = chessMatch.possibleMoves(sourcePosition);
                    updateBoard();
                } else {
                    // Move the piece
                    ChessPosition targetPosition = new ChessPosition((char) ('a' + col), 8 - row);
                    chessMatch.performChessMove(sourcePosition, targetPosition);
                    sourcePosition = null;
                    possibleMoves = null;
                    updateBoard();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid Move: " + ex.getMessage());
                sourcePosition = null;
                possibleMoves = null;
                updateBoard();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGUI::new);
    }

}