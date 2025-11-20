package org.example.checkers.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор модульных тестов для проверки корректности работы класса {@link Board}.
 * <p>
 * В тестах проверяются:
 * <ul>
 *     <li>корректность начальной расстановки шашек;</li>
 *     <li>выполнение обычного хода и превращение шашки в дамку;</li>
 *     <li>корректная проверка выхода за границы доски.</li>
 * </ul>
 */
class BoardTest {

    /**
     * Проверяет, что доска корректно инициализируется:
     * <ul>
     *     <li>на верхних рядах присутствуют чёрные шашки;</li>
     *     <li>на нижних рядах присутствуют белые шашки;</li>
     *     <li>центральные клетки остаются пустыми.</li>
     * </ul>
     */
    @Test
    void testInitialSetup() {
        Board board = new Board();
        assertNotNull(board.getPiece(0, 1)); // Черные
        assertNotNull(board.getPiece(5, 0)); // Белые
        assertNull(board.getPiece(3, 3));    // Пустые клетки
    }

    /**
     * Проверяет выполнение хода и превращение шашки в дамку.
     * <p>
     * В тесте:
     * <ul>
     *     <li>вручную устанавливается белая шашка;</li>
     *     <li>выполняется ход на последний ряд;</li>
     *     <li>проверяется, что исходная клетка опустела;</li>
     *     <li>проверяется корректность перемещения;</li>
     *     <li>вызывается {@code tryPromote} и подтверждается превращение в дамку.</li>
     * </ul>
     */
    @Test
    void testApplyMoveAndPromotion() {
        Board board = new Board();
        Piece piece = new Piece(Piece.Color.WHITE);
        board.setPiece(1, 2, piece);
        Move move = new Move(1, 2, 0, 3);

        board.applyMove(move);

        assertNull(board.getPiece(1, 2));
        assertEquals(piece, board.getPiece(0, 3));

        board.tryPromote(0, 3);
        assertTrue(piece.isKing());
    }

    /**
     * Проверяет корректность метода {@link Board#inBounds(int, int)},
     * который определяет, находится ли клетка в пределах доски.
     * <p>
     * В тест входят:
     * <ul>
     *     <li>валидные координаты (0,0) и (7,7);</li>
     *     <li>невалидные координаты — отрицательные и выходящие за пределы размера доски.</li>
     * </ul>
     */
    @Test
    void testInBounds() {
        Board board = new Board();
        assertTrue(board.inBounds(0,0));
        assertTrue(board.inBounds(7,7));
        assertFalse(board.inBounds(-1,0));
        assertFalse(board.inBounds(0,8));
    }
}
