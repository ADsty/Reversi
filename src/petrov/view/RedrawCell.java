package petrov.view;

import petrov.model.*;

import javax.swing.*;

public class RedrawCell extends Cell {

    public void changeButton(Cell button, Field classField, ReversiField logic) {
        button.setChip();
        for (int i = 0; i < 64; i++) {
            if (logic.getChip(i) == 0) classField.buttons[i].setIcon(new ImageIcon("resources\\empty.png"));
            if (logic.getChip(i) == 1) classField.buttons[i].setIcon(new ImageIcon("resources\\white.png"));
            if (logic.getChip(i) == 2) classField.buttons[i].setIcon(new ImageIcon("resources\\black.png"));
        }
        button.setPressedIcon(null);
    }

}