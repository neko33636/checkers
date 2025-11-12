package org.example.checkers.logic;

public class Piece {
    private String type;
    private String color;

    public Piece(String color) {
        this.color = color.toUpperCase();
        this.type = "MAN";
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public void promoteToKing() {
        this.type = "KING";
    }

    public boolean isKing() {
        return type.equals("KING");
    }

    @Override
    public String toString() {
        return color + " " + type;
    }
}
