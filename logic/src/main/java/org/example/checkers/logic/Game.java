package org.example.checkers.logic;

import java.util.ArrayList;
import java.util.List;


public class Game {
    private Board board;
    private Piece.Color currentPlayer;
    private boolean gameOver;
    private int continueR = -1, continueC = -1;

    public Game() {
        board = new Board();
        currentPlayer = Piece.Color.WHITE;
        gameOver = false;
    }

    public Board getBoard() { return board; }
    public Piece.Color getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return gameOver; }

    public List<Move> getAllLegalMoves() {
        List<Move> captures = new ArrayList<>();
        List<Move> normal = new ArrayList<>();

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Piece p = board.getPiece(r, c);
                if (p == null || p.getColor() != currentPlayer) continue;
                List<Move> ms = getLegalMovesForPiece(r, c);
                for (Move m : ms) {
                    if (m.isCapture()) captures.add(m);
                    else normal.add(m);
                }
            }
        }

        return captures.isEmpty() ? normal : captures;
    }


    public List<Move> getLegalMovesForPiece(int r, int c) {
        List<Move> res = new ArrayList<>();
        Piece p = board.getPiece(r, c);
        if (p == null) return res;
        if (p.getColor() != currentPlayer) return res;

        int[][] dirs;
        if (p.isKing()) {
            dirs = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}};
        } else if (p.getColor() == Piece.Color.WHITE) {
            dirs = new int[][]{{-1,1},{-1,-1}}; // белые идут вверх (уменьшаем r)
        } else {
            dirs = new int[][]{{1,1},{1,-1}}; // чёрные идут вниз
        }

        // сначала поиск захватов
        for (int[] d : dirs) {
            int midR = r + d[0];
            int midC = c + d[1];
            int toR = r + 2*d[0];
            int toC = c + 2*d[1];
            if (board.inBounds(toR, toC) && board.getPiece(toR, toC) == null) {
                Piece mid = board.getPiece(midR, midC);
                if (mid != null && mid.getColor() != p.getColor()) {
                    res.add(new Move(r, c, toR, toC, true, midR, midC));
                }
            }
        }

        if (!res.isEmpty()) return res;

        for (int[] d : dirs) {
            int toR = r + d[0];
            int toC = c + d[1];
            if (board.inBounds(toR, toC) && board.getPiece(toR, toC) == null) {
                res.add(new Move(r, c, toR, toC));
            }
        }
        return res;
    }


    public boolean makeMove(Move m) {
        if (gameOver) return false;

        if (continueR != -1) {
            if (m.fromR() != continueR || m.fromC() != continueC) return false;
        }

        List<Move> legal = getAllLegalMoves();
        boolean ok = false;
        for (Move lm : legal) {
            if (movesEqual(lm, m)) { ok = true; break; }
        }
        if (!ok) return false;

        boolean wasCapture = board.applyMove(m);
        board.tryPromote(m.toR(), m.toC());

        if (wasCapture) {
            Piece moved = board.getPiece(m.toR(), m.toC());
            List<Move> more = getLegalCapturesForPiece(m.toR(), m.toC(), moved);
            if (!more.isEmpty()) {
                continueR = m.toR();
                continueC = m.toC();
                return true;
            } else {
                continueR = -1;
                continueC = -1;
                switchPlayer();
            }
        } else {
            continueR = -1;
            continueC = -1;
            switchPlayer();
        }

        checkGameOver();
        return true;
    }

    private List<Move> getLegalCapturesForPiece(int r, int c, Piece p) {
        List<Move> res = new ArrayList<>();
        if (p == null) return res;
        int[][] dirs;
        if (p.isKing()) dirs = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}};
        else if (p.getColor() == Piece.Color.WHITE) dirs = new int[][]{{-1,1},{-1,-1}};
        else dirs = new int[][]{{1,1},{1,-1}};

        for (int[] d : dirs) {
            int midR = r + d[0], midC = c + d[1];
            int toR = r + 2*d[0], toC = c + 2*d[1];
            if (board.inBounds(toR, toC) && board.getPiece(toR, toC) == null) {
                Piece mid = board.getPiece(midR, midC);
                if (mid != null && mid.getColor() != p.getColor()) {
                    res.add(new Move(r, c, toR, toC, true, midR, midC));
                }
            }
        }
        return res;
    }

    private boolean movesEqual(Move a, Move b) {
        if (a == null || b == null) return false;
        if (a.fromR() != b.fromR() || a.fromC() != b.fromC()) return false;
        if (a.toR() != b.toR() || a.toC() != b.toC()) return false;
        if (a.isCapture() != b.isCapture()) return false;
        if (a.isCapture()) {
            return a.capturedR() == b.capturedR() && a.capturedC() == b.capturedC();
        }
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
    }

    private void checkGameOver() {
        boolean any = false;
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Piece p = board.getPiece(r, c);
                if (p != null && p.getColor() == currentPlayer) {
                    if (!getLegalMovesForPiece(r, c).isEmpty()) { any = true; break; }
                }
            }
            if (any) break;
        }
        if (!any) {
            gameOver = true;
        }
    }

    public int[] getContinuePosition() {
        return new int[]{continueR, continueC};
    }
}
