package org.example.checkers.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    @Test
    void testCreatePiece() {
        Piece piece = new Piece(Piece.Color.WHITE);
        assertEquals(Piece.Color.WHITE, piece.getColor());
        assertFalse(piece.isKing());
    }

    @Test
    void testMakeKing() {
        Piece piece = new Piece(Piece.Color.BLACK);
        assertFalse(piece.isKing());
        piece.makeKing();
        assertTrue(piece.isKing());
    }
}
