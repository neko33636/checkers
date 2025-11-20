package org.example.checkers.logic;

import java.util.Objects;

/**
 * Класс представляет один ход в игре шашки.
 * Ход содержит координаты начальной клетки, конечной клетки, а также
 * информацию о том, является ли он рубкой (захватом) и какие координаты
 * побитой шашки.
 */
public class Move {

    /** Начальная строка. */
    private final int fromR;

    /** Начальный столбец. */
    private final int fromC;

    /** Конечная строка. */
    private final int toR;

    /** Конечный столбец. */
    private final int toC;

    /** Флаг: true — ход является рубкой (захватом), false — обычный ход. */
    private final boolean capture;

    /** Строка побитой шашки (актуально только если capture == true). */
    private final int capturedR;

    /** Столбец побитой шашки (актуально только если capture == true). */
    private final int capturedC;

    /**
     * Создаёт обычный (не рубящий) ход.
     *
     * @param fromR строка начальной клетки
     * @param fromC столбец начальной клетки
     * @param toR строка конечной клетки
     * @param toC столбец конечной клетки
     */
    public Move(int fromR, int fromC, int toR, int toC) {
        this(fromR, fromC, toR, toC, false, -1, -1);
    }

    /**
     * Создаёт ход, включая информацию о том, является ли он рубкой.
     *
     * @param fromR строка начала хода
     * @param fromC столбец начала хода
     * @param toR строка конца хода
     * @param toC столбец конца хода
     * @param capture true, если ход — рубка
     * @param capturedR строка побитой шашки (если есть)
     * @param capturedC столбец побитой шашки (если есть)
     */
    public Move(int fromR, int fromC, int toR, int toC,
                boolean capture, int capturedR, int capturedC) {
        this.fromR = fromR;
        this.fromC = fromC;
        this.toR = toR;
        this.toC = toC;
        this.capture = capture;
        this.capturedR = capturedR;
        this.capturedC = capturedC;
    }

    /** @return начальная строка хода */
    public int fromR() { return fromR; }

    /** @return начальный столбец хода */
    public int fromC() { return fromC; }

    /** @return конечная строка хода */
    public int toR() { return toR; }

    /** @return конечный столбец хода */
    public int toC() { return toC; }

    /** @return true, если ход — рубка, иначе false */
    public boolean isCapture() { return capture; }

    /**
     * Возвращает строку побитой шашки.
     * Имеет смысл только если {@link #isCapture()} == true.
     *
     * @return строка побитой шашки
     */
    public int capturedR() { return capturedR; }

    /**
     * Возвращает столбец побитой шашки.
     * Имеет смысл только если {@link #isCapture()} == true.
     *
     * @return столбец побитой шашки
     */
    public int capturedC() { return capturedC; }

    /**
     * Возвращает строковое представление хода.
     * Формат:
     *
     * <pre>
     * (r1,c1)->(r2,c2)
     * (r1,c1)->(r2,c2) capture (cr,cc)
     * </pre>
     *
     * @return строка с координатами хода
     */
    @Override
    public String toString() {
        if (capture) {
            return String.format("(%d,%d)->(%d,%d) capture (%d,%d)",
                    fromR, fromC, toR, toC, capturedR, capturedC);
        } else {
            return String.format("(%d,%d)->(%d,%d)", fromR, fromC, toR, toC);
        }
    }

    /**
     * Сравнивает два хода: равны, если совпадают все координаты
     * и тип хода (простой или рубка).
     *
     * @param o другой объект
     * @return true, если ходы совпадают
     */
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

    /**
     * Генерирует хеш-код для корректной работы в коллекциях.
     *
     * @return хеш-код хода
     */
    @Override
    public int hashCode() {
        if (capture) {
            return Objects.hash(fromR, fromC, toR, toC, capture, capturedR, capturedC);
        } else {
            return Objects.hash(fromR, fromC, toR, toC, capture);
        }
    }
}
