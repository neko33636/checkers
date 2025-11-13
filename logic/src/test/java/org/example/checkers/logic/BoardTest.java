package org.example.checkers.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testInitialSetup() {
        Board board = new Board();
        // Проверка шашек в начальной позиции
        assertNotNull(board.getPiece(0, 1)); // Черные
        assertNotNull(board.getPiece(5, 0)); // Белые
        assertNull(board.getPiece(3, 3));    // Пустые клетки
    }

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

    @Test
    void testInBounds() {
        Board board = new Board();
        assertTrue(board.inBounds(0,0));
        assertTrue(board.inBounds(7,7));
        assertFalse(board.inBounds(-1,0));
        assertFalse(board.inBounds(0,8));
    }
}
