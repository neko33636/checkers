package org.example.checkers.logic;

import java.util.Objects;

public class Move {
    private final int fromR, fromC;
    private final int toR, toC;
    private final boolean capture;
    private final int capturedR, capturedC; // valid only if capture == true

    public Move(int fromR, int fromC, int toR, int toC) {
        this(fromR, fromC, toR, toC, false, -1, -1);
    }

    public Move(int fromR, int fromC, int toR, int toC, boolean capture, int capturedR, int capturedC) {
        this.fromR = fromR;
        this.fromC = fromC;
        this.toR = toR;
        this.toC = toC;
        this.capture = capture;
        this.capturedR = capturedR;
        this.capturedC = capturedC;
    }

    public int fromR() { return fromR; }
    public int fromC() { return fromC; }
    public int toR() { return toR; }
    public int toC() { return toC; }
    public boolean isCapture() { return capture; }
    public int capturedR() { return capturedR; }
    public int capturedC() { return capturedC; }

    @Override
    public String toString() {
        if (capture) {
            return String.format("(%d,%d)->(%d,%d) capture (%d,%d)", fromR, fromC, toR, toC, capturedR, capturedC);
        } else {
            return String.format("(%d,%d)->(%d,%d)", fromR, fromC, toR, toC);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move m = (Move) o;
        return fromR == m.fromR && fromC == m.fromC &&
                toR == m.toR && toC == m.toC &&
                capture == m.capture &&
                (!capture || (capturedR == m.capturedR && capturedC == m.capturedC));
    }

    @Override
    public int hashCode() {
        if (capture) {
            return Objects.hash(fromR, fromC, toR, toC, capture, capturedR, capturedC);
        } else {
            return Objects.hash(fromR, fromC, toR, toC, capture);
        }
    }

}
