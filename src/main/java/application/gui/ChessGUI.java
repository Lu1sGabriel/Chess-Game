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
import java.util.concurrent.CompletableFuture;

/**
 * Classe responsável por exibir a interface gráfica do usuário (GUI) para o jogo de xadrez.
 * <p>
 * Esta classe estende JFrame e configura os componentes gráficos para
 * exibir o tabuleiro de xadrez, os controles do jogo e as informações do estado do jogo.
 */
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
     * Construtor que inicializa a interface gráfica do jogo de xadrez com uma nova partida.
     */
    protected ChessGUI() {
        this(new ChessMatch());
    }

    /**
     * Construtor que inicializa a interface gráfica do jogo de xadrez com uma partida existente.
     *
     * @param chessMatch A partida de xadrez a ser exibida na interface gráfica.
     */
    protected ChessGUI(ChessMatch chessMatch) {
        this.chessMatch = chessMatch;
        preloadPieceIcons().thenRun(() -> {
            setupGUI();
            updateBoard();
        });
    }

    /**
     * Pré-carrega os ícones das peças de xadrez para otimizar a performance.
     *
     * @return Um CompletableFuture que será concluído após o carregamento dos ícones.
     */
    private CompletableFuture<Void> preloadPieceIcons() {
        return CompletableFuture.runAsync(() -> {
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
        });
    }

    /**
     * Configura a interface gráfica do usuário.
     */
    private void setupGUI() {
        SwingUtilities.invokeLater(() -> {
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
        });
    }

    /**
     * Aplica estilo ao rótulo de turno.
     */
    private void styleTurnLabel() {
        turnLabel.setFont(new Font("Arial", Font.BOLD, 24));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(new Color(50, 50, 50));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    /**
     * Aplica estilo ao rótulo de pontuação.
     */
    private void styleScoreLabel() {
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(new Color(50, 50, 50));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    /**
     * Aplica estilo aos botões.
     *
     * @param button O botão a ser estilizado.
     * @param color  A cor de fundo do botão.
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
     * Define o tamanho da janela e a posiciona no centro da tela.
     */
    private void setWindowSizeAndLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, (int) (screenSize.height / 1.5));
        setLocationRelativeTo(null);
    }

    /**
     * Inicializa o tabuleiro de xadrez na interface gráfica.
     */
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

    /**
     * Cria um rótulo com o texto especificado.
     *
     * @param text O texto do rótulo.
     * @return O rótulo criado.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    /**
     * Cria um botão para uma posição específica no tabuleiro.
     *
     * @param row A linha do botão.
     * @param col A coluna do botão.
     * @return O botão criado.
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
     * Trata o evento de clique em um botão do tabuleiro.
     *
     * @param row A linha do botão clicado.
     * @param col A coluna do botão clicado.
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
     * Seleciona uma peça no tabuleiro para movimentação.
     *
     * @param row A linha da peça a ser selecionada.
     * @param col A coluna da peça a ser selecionada.
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
     * Move uma peça para a posição especificada.
     *
     * @param row A linha de destino.
     * @param col A coluna de destino.
     */
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

    /**
     * Exibe um diálogo para o jogador escolher a peça de promoção.
     *
     * @param playerColor A cor do jogador.
     * @return O tipo de peça escolhido para a promoção.
     */
    private String choosePromotionPiece(PlayerColor playerColor) {
        JDialog promotionDialog = new JDialog(this, "Escolher Peça de Promoção", true);
        promotionDialog.setLayout(new GridLayout(2, 2));
        promotionDialog.setSize(500, 450);
        promotionDialog.setLocationRelativeTo(this);

        String[] pieceIcons = getPieceIconPaths(playerColor);
        String[] pieceCodes = {"Queen", "Rook", "Bishop", "Knight"};

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
     * Retorna os caminhos dos ícones das peças de promoção.
     *
     * @param playerColor A cor do jogador.
     * @return Um array com os caminhos dos ícones das peças de promoção.
     */
    private static String[] getPieceIconPaths(PlayerColor playerColor) {
        String colorPrefix = playerColor == PlayerColor.WHITE ? "white" : "black";
        return new String[]{
                String.format("%s%s-queen.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-rook.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-bishop.png", IMAGE_BASE_PATH, colorPrefix),
                String.format("%s%s-knight.png", IMAGE_BASE_PATH, colorPrefix)
        };
    }

    /**
     * Cancela a ação atual e redefine a seleção.
     */
    private void cancelAction() {
        resetSelection();
        cancelButton.setEnabled(false);
    }

    /**
     * Redefine a seleção de peças no tabuleiro.
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
     * @param message A mensagem de erro a ser exibida.
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Atualiza o estado do tabuleiro de xadrez na interface gráfica.
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
     * Atualiza uma casa específica do tabuleiro de xadrez na interface gráfica.
     *
     * @param row   A linha da casa.
     * @param col   A coluna da casa.
     * @param piece A peça a ser exibida na casa.
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
     * Define a cor de fundo de um botão do tabuleiro.
     *
     * @param button O botão a ser colorido.
     * @param row    A linha do botão.
     * @param col    A coluna do botão.
     */
    private void setButtonColor(JButton button, int row, int col) {
        button.setBackground((row + col) % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
    }

    /**
     * Retorna o ícone da peça especificada.
     *
     * @param piece A peça de xadrez.
     * @return O ícone da peça.
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
     * @param imageURL A URL da imagem.
     * @return A imagem carregada como ImageIcon.
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
     * Atualiza o rótulo de turno com o jogador atual.
     */
    private void updateTurnLabel() {
        PlayerColor currentPlayer = chessMatch.getCurrentPlayer();
        String player = currentPlayer == PlayerColor.WHITE ? "Branco" : "Preto";
        turnLabel.setText("Turno: " + player);
    }

    /**
     * Atualiza a pontuação do jogo com base no vencedor.
     *
     * @param winner O jogador vencedor.
     */
    private void updateScore(PlayerColor winner) {
        if (winner != PlayerColor.WHITE) {
            whiteScore++;
        } else {
            blackScore++;
        }
        scoreLabel.setText(String.format("Pontuação - Branco: %d, Preto: %d", whiteScore, blackScore));
    }

    /**
     * Reinicia o jogo de xadrez.
     */
    private void resetGame() {
        chessMatch = new ChessMatch();
        updateBoard();
        resetSelection();
    }

    /**
     * Salva a partida de xadrez atual em um arquivo.
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
     * Carrega uma partida de xadrez de um arquivo salvo.
     */
    private void loadMatch() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carregar Partida de Xadrez");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            Path filePath = fileChooser.getSelectedFile().toPath();
            try {
                chessMatch = ChessSaveUtil.loadMatch(filePath);
                preloadPieceIcons().thenRun(this::updateBoard);
                showErrorDialog("Partida carregada com sucesso de " + filePath);
            } catch (IOException | ClassNotFoundException e) {
                showErrorDialog("Erro ao carregar a partida: " + e.getMessage());
            }
        }
    }

    /**
     * Exibe um diálogo com o vencedor da partida.
     *
     * @param winner O jogador vencedor.
     */
    private void showWinnerDialog(PlayerColor winner) {
        if (winner == null) {
            JOptionPane.showMessageDialog(this, "Empate!", "Vencedor", JOptionPane.INFORMATION_MESSAGE);
        } else if (winner != PlayerColor.WHITE) {
            JOptionPane.showMessageDialog(this, "O vencedor é: Branco", "Vencedor", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "O vencedor é: Preto", "Vencedor", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Classe interna para desenhar um painel de vidro semitransparente.
     */
    private static class GlassPane extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

}