package org.example.checkers.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс, управляющий логикой игры в шашки.
 * Содержит текущее состояние доски, игрока, выполняет ходы,
 * определяет допустимые ходы и проверяет конец игры.
 */
public class Game {
    private Board board;
    private Piece.Color currentPlayer;
    private boolean gameOver;
    private int continueR = -1, continueC = -1;
    /**
     * Создаёт новую игру, инициализируя стандартную доску
     * и устанавливая текущего игрока — белые.
     */

    public Game() {
        board = new Board();
        currentPlayer = Piece.Color.WHITE;
        gameOver = false;
    }
    /**
     * Возвращает текущую игровую доску.
     *
     * @return объект {@link Board}, представляющий расстановку шашек.
     */
    public Board getBoard() { return board; }
    /**
     * Возвращает игрока, чей ход должен быть выполнен.
     *
     * @return цвет игрока, который должен ходить (WHITE или BLACK).
     */
    public Piece.Color getCurrentPlayer() { return currentPlayer; }
    /**
     * Проверяет, завершена ли игра.
     *
     * @return true — если у текущего игрока нет ходов; false — иначе.
     */
    public boolean isGameOver() { return gameOver; }
    /**
     * Получает список всех допустимых ходов для текущего игрока.
     * Правило шашек: если есть хоть один ход с захватом —
     * разрешены только захваты.
     *
     * @return список объектов {@link Move}, которые игрок может выполнить.
     */
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

    /**
     * Возвращает допустимые ходы для одной конкретной шашки.
     * Сначала ищутся захваты (если они есть — обычные ходы игнорируются).
     *
     * @param r строка шашки
     * @param c столбец шашки
     * @return список возможных ходов, включая захваты
     */

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

    /**
     * Выполняет ход, если он допустим.
     * Производит:
     *  - перемещение шашки;
     *  - удаление съеденной шашки;
     *  - проверку на превращение в дамку;
     *  - проверку на возможное продолжение серии захватов;
     *  - смену игрока.
     *
     * @param m объект {@link Move}, описывающий ход
     * @return true — если ход был выполнен; false — если ход недопустим
     */
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
    /**
     * Получает только захваты (если они есть) для выбранной шашки.
     *
     * @param r текущая строка шашки
     * @param c текущий столбец шашки
     * @param p объект {@link Piece}, представляющий шашку
     * @return список возможных захватов
     */
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
    /**
     * Сравнивает два хода на совпадение всех параметров.
     * Используется вместо equals(), которого нет в {@link Move}.
     *
     * @param a первый ход
     * @param b второй ход
     * @return true — если ходы идентичны; false иначе
     */
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
    /**
     * Меняет текущего игрока (белые → чёрные или наоборот).
     */

    private void switchPlayer() {
        currentPlayer = (currentPlayer == Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
    }
    /**
     * Проверяет, есть ли у текущего игрока доступные ходы.
     * Если ходов нет — игра завершается.
     */

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
    /**
     * Если игрок обязан продолжить серию захватов той же шашкой,
     * метод возвращает её координаты.
     *
     * @return массив {row, col}; если нет продолжения — {-1, -1}
     */
    public int[] getContinuePosition() {
        return new int[]{continueR, continueC};
    }
}
