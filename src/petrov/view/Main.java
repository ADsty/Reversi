package view;

import model.Field;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;

import controller.GenerateField;


public class Main {

    /**
     * Method just starts the program
     */
    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame();
        JPanel panel = new JPanel();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        Field board = new Field();
        board.mapSize = 8;
        Image icon = ImageIO.read(new java.io.File("resources\\board.png"));
        jFrame.setIconImage(icon);
        startGame(board, jFrame, panel);
        jFrame.add(panel);
    }

    /**
     * Method creates a GUI version of game field and starts a new game
     */
    private static void startGame(Field classField, JFrame jFrame, JPanel panel) {
        int mapSize = classField.mapSize;
        jFrame.setBounds(540 - 3 * mapSize, 360 - 20 * mapSize, mapSize * 50, mapSize * 50 + 25);
        panel.setLayout(new GridLayout(mapSize, mapSize));
        classField.numbersOfEmptyButtons.clear();
        classField.buttons = new RedrawCell[mapSize * mapSize];
        panel.removeAll();
        GenerateField field = new GenerateField();
        field.createEmptyField(panel, classField, jFrame);
        jFrame.setVisible(true);
    }

    /**
     * Method ends the game and offers for a new game
     */
    public static void endGame(String message, Field classField, JFrame jFrame, JPanel panel) {
        for (RedrawCell button : classField.buttons) {
            button.setPressedIcon(null);
            for (ActionListener actionListener : button.getActionListeners()) {
                button.removeActionListener(actionListener);
            }
            for (MouseListener mouseListener : button.getMouseListeners()) {
                button.removeMouseListener(mouseListener);
            }
        }
        int result = JOptionPane.showConfirmDialog(null,
                message + "\n" + "Do you want to start a new game?");
        if (result == JOptionPane.YES_OPTION) startGame(classField, jFrame, panel);
        if (result == JOptionPane.NO_OPTION) System.exit(0);
    }
}
