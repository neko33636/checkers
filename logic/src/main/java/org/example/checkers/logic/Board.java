package org.example.checkers.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий игровую доску для игры в шашки.
 * Доска имеет фиксированный размер 8×8 и содержит массив фигур.
 * Класс поддерживает начальную расстановку, копирование доски,
 * выполнение ходов, снятие фигур, повышение до дамки
 * и проверку границ.
 */
public class Board {
    /** Размер доски (8×8). */
    public static final int SIZE = 8;

    /** Внутреннее представление доски — двумерный массив фигур. */
    private final Piece[][] grid;

    /**
     * Создаёт новую доску и выполняет стандартную начальную расстановку фигур.
     */
    public Board() {
        grid = new Piece[SIZE][SIZE];
        setupInitial();
    }

    /**
     * Конструктор копирования.
     * Создаёт глубокую копию переданной доски:
     * каждая фигура копируется заново, включая её статус дамки.
     *
     * @param other другая доска, которую нужно скопировать
     */
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

    /**
     * Выполняет стандартную начальную расстановку фигур:
     * чёрные располагаются сверху, белые — снизу, только на тёмных клетках.
     */
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

    /**
     * Возвращает фигуру в указанной позиции.
     *
     * @param r строка
     * @param c столбец
     * @return фигура или null, если клетки нет в пределах доски
     */
    public Piece getPiece(int r, int c) {
        if (!inBounds(r, c)) return null;
        return grid[r][c];
    }

    /**
     * Устанавливает фигуру в указанную клетку.
     *
     * @param r строка
     * @param c столбец
     * @param p фигура (может быть null)
     * @throws IllegalArgumentException если координаты вне доски
     */
    public void setPiece(int r, int c, Piece p) {
        if (!inBounds(r, c)) throw new IllegalArgumentException("Out of bounds");
        grid[r][c] = p;
    }

    /**
     * Удаляет фигуру из клетки доски.
     *
     * @param r строка
     * @param c столбец
     * @throws IllegalArgumentException если координаты вне доски
     */
    public void removePiece(int r, int c) {
        if (!inBounds(r, c)) throw new IllegalArgumentException("Out of bounds");
        grid[r][c] = null;
    }

    /**
     * Проверяет, находятся ли координаты внутри пределов доски.
     *
     * @param r строка
     * @param c столбец
     * @return true, если координаты допустимы, иначе false
     */
    public boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    /**
     * Применяет переданный ход к доске:
     * перемещает фигуру, удаляет побитую фигуру (если ход — взятие).
     *
     * @param m объект хода
     * @return true, если ход был взятием (capture)
     * @throws IllegalStateException если в исходной клетке нет фигуры
     */
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

    /**
     * Проверяет, должна ли фигура стать дамкой и повышает её при необходимости:
     * белые становятся дамками на верхней линии, чёрные — на нижней.
     *
     * @param r строка фигуры
     * @param c столбец фигуры
     */
    public void tryPromote(int r, int c) {
        Piece p = getPiece(r, c);
        if (p == null) return;
        if (!p.isKing()) {
            if (p.getColor() == Piece.Color.WHITE && r == 0) p.makeKing();
            if (p.getColor() == Piece.Color.BLACK && r == SIZE - 1) p.makeKing();
        }
    }

    /**
     * Возвращает список всех координат клеток доски.
     * Используется для перебора всей доски.
     *
     * @return список массивов вида {r, c}
     */
    public List<int[]> allPositions() {
        List<int[]> res = new ArrayList<>();
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                res.add(new int[]{r, c});
        return res;
    }
}
