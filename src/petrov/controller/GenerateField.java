package petrov.controller;

import petrov.model.Field;
import petrov.model.ReversiField;
import petrov.view.RedrawCell;

import javax.swing.*;
import java.awt.*;

import static petrov.view.Main.endGame;

public class GenerateField extends Field {
    private ReversiField gameLogic = new ReversiField();
    private ImageIcon empty = new ImageIcon("resources\\empty.png");
    private ImageIcon white = new ImageIcon("resources\\white.png");
    private ImageIcon black = new ImageIcon("resources\\black.png");

    /**
     * Method creates new field and starts new game
     */
    public void createEmptyField(JPanel panel, Field classField, JFrame jFrame) {
        gameLogic.gameStarted();
        for (int i = 0; i < 64; i++) {
            if (i == 27 || i == 28 || i == 35 || i == 36) {
                classField.buttons[i] = new RedrawCell();
                RedrawCell button = classField.buttons[i];
                button.setPreferredSize(new Dimension(50, 50));
                addActionListener(button, i, classField, jFrame, panel);
                panel.add(button);
                if (i == 27 || i == 36) {
                    button.setIcon(new ImageIcon("resources\\white.png"));
                } else button.setIcon(new ImageIcon("resources\\black.png"));
            } else {
                classField.buttons[i] = new RedrawCell();
                RedrawCell button = classField.buttons[i];
                button.setPreferredSize(new Dimension(50, 50));
                button.setIcon(new ImageIcon("resources\\empty.png"));
                addActionListener(button, i, classField, jFrame, panel);
                panel.add(button);
            }
        }
    }

    /**
     * Method adds to current button a listener that will make a turn in this cell after you pressed on this button
     */
    private void addActionListener(RedrawCell button, int i, Field classField, JFrame jFrame, JPanel panel) {
        button.addActionListener(e -> {
            if (gameLogic.makeTurn(i)) {
                gameLogic.makeTurnAI();
                button.changeButton(button, classField, gameLogic, empty, white, black);
            } else {
                JOptionPane.showMessageDialog(jFrame,
                        "You can't move in this cell");
                if (!gameLogic.canTurn()) {
                    endGame(gameLogic.winner(), classField, jFrame, panel);
                }
            }
            if (!gameLogic.hasFreeCells()) {
                endGame(gameLogic.winner(), classField, jFrame, panel);
            }
        });
    }
}