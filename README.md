# Chess-Game

Este é um projeto de um jogo de xadrez desenvolvido em Java, projetado para oferecer uma experiência rica e interativa
aos usuários. O jogo inclui uma interface gráfica de usuário (GUI) intuitiva para facilitar o jogo de xadrez, bem como
uma interface de linha de comando (console) para uma interação direta e eficiente. Além disso, o projeto segue
princípios de código limpo e incorpora melhores práticas em Java para garantir um código bem estruturado, reutilizável e
fácil de manter.

## Índice

- [Funcionalidades](#funcionalidades)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Descrição das Classes](#descrição-das-classes)
    - [Board](#boardjava)
    - [Piece](#piecejava)
    - [Position](#positionjava)
    - [ChessMatch](#chessmatchjava)
    - [ChessPiece](#chesspiecejava)
    - [ChessPosition](#chesspositionjava)
    - [PlayerColor](#playercolorjava)
    - [Peças de Xadrez](#peças-de-xadrez)
        - [Bishop](#bishopjava)
        - [King](#kingjava)
        - [Knight](#knightjava)
        - [Pawn](#pawnjava)
        - [Queen](#queenjava)
        - [Rook](#rookjava)
    - [Exceptions](#exceptions)
        - [BoardException](#boardexceptionjava)
        - [ChessException](#chessexceptionjava)
    - [Interface Gráfica](#interface-gráfica)
        - [ChessGUI](#chessguijava)
        - [MainMenu](#mainmenujava)
    - [Console](#console)
        - [ChessGame](#chessgamejava)
        - [UserInterface](#userinterfacejava)
    - [Classes Utilitárias](#classes-utilitárias)
        - [ChessSaveUtil](#chesssaveutiljava)
        - [TerminalColor](#terminalcolorjava)
- [Contribuições](#contribuições)
- [Contato](#contato)

## Funcionalidades

- Interface gráfica intuitiva para jogar xadrez.
- Gerenciamento completo das posições e movimentos das peças no tabuleiro.
- Verificação automática de jogadas válidas conforme as regras do xadrez.
- Menu principal para iniciar uma nova partida, carregar jogos salvos ou sair do jogo.
- Suporte para todos os tipos de peças de xadrez, com comportamento específico para cada uma.

## Estrutura do Projeto

O projeto está organizado nos seguintes arquivos principais:

- `ChessGUI.java`: Implementa a interface gráfica do usuário para o jogo de xadrez.
- `MainMenu.java`: Gerencia o menu principal do jogo.
- `ChessMatch.java`: Controla a lógica da partida de xadrez.
- `ChessPiece.java`: Representa uma peça de xadrez.
- `ChessPosition.java`: Representa a posição de uma peça no tabuleiro.
- `Board.java`: Representa o tabuleiro de xadrez.
- `Piece.java`: Classe base para todas as peças de xadrez.
- `Position.java`: Representa uma posição genérica no tabuleiro.

## Descrição das Classes

### Board.java

A classe `Board` representa o tabuleiro de xadrez. Esta classe é responsável por gerenciar as posições das peças no
tabuleiro e por controlar a lógica de posicionamento e movimentação das peças.

#### Principais Responsabilidades:

- **Inicialização do Tabuleiro:** Configura o tabuleiro de xadrez com as peças na posição inicial.
- **Gerenciamento de Peças:** Adiciona, remove e acessa peças no tabuleiro.
- **Verificação de Posições Válidas:** Verifica se uma posição está dentro dos limites do tabuleiro.
- **Movimentação de Peças:** Move peças de uma posição para outra no tabuleiro.
- **Captura de Peças:** Gerencia a lógica de captura de peças adversárias.

#### Atributos Principais:

- **rows:** Número de linhas no tabuleiro.
- **columns:** Número de colunas no tabuleiro.
- **pieces:** Matriz que armazena as peças no tabuleiro.

#### Métodos Principais:

- **Board(int rows, int columns):** Construtor que inicializa o tabuleiro com o número especificado de linhas e colunas.
- **piece(int row, int column):** Retorna a peça na posição especificada.
- **piece(Position position):** Retorna a peça na posição especificada.
- **placePiece(Piece piece, Position position):** Coloca uma peça na posição especificada.
- **removePiece(Position position):** Remove a peça da posição especificada.
- **positionExists(int row, int column):** Verifica se a posição especificada está dentro dos limites do tabuleiro.
- **positionExists(Position position):** Verifica se a posição especificada está dentro dos limites do tabuleiro.
- **thereIsAPiece(Position position):** Verifica se há uma peça na posição especificada.

#### Exemplos de Uso:

##### Inicialização do Tabuleiro

```java
public Board(final int rows, final int columns) {
    if (rows < 1 || columns < 1) {
        throw new BoardException("Erro ao criar o tabuleiro: deve haver pelo menos uma linha e uma coluna. ");
    }
    this.rows = rows;
    this.columns = columns;
    pieces = new Piece[rows][columns];
}
```

##### Adicionar uma Peça ao Tabuleiro

```java
public void placePiece(final Piece piece, final Position position) {
    Objects.requireNonNull(piece, PIECE_CANNOT_BE_NULL);
    Objects.requireNonNull(position, POSITION_CANNOT_BE_NULL);
    if (thereIsAPiece(position)) {
        throw new BoardException(String.format(PIECE_ALREADY_EXIST, position));
    }
    pieces[position.getRow()][position.getColumn()] = piece;
    piece.position = position;
}
```

##### Remover uma Peça do Tabuleiro

```java
public Piece removePiece(final Position position) {
    Objects.requireNonNull(position, POSITION_CANNOT_BE_NULL);
    validatePosition(position);
    if (piece(position) == null) {
        return null;
    }
    var auxiliary = piece(position);
    auxiliary.position = null;
    pieces[position.getRow()][position.getColumn()] = null;
    return auxiliary;
}
```

Esta classe é crucial para gerenciar o estado do tabuleiro de xadrez e para garantir que todas as operações relacionadas
ao posicionamento e movimentação das peças sejam realizadas corretamente.

### Piece.java

A classe `Piece` é a classe base para todas as peças de xadrez. Ela define os atributos e métodos comuns a todas as
peças.

#### Principais Responsabilidades:

- **Atributos Comuns:** Define os atributos comuns a todas as peças, como a posição atual e o tabuleiro ao qual a peça
  pertence.
- **Movimentos Possíveis:** Declara um método abstrato para obter os movimentos possíveis para cada peça, que deve ser
  implementado pelas subclasses.
- **Contagem de Movimentos:** Mantém um contador de quantos movimentos a peça realizou.

#### Atributos Principais:

- **position:** A posição atual da peça no tabuleiro.
- **board:** O tabuleiro ao qual a peça pertence.
- **moveCount:** Contador que registra quantos movimentos a peça já realizou.

#### Métodos Principais:

- **Piece(Board board):** Construtor que inicializa a peça com o tabuleiro fornecido.
- **getPosition():** Retorna a posição atual da peça.
- **getMoveCount():** Retorna o número de movimentos realizados pela peça.
- **increaseMoveCount():** Incrementa o contador de movimentos da peça.
- **decreaseMoveCount():** Decrementa o contador de movimentos da peça.
- **possibleMoves():** Método abstrato que deve ser implementado pelas subclasses para retornar uma matriz booleana
  indicando todos os movimentos possíveis para a peça.
- **isMoveValid(Position position):** Verifica se um movimento para a posição especificada é válido.

Esta classe fornece a estrutura básica para todas as peças de xadrez, permitindo que comportamentos específicos sejam
definidos nas subclasses.

#### Exemplos de Uso

##### Inicialização de uma Peça

```java
public Piece(final Board board) {
    this.board = Objects.requireNonNull(board, "O tabuleiro não pode ser nulo. ");
    position = null;
}
```

##### Verificar se um Movimento é Válido

```java
public boolean isThereAnyPossibleMove() {
    boolean[][] possibleMoves = possibleMoves();
    for (boolean[] row : possibleMoves) {
        for (boolean cell : row) {
            if (cell) {
                return true;
            }
        }
    }
    return false;
}
```

### Position.java

A classe `Position` representa uma posição genérica no tabuleiro de xadrez. Esta classe é utilizada para manipular as
coordenadas das peças no tabuleiro.

#### Principais Responsabilidades:

- **Representação de Coordenadas:** Armazena as coordenadas da posição no tabuleiro.
- **Manipulação de Posições:** Permite a manipulação e comparação de posições no tabuleiro.

#### Atributos Principais:

- **row:** A linha da posição no tabuleiro.
- **column:** A coluna da posição no tabuleiro.

#### Métodos Principais:

- **Position(int row, int column):** Construtor que inicializa a posição com a linha e coluna fornecidas.
- **getRow():** Retorna a linha da posição.
- **getColumn():** Retorna a coluna da posição.
- **setValues(int row, int column):** Define a linha e a coluna da posição.
- **equals(Object obj):** Compara esta posição com outra para verificar se são iguais.
- **hashCode():** Retorna o código hash para esta posição.
- **toString():** Retorna a representação em string da posição no formato "(row, column)".

Esta classe é fundamental para representar e manipular as posições das peças no tabuleiro de xadrez, facilitando a
lógica de movimentação e verificação de posições.

### ChessMatch.java

A classe `ChessMatch` é responsável por controlar a lógica principal da partida de xadrez. Ela gerencia o estado do
jogo, verifica a validade dos movimentos, controla o turno dos jogadores e determina o resultado da partida.

#### Principais Responsabilidades:

- **Inicialização do Jogo:** Configura o tabuleiro com as peças na posição inicial e prepara o jogo para começar.
- **Controle de Turno:** Alterna o turno entre os jogadores brancos e pretos após cada movimento válido.
- **Validação de Movimentos:** Verifica se os movimentos são válidos conforme as regras do xadrez.
- **Movimento das Peças:** Executa os movimentos das peças no tabuleiro, incluindo a captura de peças adversárias.
- **Verificação de Xeque e Xeque-mate:** Determina se um rei está em xeque ou xeque-mate após cada movimento.
- **Histórico de Movimentos:** Mantém um registro dos movimentos realizados durante a partida para permitir
  funcionalidades como desfazer movimento.
- **Encerramento do Jogo:** Determina o resultado da partida, seja por xeque-mate, empate ou outra condição de
  término.

#### Métodos Principais:

- **startGame():** Inicia uma nova partida de xadrez.
- **movePiece(Position source, Position target):** Realiza o movimento de uma peça do ponto de origem para o ponto de
  destino.
- **validateMove(Position source, Position target):** Verifica se um movimento é válido.
- **isCheck():** Verifica se o rei do jogador atual está em xeque.
- **isCheckMate():** Verifica se o rei do jogador atual está em xeque-mate.
- **undoMove():** Desfaz o último movimento realizado.

#### Exemplos de Uso

##### Inicializar uma Nova Partida

```java
public ChessMatch() {
    board = new Board(8, 8);
    turn = 1;
    currentPlayer = PlayerColor.WHITE;
    piecesOnTheBoard = new ArrayList<>();
    capturedPieces = new ArrayList<>();
    setupInitialPieces();
}
```

##### Mover uma Peça

```java
public ChessPiece performChessMove(final ChessPosition sourcePosition, final ChessPosition targetPosition) {
    var source = sourcePosition.toPosition();
    var target = targetPosition.toPosition();
    validateSourcePosition(source);
    validateTargetPosition(source, target);
    var capturedPiece = makeMove(source, target);
    if (testCheck(currentPlayer)) {
        undoMove(source, target, capturedPiece);
        throw new ChessException("Você não pode se colocar em check! ");
    }

    var movedPiece = (ChessPiece) board.piece(target);

    //  #Movimento especial promoted
    promoted = null;
    if (movedPiece instanceof Pawn) {
        if (movedPiece.getColor().equals(PlayerColor.WHITE) && target.getRow() == 0
                || movedPiece.getColor().equals(PlayerColor.BLACK) && target.getRow() == 7) {
            promoted = (ChessPiece) board.piece(target);
            promoted = replacePromotedPiece("Q");
        }
    }

    check = testCheck(opponent(currentPlayer));

    if (testCheckMate(opponent(currentPlayer))) {
        checkMate = true;
    } else {
        nextTurn();
    }

    //  #Movimento especial en passant
    if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
        enPassantVulnerable = movedPiece;
    } else {
        enPassantVulnerable = null;
    }

    return (ChessPiece) capturedPiece;
}
```

##### Validar um Movimento

```java
private void validateSourcePosition(final Position position) {
    if (!board.thereIsAPiece(position)) {
        throw new ChessException("Não há peça na posição de origem. ");
    }
    if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
        throw new ChessException("A peça escolhida não é sua. ");
    }
    if (!board.piece(position).isThereAnyPossibleMove()) {
        throw new ChessException("A peça escolhida não tem movimentos possíveis. ");
    }
}

private void validateTargetPosition(final Position source, final Position target) {
    if (!board.piece(source).possibleMove(target)) {
        throw new ChessException("A peça escolhida não pode mover-se para a posição de destino. ");
    }
}
```

##### Desfazer um Movimento

```java
private void undoMove(final Position source, final Position target, Piece capturedPiece) {
    var piece = (ChessPiece) board.removePiece(target);
    piece.decreaseMoveCount();
    board.placePiece(piece, source);

    if (capturedPiece != null) {
        board.placePiece(capturedPiece, target);
        capturedPieces.remove(capturedPiece);
        piecesOnTheBoard.add(capturedPiece);
    }

    //  Testar castling do lado do rei
    if (piece instanceof King && target.getColumn() == source.getColumn() + 2) {
        var sourceRook = new Position(source.getRow(), source.getColumn() + 3);
        var targetRook = new Position(source.getRow(), source.getColumn() + 1);
        var rook = (ChessPiece) board.removePiece(targetRook);
        board.placePiece(rook, sourceRook);
        rook.decreaseMoveCount();
    }

    //  Testar castling do lado da rainha
    if (piece instanceof King && target.getColumn() == source.getColumn() - 2) {
        var sourceRook = new Position(source.getRow(), source.getColumn() - 4);
        var targetRook = new Position(source.getRow(), source.getColumn() - 1);
        var rook = (ChessPiece) board.removePiece(targetRook);
        board.placePiece(rook, sourceRook);
        rook.decreaseMoveCount();
    }

    //  Testar en passant
    if (piece instanceof Pawn) {
        if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
            var pawn = (ChessPiece) board.removePiece(target);
            Position pawnPosition;
            if (piece.getColor().equals(PlayerColor.WHITE)) {
                pawnPosition = new Position(3, target.getColumn());
            } else {
                pawnPosition = new Position(4, target.getColumn());
            }
            board.placePiece(pawn, pawnPosition);
        }
    }

}
```

##### Verificar Xeque

```java
private boolean testCheck(final PlayerColor playerColor) {
    var kingPosition = kingColor(playerColor).getChessPosition().toPosition();
    var opponentPieceList = piecesOnTheBoard.stream()
            .filter(pieces -> ((ChessPiece) pieces).getColor().equals(opponent(playerColor))).toList();
    for (var piece : opponentPieceList) {
        boolean[][] matrix = piece.possibleMoves();
        if (matrix[kingPosition.getRow()][kingPosition.getColumn()]) {
            return true;
        }
    }
    return false;
}
```

##### Verificar Xeque-mate

```java
private boolean testCheckMate(final PlayerColor playerColor) {
    Objects.requireNonNull(playerColor, "A cor não pode ser nula. ");

    if (!testCheck(playerColor)) {
        return false;
    }

    var pieceList = piecesOnTheBoard.stream()
            .filter(pieces -> ((ChessPiece) pieces).getColor().equals(playerColor)).toList();

    return pieceList.stream().noneMatch(piece -> canPieceAvoidCheck(piece, playerColor));
}

```

### ChessPiece.java

A classe `ChessPiece` representa uma peça de xadrez no tabuleiro. Esta classe é uma extensão da classe `Piece` e contém
atributos e métodos específicos para as peças de xadrez.

#### Principais Responsabilidades:

- **Identificação da Peça:** Armazena informações sobre o tipo e a cor da peça (branca ou preta).
- **Movimentação:** Define as regras de movimento específicas para cada tipo de peça de xadrez.
- **Captura:** Gerencia a captura de peças adversárias.
- **Validação de Movimentos:** Verifica se um movimento é permitido conforme as regras específicas da peça.
- **Primeiro Movimento:** Para algumas peças, como peões e torres, gerencia a lógica do primeiro movimento, que pode ter
  regras especiais (como o roque para as torres ou o movimento inicial duplo dos peões).

#### Atributos Principais:

- **color:** Define a cor da peça (branca ou preta).
- **board:** Referência ao tabuleiro de xadrez onde a peça está posicionada.
- **moveCount:** Contador que registra quantos movimentos a peça já realizou, útil para regras especiais como o roque e
  o movimento inicial dos peões.

#### Métodos Principais:

- **possibleMoves():** Retorna uma matriz booleana indicando todos os movimentos possíveis para a peça de acordo com
  suas regras específicas.
- **move(Position target):** Move a peça para a posição de destino se o movimento for válido.
- **isMoveValid(Position target):** Verifica se o movimento para a posição de destino é válido.
- **incrementMoveCount():** Incrementa o contador de movimentos da peça.
- **decrementMoveCount():** Decrementa o contador de movimentos da peça (usado para desfazer movimentos).

#### Exemplos de Uso

##### Inicialização de uma Peça de Xadrez

```java
public ChessPiece(final Board board, final PlayerColor playerColor) {
    super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo. "));
    this.playerColor = Objects.requireNonNull(playerColor, "A cor não pode ser nula. ");
}
```

##### Verificar se há uma peça adversária na posição especificada.

```java
protected boolean isThereOpponentPiece(final Position position) {
    var piece = (ChessPiece) getBoard().piece(position);
    if (piece != null) {
        return piece.getColor() != playerColor;
    } else {
        return false;
    }
}
```

##### Incrementar e Decrementar o Contador de Movimentos

```java
public void increaseMoveCount() {
    moveCount++;
}

public void decreaseMoveCount() {
    moveCount--;
}
```

### ChessPosition.java

A classe `ChessPosition` representa a posição de uma peça no tabuleiro de xadrez. Esta classe é utilizada para converter
e manipular as coordenadas do tabuleiro de xadrez em termos de coluna (letras) e linha (números).

#### Principais Responsabilidades:

- **Representação da Posição:** Define a posição de uma peça utilizando a notação de coluna (letras de 'a' a 'h') e
  linha (números de 1 a 8).
- **Conversão de Coordenadas:** Converte coordenadas do sistema de notação do xadrez para o sistema de matriz utilizado
  internamente no tabuleiro.
- **Validação de Posição:** Verifica se uma posição está dentro dos limites válidos do tabuleiro de xadrez.

#### Atributos Principais:

- **column:** Representa a coluna da posição (letras de 'a' a 'h').
- **row:** Representa a linha da posição (números de 1 a 8).

#### Métodos Principais:

- **ChessPosition(char column, int row):** Construtor que inicializa a posição com a coluna e linha fornecidas.
- **toPosition():** Converte a posição de xadrez (ChessPosition) para uma posição na matriz (Position) utilizada
  internamente.
- **fromPosition(Position position):** Converte uma posição na matriz (Position) para uma posição de xadrez
  (ChessPosition).
- **toString():** Retorna a representação em string da posição no formato tradicional de xadrez (ex.: 'e4').

#### Exemplos de Uso

##### Inicialização de uma Posição de Xadrez

```java
public ChessPosition {
    if (column < MIN_COLUMN || column > MAX_COLUMN || row < MIN_ROW || row > MAX_ROW) {
        throw new ChessException("Erro ao instanciar ChessPosition. Os valores válidos são de a1 a h8. ");
    }
}
```

##### Converter para uma Posição na Matriz

```java
Position toPosition() {
    return new Position(MAX_ROW - row, column - MIN_COLUMN);
}
```

##### Converter de uma Posição na Matriz para uma Posição de Xadrez

```java
static ChessPosition fromPosition(final Position position) {
    Objects.requireNonNull(position, "A posição não pode ser nula. ");
    return new ChessPosition((char) (MIN_COLUMN + position.getColumn()), MAX_ROW - position.getRow());
}
```

### PlayerColor.java

A classe `PlayerColor` é uma enumeração que define as cores dos jogadores no jogo de xadrez. Esta classe é utilizada
para identificar e diferenciar os dois jogadores (branco e preto) durante a partida.

#### Principais Responsabilidades:

- **Definição de Cores:** Define as duas cores possíveis dos jogadores: branco e preto.
- **Simplificação do Código:** Facilita a identificação e comparação das cores dos jogadores, melhorando a legibilidade
  e manutenção do código.

#### Valores da Enumeração:

- **WHITE:** Representa o jogador com as peças brancas.
- **BLACK:** Representa o jogador com as peças pretas.

#### Uso Comum:

Esta enumeração é utilizada em várias partes do código para atribuir e verificar a cor das peças e determinar o turno do
jogador atual.

```java
public ChessMatch() {
    board = new Board(8, 8);
    turn = 1;
    currentPlayer = PlayerColor.WHITE;
    piecesOnTheBoard = new ArrayList<>();
    capturedPieces = new ArrayList<>();
    setupInitialPieces();
}

public ChessPiece(final Board board, final PlayerColor playerColor) {
    super(Objects.requireNonNull(board, "O tabuleiro não pode ser nulo. "));
    this.playerColor = Objects.requireNonNull(playerColor, "A cor não pode ser nula. ");
}

public ChessPiece performChessMove(final ChessPosition sourcePosition, final ChessPosition targetPosition) {
    //  código acima
    if (movedPiece instanceof Pawn) {
        if (movedPiece.getColor().equals(PlayerColor.WHITE) && target.getRow() == 0
                || movedPiece.getColor().equals(PlayerColor.BLACK) && target.getRow() == 7) {
            promoted = (ChessPiece) board.piece(target);
            promoted = replacePromotedPiece("Q");
        }
    }
    //  código abaixo
}
```

## Peças de Xadrez

### Bishop.java

A classe `Bishop` representa o bispo no jogo de xadrez. Herda de `ChessPiece` e define os movimentos específicos desta
peça.

#### Principais Responsabilidades:

- **Movimentação Diagonal:** O bispo pode se mover qualquer número de casas diagonalmente, contanto que o caminho esteja
  livre.
- **Verificação de Movimentos:** Implementa a lógica para verificar se um movimento é válido conforme as regras de
  movimentação do bispo.

#### Métodos Principais:

- **possibleMoves():** Retorna uma matriz booleana indicando todas as posições válidas para onde o bispo pode se mover.

Esta classe assegura que o bispo se mova corretamente no tabuleiro, respeitando as regras do xadrez.

### King.java

A classe `King` representa o rei no jogo de xadrez. Herda de `ChessPiece` e define os movimentos específicos desta peça.

#### Principais Responsabilidades:

- **Movimentação Limitada:** O rei pode se mover uma casa em qualquer direção (horizontal, vertical ou diagonal).
- **Verificação de Xeque:** Verifica se o movimento coloca o rei em xeque.
- **Roque:** Implementa a lógica do movimento especial de roque, onde o rei e uma torre se movem simultaneamente.

#### Métodos Principais:

- **possibleMoves():** Retorna uma matriz booleana indicando todas as posições válidas para onde o rei pode se mover.
- **isCastlingMove(Position target):** Verifica se um movimento é um roque válido.

Esta classe é crucial para garantir que o rei se mova corretamente e aplique a lógica de xeque e roque.

### Knight.java

A classe `Knight` representa o cavalo no jogo de xadrez. Herda de `ChessPiece` e define os movimentos específicos desta
peça.

#### Principais Responsabilidades:

- **Movimentação em "L":** O cavalo pode se mover em um padrão de "L", que consiste em duas casas em uma direção e uma
  casa perpendicularmente.
- **Salto Sobre Peças:** O cavalo é a única peça que pode pular sobre outras peças.

#### Métodos Principais:

- **possibleMoves():** Retorna uma matriz booleana indicando todas as posições válidas para onde o cavalo pode se mover.

Esta classe assegura que o cavalo se mova corretamente no tabuleiro, respeitando suas regras especiais de movimento.

### Pawn.java

A classe `Pawn` representa o peão no jogo de xadrez. Herda de `ChessPiece` e define os movimentos específicos desta
peça.

#### Principais Responsabilidades:

- **Movimento Inicial:** O peão pode se mover duas casas para frente no seu primeiro movimento.
- **Movimento Normal:** O peão pode se mover uma casa para frente.
- **Captura Diagonal:** O peão pode capturar peças adversárias movendo-se uma casa na diagonal.
- **Promoção:** Implementa a lógica de promoção, onde o peão pode ser promovido a outra peça ao alcançar a última
  fileira.
- **En Passant:** Implementa a captura especial "en passant".

#### Métodos Principais:

- **possibleMoves():** Retorna uma matriz booleana indicando todas as posições válidas para onde o peão pode se mover.
- **isEnPassantMove(Position target):** Verifica se um movimento é uma captura "en passant".

Esta classe é essencial para garantir que o peão se mova corretamente, respeitando todas as suas regras especiais de
movimento.

### Queen.java

A classe `Queen` representa a rainha no jogo de xadrez. Herda de `ChessPiece` e define os movimentos específicos desta
peça.

#### Principais Responsabilidades:

- **Movimentação Versátil:** A rainha pode se mover qualquer número de casas em linha reta (horizontal, vertical ou
  diagonal).
- **Verificação de Movimentos:** Implementa a lógica para verificar se um movimento é válido conforme as regras de
  movimentação da rainha.

#### Métodos Principais:

- **possibleMoves():** Retorna uma matriz booleana indicando todas as posições válidas para onde a rainha pode se mover.

Esta classe assegura que a rainha se mova corretamente no tabuleiro, respeitando as regras do xadrez.

### Rook.java

A classe `Rook` representa a torre no jogo de xadrez. Herda de `ChessPiece` e define os movimentos específicos desta
peça.

#### Principais Responsabilidades:

- **Movimentação Retilínea:** A torre pode se mover qualquer número de casas em linha reta, horizontal ou verticalmente.
- **Roque:** Participa do movimento especial de roque com o rei.
- **Verificação de Movimentos:** Implementa a lógica para verificar se um movimento é válido conforme as regras de
  movimentação da torre.

#### Métodos Principais:

- **possibleMoves():** Retorna uma matriz booleana indicando todas as posições válidas para onde a torre pode se mover.

Esta classe assegura que a torre se mova corretamente no tabuleiro, respeitando as regras do xadrez e permitindo o
movimento especial de roque.

### Exceptions

O projeto inclui exceções personalizadas para tratar erros específicos que podem ocorrer durante uma partida de xadrez.
Estas exceções ajudam a identificar e gerenciar problemas de maneira mais eficaz, proporcionando uma melhor experiência
de depuração e manutenção do código.

#### BoardException.java

A classe `BoardException` é uma exceção personalizada lançada para indicar problemas relacionados ao tabuleiro de
xadrez. Esta classe estende `RuntimeException`.

#### Principais Responsabilidades:

- **Indicação de Erros no Tabuleiro:** Utilizada para sinalizar erros específicos relacionados às operações no
  tabuleiro, como tentativas de acessar posições inválidas ou mover peças de forma ilegal.

#### Exemplos de Uso

```java
public Board(final int rows, final int columns) {
    if (rows < 1 || columns < 1) {
        throw new BoardException("Erro ao criar o tabuleiro: deve haver pelo menos uma linha e uma coluna. ");
    }
    this.rows = rows;
    this.columns = columns;
    pieces = new Piece[rows][columns];
}
```

#### ChessException.java

A classe `ChessException` é uma exceção personalizada lançada para indicar problemas específicos de uma partida de
xadrez. Esta classe estende `BoardException`.

#### Principais Responsabilidades:

- **Indicação de Erros na Partida de Xadrez:** Utilizada para sinalizar erros específicos relacionados às regras e
  operações
  de uma partida de xadrez, como tentativas de fazer movimentos ilegais ou verificar xeque-mate de forma incorreta.

#### Exemplos de Uso

```java
public ChessPosition {
    if (column < MIN_COLUMN || column > MAX_COLUMN || row < MIN_ROW || row > MAX_ROW) {
        throw new ChessException("Erro ao instanciar ChessPosition. Os valores válidos são de a1 a h8. ");
    }
}
```

### Interface Gráfica

A interface gráfica (GUI) permite aos usuários interagir com o jogo de xadrez através de elementos visuais como janelas,
botões e tabuleiros desenhados. Esta abordagem torna o jogo mais acessível e intuitivo, proporcionando uma experiência
visualmente rica e interativa.

#### ChessGUI.java

A classe `ChessGUI` implementa a interface gráfica do usuário (GUI) para o jogo de xadrez. Esta classe é responsável por
criar a janela principal do jogo, exibir o tabuleiro e as peças, e gerenciar as interações do usuário, como movimentos
das peças e ações no menu.

#### Principais Responsabilidades:

- **Inicialização da GUI:** Configura e exibe a janela principal do jogo.
- **Renderização do Tabuleiro:** Desenha o tabuleiro de xadrez e as peças na tela.
- **Interação com o Usuário:** Gerencia cliques do usuário, permitindo a seleção e o movimento das peças.
- **Atualização da Interface:** Atualiza a interface gráfica em resposta a ações do usuário e mudanças no estado do
  jogo.

#### Exemplos de Uso

### Inicializar uma GUI

```java
protected ChessGUI(ChessMatch chessMatch) {
    this.chessMatch = chessMatch;
    preloadPieceIcons();
    setupGUI();
    updateBoard();
}
```

### Pré-carregar os ícones das peças de xadrez e os armazena em um cache.

```java
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
```

### Configurar os componentes da interface gráfica

```java
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
```

### Inicializar o tabuleiro de xadrez.

```java
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
```

### Selecionar a peça na posição especificada.

```java
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
```

### Mover a peça para a posição especificada.

```java
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
```

### Permitir ao jogador escolher uma peça de promoção.

```java
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
```

#### MainMenu.java

A classe `MainMenu` gerencia o menu principal do jogo. Esta classe é responsável por exibir as opções iniciais ao
usuário, como iniciar uma nova partida, carregar um jogo salvo, ou sair do jogo.

#### Principais Responsabilidades:

- **Exibição do Menu:** Apresenta as opções do menu principal ao usuário.
- **Navegação do Menu:** Gerencia a navegação entre diferentes opções e telas do jogo.
- **Configuração Inicial:** Configura o estado inicial do jogo com base na escolha do usuário.

#### Exemplos de Uso

### Inicializar e construir uma GUI

```java
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
```

### Iniciar um novo jogo lançando o ChessGUI e fechando o menu principal.

```java
    private void startNewGame() {
    SwingUtilities.invokeLater(() -> {
        new ChessGUI();
        dispose();
    });
}
```

### Iniciar um novo jogo lançando o ChessGUI e fechando o menu principal.

```java
private void loadGame() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Carregar Partida de Xadrez");
    int userSelection = fileChooser.showOpenDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        Path filePath = fileChooser.getSelectedFile().toPath();
        try {
            ChessMatch chessMatch = ChessSaveUtil.loadMatch(filePath);
            SwingUtilities.invokeLater(() -> {
                new ChessGUI(chessMatch);
                dispose();
            });
        } catch (IOException | ClassNotFoundException e) {
            showErrorDialog("Erro ao carregar a partida: " + e.getMessage());
        }
    }
}
```

### Console

O console é uma interface de linha de comando que permite a interação com o jogo de xadrez através de texto. Diferente
de uma interface gráfica, a interação no console é feita digitando comandos e visualizando o tabuleiro e outras
informações como texto. Esta abordagem é útil para sistemas onde uma interface gráfica não está disponível ou desejada,
e pode ser especialmente útil para testes e desenvolvimento.

#### ChessGame.java

A classe `ChessGame` é a classe principal que inicia o jogo de xadrez no console. Ela é responsável por configurar o
jogo, iniciar a interface do usuário e gerenciar o loop principal do jogo.

#### Principais Responsabilidades:

- **Configuração Inicial:** Configura o jogo de xadrez, incluindo a criação do tabuleiro e das peças.
- **Inicialização da Interface do Usuário:** Inicia a interface do usuário que permite a interação com o jogo via
  console.
- **Loop Principal do Jogo:** Gerencia o loop principal do jogo, onde o estado do jogo é atualizado e as interações do
  usuário são processadas.

#### UserInterface.java

A classe `UserInterface` fornece métodos para interagir com o usuário via console. Esta classe é responsável por exibir
o tabuleiro, capturar a entrada do usuário e exibir mensagens e resultados do jogo.

#### Principais Responsabilidades:

- **Exibição do Tabuleiro:** Desenha o tabuleiro de xadrez no console.
- **Captura de Entrada:** Captura e valida a entrada do usuário para realizar movimentos e outras ações.
- **Exibição de Mensagens:** Exibe mensagens de status, resultados e erros no console.

### Ler uma posição de xadrez a partir da entrada do usuário.

```java
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
```

### Imprimir o tabuleiro de xadrez na tela.

```java
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
```

### Imprimir o tabuleiro de xadrez na tela, destacando os movimentos possíveis

```java
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
```

### Imprimir as peças capturadas, separadas por cor.

```java
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
```

### Classes Utilitárias

As classes utilitárias são usadas no código para agrupar métodos comumente utilizados em várias partes do
projeto, mas que não pertencem a uma classe específica de objeto. Elas ajudam a evitar a repetição de código, facilitam
a manutenção e promovem a reutilização de funções comuns.

As principais vantagens de usar classes utilitárias incluem:

- **Centralização de Funcionalidades Comuns:** Agrupando funções relacionadas em uma única classe, facilita-se a
  manutenção e atualização do código. Alterações podem ser feitas em um único local, refletindo em todas as partes do
  projeto que utilizam esses métodos.
- **Reutilização de Código:** Classes utilitárias permitem a reutilização de métodos comuns em várias partes do projeto,
  reduzindo a duplicação de código e promovendo práticas de desenvolvimento mais limpas.
- **Organização e Clareza:** Ao separar métodos utilitários das classes principais, o código se torna mais organizado e
  legível. As classes de domínio podem se concentrar em suas responsabilidades principais sem serem sobrecarregadas com
  métodos auxiliares.

#### ChessSaveUtil.java

A classe `ChessSaveUtil` fornece métodos utilitários para salvar e carregar o estado do jogo de xadrez. Esta classe é
responsável por serializar e desserializar o estado do tabuleiro e das peças para permitir que o jogo seja salvo e
retomado posteriormente.

#### Principais Responsabilidades:

- **Salvar o Jogo:** Serializa o estado atual do tabuleiro e das peças para um arquivo.
- **Carregar o Jogo:** Desserializa o estado do tabuleiro e das peças a partir de um arquivo salvo.

#### Exemplos de Uso

##### Salvar o Estado do Jogo

```java
public static void saveMatch(ChessMatch match, Path filePath) throws IOException {
    try (var fileOut = new FileOutputStream(filePath.toFile());
         var out = new ObjectOutputStream(fileOut)) {
        out.writeObject(match);
        System.out.println("Partida de xadrez salva em " + filePath);
    }
}
```

##### Carregar o Estado do Jogo

```java
public static ChessMatch loadMatch(Path filePath) throws IOException, ClassNotFoundException {
    try (var fileIn = new FileInputStream(filePath.toFile());
         var objectInputStream = new ObjectInputStream(fileIn)) {
        var match = (ChessMatch) objectInputStream.readObject();
        System.out.println("Partida de xadrez carregada de " + filePath);
        return match;
    }
}
```

#### TerminalColor.java

A classe `TerminalColor` fornece métodos utilitários para adicionar cores ao texto exibido no terminal. Esta classe é
utilizada para melhorar a legibilidade e a apresentação das mensagens de saída no terminal.

#### Principais Responsabilidades:

- **Adicionar Cores ao Texto:** Aplica cores específicas ao texto exibido no terminal para diferenciar tipos de
  mensagens.
- **Melhorar a Legibilidade:** Utiliza cores para tornar as mensagens de erro, aviso e informação mais visíveis e fáceis
  de entender.

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests para melhorar este projeto.

## Contato

Para mais informações ou dúvidas sobre o projeto, entre em contato:

- **Email:** [luis.santana.profissional@gmail.com](mailto:luis.santana.profissional@gmail.com)
- **LinkedIn:** [Luis Gabriel Santana](https://www.linkedin.com/in/luisgabrielsantana/)