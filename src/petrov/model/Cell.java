package petrov.model;


import javax.swing.*;

public class Cell extends JButton {
    private boolean withChip = false;

    boolean getChip() {
        return withChip;
    }

    public void setChip() {
        withChip = true;
    }
}
