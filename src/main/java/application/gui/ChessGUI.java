package src.main.java.application.gui;

import src.main.java.chess.ChessMatch;
import src.main.java.chess.ChessPiece;
import src.main.java.chess.ChessPosition;
import src.main.java.chess.PlayerColor;
import src.main.java.utils.ChessSaveUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ChessGUI extends JFrame {

    private static final int BOARD_SIZE = 8;
    private static final Color LIGHT_COLOR = new Color(240, 217, 181);
    private static final Color DARK_COLOR = new Color(181, 136, 99);
    private static final Color HIGHLIGHT_COLOR = new Color(118, 150, 86);
    private static final Color CAPTURE_COLOR = new Color(255, 99, 71);
    private static final int BUTTON_SIZE = 80;
    private static final String IMAGE_BASE_PATH = "/src/main/java/resources/images/pieces-basic-png/";

    private final JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1));
    private final JButton[][] boardSquares = new JButton[BOARD_SIZE][BOARD_SIZE];
    private final JLabel turnLabel = new JLabel("Turn: White", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Score - White: 0, Black: 0", SwingConstants.CENTER);
    private final JButton cancelButton = new JButton("Cancelar Ação");
    private final JButton saveButton = new JButton("Salvar Partida");
    private final JButton loadButton = new JButton("Carregar Partida");
    private final JButton exitButton = new JButton("Sair");
    private ChessMatch chessMatch;
    private ChessPosition sourcePosition;
    private boolean[][] possibleMoves;
    private final Map<String, ImageIcon> pieceIconCache = new HashMap<>();
    private int whiteScore = 0;
    private int blackScore = 0;

    protected ChessGUI() {
        this(new ChessMatch());
    }

    protected ChessGUI(ChessMatch chessMatch) {
        this.chessMatch = chessMatch;
        preloadPieceIcons();
        setupGUI();
        updateBoard();
    }

    private void preloadPieceIcons() {
        String[] colors = {"white", "black"};
        String[] pieces = {"king", "queen", "rook", "bishop", "knight", "pawn"};
        for (String color : colors) {
            for (String piece : pieces) {
                String key = color + "_" + piece;
                String path = String.format("%s%s-%s.png", IMAGE_BASE_PATH, color, piece);
                URL imageURL = getClass().getResource(path);
                pieceIconCache.put(key, Optional.ofNullable(imageURL).map(this::loadImage).orElse(null));
            }
        }
    }

    private void setupGUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        styleTurnLabel();
        styleScoreLabel();
        initializeBoard();

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(turnLabel, BorderLayout.NORTH);
        headerPanel.add(scoreLabel, BorderLayout.SOUTH);

        JPanel sidePanel = new JPanel(new GridLayout(4, 1, 10, 10));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        styleButton(cancelButton, Color.RED);
        styleButton(saveButton, new Color(0, 128, 0));
        styleButton(loadButton, new Color(0, 128, 255));
        styleButton(exitButton, new Color(128, 0, 0));

        cancelButton.addActionListener(e -> cancelAction());
        saveButton.addActionListener(e -> saveMatch());
        loadButton.addActionListener(e -> loadMatch());
        exitButton.addActionListener(e -> System.exit(0));

        sidePanel.add(cancelButton);
        sidePanel.add(saveButton);
        sidePanel.add(loadButton);
        sidePanel.add(exitButton);

        add(headerPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        setWindowSizeAndLocation();

        setGlassPane(new GlassPane());
        getGlassPane().setVisible(false);

        setVisible(true);
    }

    private void styleTurnLabel() {
        turnLabel.setFont(new Font("Arial", Font.BOLD, 24));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(new Color(50, 50, 50));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void styleScoreLabel() {
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(new Color(50, 50, 50));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void styleButton(JButton button, Color color) {
        button.setEnabled(true);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(color));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
    }

    private void setWindowSizeAndLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, (int) (screenSize.height / 1.5));
        setLocationRelativeTo(null);
    }

    private void initializeBoard() {
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        boardPanel.add(new JLabel("")); // canto superior esquerdo

        for (int col = 0; col < BOARD_SIZE; col++) {
            JLabel label = createLabel(String.valueOf((char) ('a' + col)));
            boardPanel.add(label);
        }

        for (int row = 0; row < BOARD_SIZE; row++) {
            JLabel label = createLabel(String.valueOf(BOARD_SIZE - row));
            boardPanel.add(label);

            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton button = createBoardButton(row, col);
                boardSquares[row][col] = button;
                boardPanel.add(button);
            }
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    private JButton createBoardButton(int row, int col) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setFocusPainted(false);
        button.addActionListener(e -> handleButtonClick(row, col));
        setButtonColor(button, row, col);
        return button;
    }

    private void handleButtonClick(int row, int col) {
        SwingUtilities.invokeLater(() -> {
            try {
                if (sourcePosition == null) {
                    selectPiece(row, col);
                } else {
                    movePiece(row, col);
                }
            } catch (Exception ex) {
                showErrorDialog("Movimento inválido: " + ex.getMessage());
                resetSelection();
            }
            updateBoard();
        });
    }

    private void selectPiece(int row, int col) {
        ChessPiece piece = chessMatch.getPieces()[row][col];
        if (piece != null && piece.getColor() == chessMatch.getCurrentPlayer()) {
            sourcePosition = new ChessPosition((char) ('a' + col), 8 - row);
            possibleMoves = chessMatch.possibleMoves(sourcePosition);
            cancelButton.setEnabled(true);
        } else {
            sourcePosition = null;
            possibleMoves = null;
        }
        updateBoard();
    }

    private void movePiece(int row, int col) {
        ChessPosition targetPosition = new ChessPosition((char) ('a' + col), 8 - row);
        try {
            String pieceType = null;
            if (chessMatch.isPromotionPossible(sourcePosition, targetPosition)) {
                pieceType = choosePromotionPiece(chessMatch.getCurrentPlayer());
            }
            chessMatch.performChessMove(sourcePosition, targetPosition, pieceType);
            updateBoard();

            if (chessMatch.isCheckMate()) {
                showErrorDialog("Check! Você deve proteger seu rei!");
            }
            if (chessMatch.isCheckMate()) {
                PlayerColor winner = chessMatch.getCurrentPlayer().opponent();
                updateScore(winner);
                showWinnerDialog(winner);
                resetGame();
            }
            resetSelection();
        } catch (Exception ex) {
            showErrorDialog("Movimento inválido: " + ex.getMessage());
            resetSelection();
        }
    }

    private String choosePromotionPiece(PlayerColor playerColor) {
        JDialog promotionDialog = new JDialog(this, "Escolher Peça de Promoção", true);
        promotionDialog.setLayout(new GridLayout(2, 2));
        promotionDialog.setSize(500, 450);
        promotionDialog.setLocationRelativeTo(this);

        String[] pieceIcons = getPieceIconPaths(playerColor);
        String[] pieceCodes = {"Q", "R", "B", "N"};

        final String[] selectedPiece = {pieceCodes[0]};

        for (int i = 0; i < pieceIcons.length; i++) {
            JButton button = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource(pieceIcons[i]))));
            button.setFocusPainted(false);
            button.setBackground(Color.WHITE);
            int finalI = i;
            button.addActionListener(e -> {
                selectedPiece[0] = pieceCodes[finalI];
                promotionDialog.dispose();
            });
            promotionDialog.add(button);
        }

        getGlassPane().setVisible(true);
        promotionDialog.setVisible(true);
        getGlassPane().setVisible(false);

        return selectedPiece[0];
    }

    private static String[] getPieceIconPaths(PlayerColor playerColor) {
        String colorPrefix = playerColor == PlayerColor.WHITE ? "white" : "black";
        return new String[]{
                String.format("%s%s-queen.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-rook.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-bishop.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-knight.png", IMAGE_BASE_PATH, colorPrefix)
        };
    }

    private void cancelAction() {
        resetSelection();
        cancelButton.setEnabled(false);
    }

    private void resetSelection() {
        sourcePosition = null;
        possibleMoves = null;
        updateBoard();
        cancelButton.setEnabled(false);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void updateBoard() {
        ChessPiece[][] pieces = chessMatch.getPieces();
        SwingUtilities.invokeLater(() -> {
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    updateBoardSquare(row, col, pieces[row][col]);
                }
            }
            updateTurnLabel();
        });
    }

    private void updateBoardSquare(int row, int col, ChessPiece piece) {
        JButton button = boardSquares[row][col];
        Icon currentIcon = button.getIcon();
        Icon newIcon = Optional.ofNullable(piece).map(this::getPieceIcon).orElse(null);
        if (!Objects.equals(currentIcon, newIcon)) {
            button.setIcon(newIcon);
        }
        setButtonColor(button, row, col);

        if (piece != null && piece.getColor() != chessMatch.getCurrentPlayer()) {
            button.setEnabled(possibleMoves != null && possibleMoves[row][col]);
            button.setToolTipText("Peça do oponente");
        } else if (sourcePosition != null && possibleMoves != null) {
            button.setEnabled(possibleMoves[row][col]);
            button.setToolTipText(null);
        } else {
            button.setEnabled(piece != null && piece.getColor() == chessMatch.getCurrentPlayer());
            button.setToolTipText(piece == null ? null : "Sua peça");
        }

        if (possibleMoves != null && possibleMoves[row][col]) {
            button.setBackground((chessMatch.getPieces()[row][col] != null && !chessMatch.getPieces()[row][col].getColor().equals(chessMatch.getCurrentPlayer())) ? CAPTURE_COLOR : HIGHLIGHT_COLOR);
        } else {
            button.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
        }
    }

    private void setButtonColor(JButton button, int row, int col) {
        button.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
    }

    private Icon getPieceIcon(ChessPiece piece) {
        String pieceName = piece.getClass().getSimpleName().toLowerCase();
        String color = piece.getColor() == PlayerColor.WHITE ? "white" : "black";
        String key = color + "_" + pieceName;
        return pieceIconCache.get(key);
    }

    private ImageIcon loadImage(URL imageURL) {
        try {
            BufferedImage originalImage = ImageIO.read(imageURL);
            Image scaledImage = originalImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTurnLabel() {
        PlayerColor currentPlayer = chessMatch.getCurrentPlayer();
        String player = currentPlayer == PlayerColor.WHITE ? "Branco" : "Preto";
        turnLabel.setText("Turno: " + player);
    }

    private void updateScore(PlayerColor winner) {
        if (winner == PlayerColor.WHITE) {
            whiteScore++;
        } else {
            blackScore++;
        }
        scoreLabel.setText(String.format("Pontuação - Branco: %d, Preto: %d", whiteScore, blackScore));
    }

    private void resetGame() {
        chessMatch = new ChessMatch();
        updateBoard();
        resetSelection();
    }

    private void saveMatch() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Partida de Xadrez");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            Path filePath = fileChooser.getSelectedFile().toPath();
            try {
                ChessSaveUtil.saveMatch(chessMatch, filePath);
                showErrorDialog("Partida salva com sucesso em " + filePath);
            } catch (IOException e) {
                showErrorDialog("Erro ao salvar a partida: " + e.getMessage());
            }
        }
    }

    private void loadMatch() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carregar Partida de Xadrez");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            Path filePath = fileChooser.getSelectedFile().toPath();
            try {
                chessMatch = ChessSaveUtil.loadMatch(filePath);
                updateBoard();
                showErrorDialog("Partida carregada com sucesso de " + filePath);
            } catch (IOException | ClassNotFoundException e) {
                showErrorDialog("Erro ao carregar a partida: " + e.getMessage());
            }
        }
    }

    private void showWinnerDialog(PlayerColor winner) {
        String winnerMessage = "O vencedor é: " + (winner == PlayerColor.WHITE ? "Branco" : "Preto");
        JOptionPane.showMessageDialog(this, winnerMessage, "Vencedor", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class GlassPane extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

}