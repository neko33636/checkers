package org.example.checkers.logic;

public class Piece {
    public enum Color { WHITE, BLACK }

    private final Color color;
    private boolean king;

    public Piece(Color color) {
        this.color = color;
        this.king = false;
    }

    public Color getColor() {
        return color;
    }

    public boolean isKing() {
        return king;
    }

    public void makeKing() {
        this.king = true;
    }

    @Override
    public String toString() {
        return color + (king ? "K" : "M");
    }
}
