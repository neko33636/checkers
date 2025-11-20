package org.example.checkers.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор модульных тестов для проверки корректности работы класса {@link Move}.
 * <p>
 * В тестах проверяется:
 * <ul>
 *     <li>создание обычного хода (без взятия);</li>
 *     <li>создание хода с захватом шашки;</li>
 *     <li>корректность всех координат и признака захвата.</li>
 * </ul>
 */
class MoveTest {

    /**
     * Проверяет создание обычного хода без взятия шашки.
     * <p>
     * В тесте:
     * <ul>
     *     <li>создаётся объект {@link Move} с координатами;</li>
     *     <li>проверяется, что координаты источника и назначения установлены правильно;</li>
     *     <li>подтверждается, что {@link Move#isCapture()} возвращает {@code false}.</li>
     * </ul>
     */
    @Test
    void testMoveWithoutCapture() {
        Move move = new Move(2, 3, 3, 4);
        assertEquals(2, move.fromR());
        assertEquals(3, move.fromC());
        assertEquals(3, move.toR());
        assertEquals(4, move.toC());
        assertFalse(move.isCapture());
    }

    /**
     * Проверяет создание хода с захватом шашки.
     * <p>
     * В тесте:
     * <ul>
     *     <li>создаётся объект {@link Move} с координатами хода и захвата;</li>
     *     <li>подтверждается, что {@link Move#isCapture()} возвращает {@code true};</li>
     *     <li>проверяются координаты захваченной шашки ({@link Move#capturedR()}, {@link Move#capturedC()}).</li>
     * </ul>
     */
    @Test
    void testMoveWithCapture() {
        Move move = new Move(2, 3, 4, 5, true, 3, 4);
        assertTrue(move.isCapture());
        assertEquals(3, move.capturedR());
        assertEquals(4, move.capturedC());
    }
}
