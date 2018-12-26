package view;

import model.*;

import javax.swing.*;

public class RedrawCell extends Cell {

    /**
     * Method redraws all field after turns of player and AI
     *
     * @param button     is button on which you pressed
     * @param classField is a field of game
     * @param logic      is game logic which contains all chips
     * @param empty      is empty cell
     * @param white      is cell with white chip
     * @param black      is cell with black chip
     */
    public void changeButton(Cell button, Field classField, ReversiField logic, ImageIcon empty, ImageIcon white,
                             ImageIcon black) {
        button.setChip();
        for (int i = 0; i < 64; i++) {
            if (logic.getChip(i) == 0) classField.buttons[i].setIcon(empty);
            if (logic.getChip(i) == 1) classField.buttons[i].setIcon(white);
            if (logic.getChip(i) == 2) classField.buttons[i].setIcon(black);
        }
        button.setPressedIcon(null);
    }

}