package org.example.checkers.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор модульных тестов для проверки корректности работы класса {@link Piece}.
 * <p>
 * Проверяется:
 * <ul>
 *     <li>создание шашки с указанным цветом;</li>
 *     <li>начальное состояние (не является дамкой);</li>
 *     <li>превращение шашки в дамку методом {@code makeKing()}.</li>
 * </ul>
 */
class PieceTest {

    /**
     * Проверяет корректность создания новой шашки.
     * <p>
     * Убедимся, что:
     * <ul>
     *     <li>цвет устанавливается правильно;</li>
     *     <li>новая шашка не является дамкой.</li>
     * </ul>
     */
    @Test
    void testCreatePiece() {
        Piece piece = new Piece(Piece.Color.WHITE);
        assertEquals(Piece.Color.WHITE, piece.getColor());
        assertFalse(piece.isKing());
    }

    /**
     * Проверяет работу метода {@link Piece#makeKing()},
     * который превращает обычную шашку в дамку.
     * <p>
     * В тесте:
     * <ul>
     *     <li>сначала создаётся обычная шашка;</li>
     *     <li>подтверждается, что она не является дамкой;</li>
     *     <li>вызывается метод превращения;</li>
     *     <li>проверяется, что шашка стала дамкой.</li>
     * </ul>
     */
    @Test
    void testMakeKing() {
        Piece piece = new Piece(Piece.Color.BLACK);
        assertFalse(piece.isKing());
        piece.makeKing();
        assertTrue(piece.isKing());
    }
}
