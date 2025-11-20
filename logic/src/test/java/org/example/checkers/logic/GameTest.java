package org.example.checkers.logic;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор модульных тестов для проверки корректности работы класса {@link Game}.
 * <p>
 * В тестах проверяется:
 * <ul>
 *     <li>начальный игрок;</li>
 *     <li>наличие легальных ходов;</li>
 *     <li>выполнение обычного хода и смена игрока;</li>
 *     <li>выполнение захвата и удаление побитой шашки;</li>
 *     <li>определение конца игры.</li>
 * </ul>
 */
class GameTest {

    /**
     * Проверяет, что начальный игрок в игре установлен корректно.
     * <p>
     * В стандартной расстановке первый ход делает белый игрок.
     */
    @Test
    void testInitialPlayer() {
        Game game = new Game();
        assertEquals(Piece.Color.WHITE, game.getCurrentPlayer());
    }

    /**
     * Проверяет, что после инициализации игры существуют легальные ходы.
     * <p>
     * Используется метод {@link Game#getAllLegalMoves()}.
     */
    @Test
    void testLegalMovesExist() {
        Game game = new Game();
        List<Move> moves = game.getAllLegalMoves();
        assertFalse(moves.isEmpty());
    }

    /**
     * Проверяет выполнение обычного хода без захвата.
     * <p>
     * В тесте:
     * <ul>
     *     <li>берётся первый доступный ход;</li>
     *     <li>вызывается {@link Game#makeMove(Move)};</li>
     *     <li>проверяется успешность выполнения хода;</li>
     *     <li>проверяется смена игрока после хода.</li>
     * </ul>
     */
    @Test
    void testMakeMoveNormal() {
        Game game = new Game();
        List<Move> moves = game.getAllLegalMoves();
        Move move = moves.get(0);
        boolean ok = game.makeMove(move);
        assertTrue(ok);
        assertNotEquals(game.getCurrentPlayer(), Piece.Color.WHITE); // смена игрока
    }

    /**
     * Проверяет выполнение хода с захватом шашки.
     * <p>
     * В тесте:
     * <ul>
     *     <li>на доску ставятся белая и черная шашки;</li>
     *     <li>формируется ход с захватом;</li>
     *     <li>проверяется, что ход присутствует среди легальных ходов;</li>
     *     <li>выполняется ход, проверяется успешность и удаление побитой шашки.</li>
     * </ul>
     */
    @Test
    void testCaptureMove() {
        Game game = new Game();
        Board board = game.getBoard();
        Piece w = new Piece(Piece.Color.WHITE);
        Piece b = new Piece(Piece.Color.BLACK);
        board.setPiece(5,0, w);
        board.setPiece(4,1, b);

        Move capture = new Move(5,0,3,2,true,4,1);
        List<Move> legal = game.getLegalMovesForPiece(5,0);
        assertTrue(legal.contains(capture));

        boolean ok = game.makeMove(capture);
        assertTrue(ok);
        assertNull(board.getPiece(4,1));
    }

    /**
     * Проверяет определение конца игры.
     * <p>
     * В тесте:
     * <ul>
     *     <li>удаляются все шашки черного игрока с доски;</li>
     *     <li>выполняется любой ход белого игрока;</li>
     *     <li>проверяется, что метод {@link Game#isGameOver()} возвращает {@code true}.</li>
     * </ul>
     */
    @Test
    void testGameOver() {
        Game game = new Game();
        Board board = game.getBoard();
        for (int r=0; r<Board.SIZE; r++)
            for (int c=0; c<Board.SIZE; c++)
                if (board.getPiece(r,c) != null && board.getPiece(r,c).getColor() == Piece.Color.BLACK)
                    board.removePiece(r,c);

        game.makeMove(new Move(5,0,4,1));
        assertTrue(game.isGameOver());
    }
}
