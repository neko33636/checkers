package org.example.checkers.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    void testMoveWithoutCapture() {
        Move move = new Move(2, 3, 3, 4);
        assertEquals(2, move.fromR());
        assertEquals(3, move.fromC());
        assertEquals(3, move.toR());
        assertEquals(4, move.toC());
        assertFalse(move.isCapture());
    }

    @Test
    void testMoveWithCapture() {
        Move move = new Move(2, 3, 4, 5, true, 3, 4);
        assertTrue(move.isCapture());
        assertEquals(3, move.capturedR());
        assertEquals(4, move.capturedC());
    }
}
