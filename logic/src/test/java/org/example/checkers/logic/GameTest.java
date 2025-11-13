package org.example.checkers.logic;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testInitialPlayer() {
        Game game = new Game();
        assertEquals(Piece.Color.WHITE, game.getCurrentPlayer());
    }

    @Test
    void testLegalMovesExist() {
        Game game = new Game();
        List<Move> moves = game.getAllLegalMoves();
        assertFalse(moves.isEmpty());
    }

    @Test
    void testMakeMoveNormal() {
        Game game = new Game();
        List<Move> moves = game.getAllLegalMoves();
        Move move = moves.get(0);
        boolean ok = game.makeMove(move);
        assertTrue(ok);
        assertNotEquals(game.getCurrentPlayer(), Piece.Color.WHITE); // смена игрока
    }

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
