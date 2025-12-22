package org.example.checkers.ui;

import org.example.checkers.logic.*;

import java.util.List;

/**
 * Контроллер игры.
 * <p>
 * Связывает игровую логику ({@link Game}) и пользовательский интерфейс.
 * Обрабатывает действия пользователя и управляет игровым процессом.
 */
public class GameController {

    /** Экземпляр игровой логики. */
    private final Game game;

    /** Панель отображения доски. */
    private final BoardPanel boardPanel;

    /** Координаты выбранной шашки. */
    private int selectedR = -1;
    private int selectedC = -1;

    /**
     * Создаёт контроллер игры.
     * Инициализирует игровую логику и графическую панель.
     */
    public GameController() {
        this.game = new Game();
        this.boardPanel = new BoardPanel(this);
    }

    /**
     * Возвращает текущую игровую доску.
     *
     * @return объект {@link Board}
     */
    public Board getBoard() {
        return game.getBoard();
    }

    /**
     * Возвращает панель отображения доски.
     *
     * @return объект {@link BoardPanel}
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * Обрабатывает нажатие пользователя на клетку доски.
     * <p>
     * Логика работы:
     * <ul>
     *     <li>если шашка не выбрана — пытаемся выбрать шашку текущего игрока;</li>
     *     <li>если шашка уже выбрана — проверяем возможность хода;</li>
     *     <li>если ход корректен — выполняем его.</li>
     * </ul>
     *
     * @param r номер строки клетки
     * @param c номер столбца клетки
     */
    public void onCellClicked(int r, int c) {
        if (game.isGameOver()) return;

        if (selectedR == -1) {
            Piece p = game.getBoard().getPiece(r, c);
            if (p != null && p.getColor() == game.getCurrentPlayer()) {
                selectedR = r;
                selectedC = c;
                boardPanel.setSelected(r, c);
            }
            return;
        }

        List<Move> moves = game.getLegalMovesForPiece(selectedR, selectedC);
        for (Move m : moves) {
            if (m.toR() == r && m.toC() == c) {
                game.makeMove(m);
                break;
            }
        }

        selectedR = -1;
        selectedC = -1;
        boardPanel.clearSelected();
        boardPanel.repaint();
    }
}
