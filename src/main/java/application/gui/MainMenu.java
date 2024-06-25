package src.main.java.application.gui;

import src.main.java.chess.ChessMatch;
import src.main.java.utils.ChessSaveUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;


/**
 * A classe MainMenu representa o menu principal do aplicativo de jogo de xadrez.
 * Ela fornece opções para iniciar um novo jogo, carregar um jogo existente ou sair do aplicativo.
 */
public class MainMenu extends JFrame {

    /**
     * Constrói o MainMenu e inicializa os componentes da interface gráfica.
     */
    public MainMenu() {
        setTitle("Menu do Jogo de Xadrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Jogo de Xadrez", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton newGameButton = new JButton("Novo Jogo");
        JButton loadGameButton = new JButton("Carregar Partida");
        JButton exitButton = new JButton("Sair");

        styleButton(newGameButton, new Color(0, 128, 0));
        styleButton(loadGameButton, new Color(0, 128, 255));
        styleButton(exitButton, Color.RED);

        newGameButton.addActionListener(e -> startNewGame());
        loadGameButton.addActionListener(e -> loadGame());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(newGameButton);
        buttonPanel.add(loadGameButton);
        buttonPanel.add(exitButton);

        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setWindowSizeAndLocation();
        setVisible(true);
    }

    /**
     * Estiliza um botão com uma cor de fundo especificada.
     *
     * @param button o JButton a ser estilizado
     * @param color  a cor de fundo a ser definida
     */
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    /**
     * Define o tamanho da janela e centraliza-a na tela.
     */
    private void setWindowSizeAndLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 3, screenSize.height / 3);
        setLocationRelativeTo(null);
    }

    /**
     * Inicia um novo jogo lançando o ChessGUI e fechando o menu principal.
     */
    private void startNewGame() {
        showLoadingScreen("Carregando novo jogo...");
        CompletableFuture.runAsync(() -> {
            ChessGUI chessGUI = new ChessGUI();
            SwingUtilities.invokeLater(() -> {
                chessGUI.setVisible(true);
                dispose();
            });
        });
    }

    /**
     * Carrega um jogo existente a partir de um arquivo escolhido pelo usuário.
     * Exibe um diálogo de erro se o arquivo não puder ser carregado.
     */
    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carregar Partida de Xadrez");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            Path filePath = fileChooser.getSelectedFile().toPath();
            showLoadingScreen("Carregando partida...");
            CompletableFuture.runAsync(() -> {
                try {
                    ChessMatch chessMatch = ChessSaveUtil.loadMatch(filePath);
                    SwingUtilities.invokeLater(() -> {
                        new ChessGUI(chessMatch).setVisible(true);
                        dispose();
                    });
                } catch (IOException | ClassNotFoundException e) {
                    SwingUtilities.invokeLater(() -> showErrorDialog("Erro ao carregar a partida: " + e.getMessage()));
                }
            });
        }
    }

    /**
     * Exibe um diálogo de erro com uma mensagem especificada.
     *
     * @param message a mensagem de erro a ser exibida
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Exibe uma tela de carregamento com uma mensagem especificada.
     *
     * @param message a mensagem a ser exibida na tela de carregamento
     */
    private void showLoadingScreen(String message) {
        JDialog loadingDialog = new JDialog(this, "Aguarde", true);
        JLabel loadingLabel = new JLabel(message, SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loadingDialog.add(loadingLabel);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);
        SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000); // Simula o tempo de carregamento
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SwingUtilities.invokeLater(loadingDialog::dispose);
        });
    }

    /**
     * O método principal para lançar o menu principal do aplicativo de jogo de xadrez.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }

}