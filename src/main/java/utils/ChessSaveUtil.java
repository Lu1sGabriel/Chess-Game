package src.main.java.utils;

import src.main.java.chess.ChessMatch;

import java.io.*;
import java.nio.file.Path;

/**
 * Utilitário para salvar e carregar partidas de xadrez.
 * <p>
 * Esta classe implementa Serializable para permitir que o estado do objeto
 * seja salvo e carregado de um arquivo, ou transmitido pela rede.
 */
public class ChessSaveUtil implements Serializable {

    /**
     * Identificador de versão da classe para fins de serialização.
     * <p>
     * Este identificador é utilizado pelo mecanismo de serialização
     * para assegurar que a versão da classe que está sendo serializada
     * seja compatível com a versão da classe que está sendo desserializada.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Salva a partida de xadrez especificada no arquivo indicado pelo caminho.
     *
     * @param match    a partida de xadrez a ser salva
     * @param filePath o caminho do arquivo onde a partida será salva
     * @throws IOException se ocorrer um erro de E/S ao salvar a partida
     */
    public static void saveMatch(ChessMatch match, Path filePath) throws IOException {
        try (var fileOut = new FileOutputStream(filePath.toFile());
             var out = new ObjectOutputStream(fileOut)) {
            out.writeObject(match);
            System.out.println("Partida de xadrez salva em " + filePath);
        }
    }

    /**
     * Carrega uma partida de xadrez do arquivo indicado pelo caminho.
     *
     * @param filePath o caminho do arquivo de onde a partida será carregada
     * @return a partida de xadrez carregada
     * @throws IOException            se ocorrer um erro de E/S ao carregar a partida
     * @throws ClassNotFoundException se a classe da partida de xadrez não for encontrada
     */
    public static ChessMatch loadMatch(Path filePath) throws IOException, ClassNotFoundException {
        try (var fileIn = new FileInputStream(filePath.toFile());
             var objectInputStream = new ObjectInputStream(fileIn)) {
            var match = (ChessMatch) objectInputStream.readObject();
            System.out.println("Partida de xadrez carregada de " + filePath);
            return match;
        }
    }

}