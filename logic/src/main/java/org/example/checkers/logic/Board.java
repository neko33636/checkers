package org.example.checkers.logic;

import java.util.ArrayList;
import java.util.List;


public class Board {
    public static final int SIZE = 8;
    private final Piece[][] grid;

    public Board() {
        grid = new Piece[SIZE][SIZE];
        setupInitial();
    }

    public Board(Board other) {
        this.grid = new Piece[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = other.grid[r][c];
                if (p != null) {
                    Piece copy = new Piece(p.getColor());
                    if (p.isKing()) copy.makeKing();
                    this.grid[r][c] = copy;
                }
            }
        }
    }

    private void setupInitial() {
        for (int r = 0; r < 3; r++) {
            for (int c = (r % 2 == 0 ? 1 : 0); c < SIZE; c += 2) {
                grid[r][c] = new Piece(Piece.Color.BLACK);
            }
        }
        for (int r = SIZE - 3; r < SIZE; r++) {
            for (int c = (r % 2 == 0 ? 1 : 0); c < SIZE; c += 2) {
                grid[r][c] = new Piece(Piece.Color.WHITE);
            }
        }
    }

    public Piece getPiece(int r, int c) {
        if (!inBounds(r, c)) return null;
        return grid[r][c];
    }

    public void setPiece(int r, int c, Piece p) {
        if (!inBounds(r, c)) throw new IllegalArgumentException("Out of bounds");
        grid[r][c] = p;
    }

    public void removePiece(int r, int c) {
        if (!inBounds(r, c)) throw new IllegalArgumentException("Out of bounds");
        grid[r][c] = null;
    }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    public boolean applyMove(Move m) {
        Piece p = getPiece(m.fromR(), m.fromC());
        if (p == null) throw new IllegalStateException("No piece at from");
        setPiece(m.toR(), m.toC(), p);
        removePiece(m.fromR(), m.fromC());
        if (m.isCapture()) {
            removePiece(m.capturedR(), m.capturedC());
            return true;
        }
        return false;
    }

    public void tryPromote(int r, int c) {
        Piece p = getPiece(r, c);
        if (p == null) return;
        if (!p.isKing()) {
            if (p.getColor() == Piece.Color.WHITE && r == 0) p.makeKing();
            if (p.getColor() == Piece.Color.BLACK && r == SIZE - 1) p.makeKing();
        }
    }

    public List<int[]> allPositions() {
        List<int[]> res = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) for (int c = 0; c < SIZE; c++) res.add(new int[]{r, c});
        return res;
    }
}
