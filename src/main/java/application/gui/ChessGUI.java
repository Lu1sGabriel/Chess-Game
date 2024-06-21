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

    /**
     * Constrói uma instância de ChessGUI com uma nova partida de xadrez.
     */
    protected ChessGUI() {
        this(new ChessMatch());
    }

    /**
     * Constrói uma instância de ChessGUI com a partida de xadrez especificada.
     *
     * @param chessMatch a instância de ChessMatch a ser usada por esta GUI
     */
    protected ChessGUI(ChessMatch chessMatch) {
        this.chessMatch = chessMatch;
        preloadPieceIcons();
        setupGUI();
        updateBoard();
    }

    /**
     * Pré-carrega os ícones das peças de xadrez e os armazena em um cache.
     */
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

    /**
     * Configura os componentes da interface gráfica do usuário para o jogo de xadrez.
     */
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

    /**
     * Estiliza o rótulo de turno.
     */
    private void styleTurnLabel() {
        turnLabel.setFont(new Font("Arial", Font.BOLD, 24));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(new Color(50, 50, 50));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    /**
     * Estiliza o rótulo de pontuação.
     */
    private void styleScoreLabel() {
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(new Color(50, 50, 50));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    /**
     * Estiliza um botão com a cor especificada.
     *
     * @param button o botão a ser estilizado
     * @param color  a cor a ser aplicada ao botão
     */
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

    /**
     * Define o tamanho e a localização da janela.
     */
    private void setWindowSizeAndLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, (int) (screenSize.height / 1.5));
        setLocationRelativeTo(null);
    }

    /**
     * Inicializa o tabuleiro de xadrez.
     */
    private void initializeBoard() {
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        boardPanel.add(new JLabel("")); // canto superior esquerdo

        // Adiciona rótulos das colunas
        for (int col = 0; col < BOARD_SIZE; col++) {
            JLabel label = createLabel(String.valueOf((char) ('a' + col)));
            boardPanel.add(label);
        }

        for (int row = 0; row < BOARD_SIZE; row++) {
            // Adiciona rótulos das linhas
            JLabel label = createLabel(String.valueOf(BOARD_SIZE - row));
            boardPanel.add(label);

            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton button = createBoardButton(row, col);
                boardSquares[row][col] = button;
                boardPanel.add(button);
            }
        }
    }

    /**
     * Cria um rótulo com o texto especificado.
     *
     * @param text o texto do rótulo
     * @return o rótulo criado
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    /**
     * Cria um botão do tabuleiro de xadrez para a posição especificada.
     *
     * @param row a linha do botão
     * @param col a coluna do botão
     * @return o botão criado
     */
    private JButton createBoardButton(int row, int col) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        button.setFocusPainted(false);
        button.addActionListener(e -> handleButtonClick(row, col));
        setButtonColor(button, row, col);
        return button;
    }

    /**
     * Lida com o clique em um botão do tabuleiro.
     *
     * @param row a linha do botão clicado
     * @param col a coluna do botão clicado
     */
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

    /**
     * Seleciona a peça na posição especificada.
     *
     * @param row a linha da peça
     * @param col a coluna da peça
     */
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

    /**
     * Move a peça para a posição especificada.
     *
     * @param row a linha de destino
     * @param col a coluna de destino
     */
    private void movePiece(int row, int col) {
        ChessPosition targetPosition = new ChessPosition((char) ('a' + col), 8 - row);
        try {
            chessMatch.performChessMove(sourcePosition, targetPosition);
            if (chessMatch.getPromoted() != null) {
                String pieceType = choosePromotionPiece(chessMatch.getCurrentPlayer());
                chessMatch.replacePromotedPiece(pieceType);
            }
            updateBoard();
            if (chessMatch.getCheck()) {
                showErrorDialog("Cheque! Você deve proteger seu rei!");
            }
            if (chessMatch.getCheckMate()) {
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

    /**
     * Permite ao jogador escolher uma peça de promoção.
     *
     * @param playerColor a cor do jogador
     * @return o tipo de peça escolhida
     */
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

    /**
     * Obtém os caminhos dos ícones das peças para a cor do jogador especificada.
     *
     * @param playerColor a cor do jogador
     * @return uma matriz de caminhos dos ícones das peças
     */
    private static String[] getPieceIconPaths(PlayerColor playerColor) {
        String colorPrefix = playerColor != PlayerColor.WHITE ? "white" : "black";
        return new String[]{
                String.format("%s%s-queen.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-rook.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-bishop.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-knight.png", IMAGE_BASE_PATH, colorPrefix)
        };
    }

    /**
     * Ação para cancelar a seleção da peça.
     */
    private void cancelAction() {
        resetSelection();
        cancelButton.setEnabled(false);
    }

    /**
     * Redefine a seleção da peça.
     */
    private void resetSelection() {
        sourcePosition = null;
        possibleMoves = null;
        updateBoard();
        cancelButton.setEnabled(false);
    }

    /**
     * Exibe um diálogo de erro com a mensagem especificada.
     *
     * @param message a mensagem de erro
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Atualiza o tabuleiro de xadrez.
     */
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

    /**
     * Atualiza uma casa do tabuleiro de xadrez com a peça especificada.
     *
     * @param row   a linha da casa
     * @param col   a coluna da casa
     * @param piece a peça a ser exibida na casa
     */
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

    /**
     * Define a cor do botão com base na posição.
     *
     * @param button o botão a ser colorido
     * @param row    a linha do botão
     * @param col    a coluna do botão
     */
    private void setButtonColor(JButton button, int row, int col) {
        button.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
    }

    /**
     * Obtém o ícone da peça de xadrez especificada.
     *
     * @param piece a peça de xadrez
     * @return o ícone da peça
     */
    private Icon getPieceIcon(ChessPiece piece) {
        String pieceName = piece.getClass().getSimpleName().toLowerCase();
        String color = piece.getColor() == PlayerColor.WHITE ? "white" : "black";
        String key = color + "_" + pieceName;
        return pieceIconCache.get(key);
    }

    /**
     * Carrega uma imagem a partir de uma URL.
     *
     * @param imageURL a URL da imagem
     * @return o ícone da imagem carregada
     */
    private ImageIcon loadImage(URL imageURL) {
        try {
            BufferedImage originalImage = ImageIO.read(imageURL);
            Image scaledImage = originalImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Atualiza o rótulo do turno para exibir o jogador atual.
     */
    private void updateTurnLabel() {
        PlayerColor currentPlayer = chessMatch.getCurrentPlayer();
        String player = currentPlayer == PlayerColor.WHITE ? "Branco" : "Preto";
        turnLabel.setText("Turno: " + player);
    }

    /**
     * Atualiza a pontuação do jogo com base no vencedor.
     *
     * @param winner a cor do jogador vencedor
     */
    private void updateScore(PlayerColor winner) {
        if (winner == PlayerColor.WHITE) {
            whiteScore++;
        } else {
            blackScore++;
        }
        scoreLabel.setText(String.format("Pontuação - Branco: %d, Preto: %d", whiteScore, blackScore));
    }

    /**
     * Redefine o jogo de xadrez para uma nova partida.
     */
    private void resetGame() {
        chessMatch = new ChessMatch();
        updateBoard();
        resetSelection();
    }

    /**
     * Salva a partida de xadrez atual.
     */
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

    /**
     * Carrega uma partida de xadrez salva.
     */
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

    /**
     * Exibe um diálogo com a mensagem do vencedor.
     *
     * @param winner a cor do jogador vencedor
     */
    private void showWinnerDialog(PlayerColor winner) {
        String winnerMessage = "O vencedor é: " + (winner == PlayerColor.WHITE ? "Branco" : "Preto");
        JOptionPane.showMessageDialog(this, winnerMessage, "Vencedor", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Classe interna para representar um painel de vidro translúcido.
     */
    private static class GlassPane extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

}