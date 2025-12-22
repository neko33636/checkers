package org.example.checkers.ui;

import org.example.checkers.logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Панель отображения игровой доски.
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>отрисовку клеток доски;</li>
 *     <li>отрисовку шашек;</li>
 *     <li>подсветку выбранной шашки;</li>
 *     <li>обработку кликов мыши.</li>
 * </ul>
 */
public class BoardPanel extends JPanel {

    /** Размер одной клетки доски в пикселях. */
    private static final int CELL_SIZE = 70;

    /** Контроллер игры. */
    private final GameController controller;

    /** Координаты выбранной клетки. */
    private int selectedR = -1;
    private int selectedC = -1;

    /**
     * Создаёт панель доски.
     *
     * @param controller контроллер игры
     */
    public BoardPanel(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(8 * CELL_SIZE, 8 * CELL_SIZE));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int c = e.getX() / CELL_SIZE;
                int r = e.getY() / CELL_SIZE;
                controller.onCellClicked(r, c);
            }
        });
    }

    /**
     * Устанавливает выбранную клетку.
     *
     * @param r номер строки
     * @param c номер столбца
     */
    public void setSelected(int r, int c) {
        selectedR = r;
        selectedC = c;
        repaint();
    }

    /**
     * Сбрасывает выделение клетки.
     */
    public void clearSelected() {
        selectedR = -1;
        selectedC = -1;
    }

    /**
     * Основной метод отрисовки компонента.
     *
     * @param g графический контекст
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
        drawSelection(g);
    }

    /**
     * Отрисовывает игровую доску.
     *
     * @param g графический контекст
     */
    private void drawBoard(Graphics g) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if ((r + c) % 2 == 0)
                    g.setColor(new Color(240, 217, 181));
                else
                    g.setColor(new Color(181, 136, 99));
                g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * Отрисовывает шашки на доске.
     *
     * @param g графический контекст
     */
    private void drawPieces(Graphics g) {
        Board board = controller.getBoard();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p == null) continue;

                g.setColor(p.getColor() == Piece.Color.WHITE ? Color.WHITE : Color.BLACK);

                int x = c * CELL_SIZE + 10;
                int y = r * CELL_SIZE + 10;
                g.fillOval(x, y, CELL_SIZE - 20, CELL_SIZE - 20);

                if (p.isKing()) {
                    g.setColor(Color.RED);
                    g.drawString("K", c * CELL_SIZE + 30, r * CELL_SIZE + 40);
                }
            }
        }
    }

    /**
     * Отрисовывает выделение выбранной клетки.
     *
     * @param g графический контекст
     */
    private void drawSelection(Graphics g) {
        if (selectedR == -1) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(4));

        g2.drawRect(
                selectedC * CELL_SIZE,
                selectedR * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE
        );

        g2.setStroke(new BasicStroke(1));
    }
}
