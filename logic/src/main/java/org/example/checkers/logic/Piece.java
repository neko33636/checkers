package org.example.checkers.logic;

/**
 * Класс представляет одну шашку на игровом поле.
 * Шашка принадлежит одному из двух игроков (белому или чёрному)
 * и может быть простой либо дамкой.
 */
public class Piece {
    /**
     * Цвет шашки — определяет игрока, которому она принадлежит.
     * WHITE — белые шашки, ходят первыми.
     * BLACK — чёрные шашки.
     */
    public enum Color { WHITE, BLACK }

    /** Цвет (принадлежность) данной шашки. */
    private final Color color;
    /** Показатель того, является ли шашка дамкой. */
    private boolean king;

    /**
     * Создаёт новую шашку заданного цвета.
     * По умолчанию шашка не является дамкой.
     *
     * @param color цвет (игрок), которому принадлежит шашка
     */
    public Piece(Color color) {
        this.color = color;
        this.king = false;
    }

    /**
     * Возвращает цвет шашки.
     *
     * @return цвет шашки (WHITE или BLACK)
     */
    public Color getColor() {
        return color;
    }

    /**
     * Проверяет, является ли шашка дамкой.
     *
     * @return true, если шашка — дамка; false, если обычная
     */
    public boolean isKing() {
        return king;
    }

    /**
     * Превращает шашку в дамку.
     * Дамка получает право ходить в обе стороны.
     */
    public void makeKing() {
        this.king = true;
    }

    /**
     * Возвращает строковое представление шашки.
     * Формат:
     * WHITEK — белая дамка,
     * WHITEM — белая обычная,
     * BLACKK — чёрная дамка,
     * BLACKM — чёрная обычная.
     *
     * @return строка с информацией о цвете и типе шашки
     */
    @Override
    public String toString() {
        return color + (king ? "K" : "M");
    }
}
