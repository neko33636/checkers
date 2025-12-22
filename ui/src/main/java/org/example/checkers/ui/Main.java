package org.example.checkers.ui;

import javax.swing.*;

/**
 * Главный класс пользовательского интерфейса.
 * <p>
 * Содержит точку входа в приложение.
 * Отвечает за создание главного окна программы
 * и инициализацию контроллера игры.
 */
public class Main {

    /**
     * Точка входа в приложение.
     * <p>
     * Запуск интерфейса выполняется в EDT (Event Dispatch Thread),
     * что является обязательным требованием для Swing-приложений.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Шашки");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.setResizable(false);

            GameController controller = new GameController();
            frame.add(controller.getBoardPanel());

            frame.setVisible(true);
        });
    }
}
