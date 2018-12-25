package petrov.model;

import petrov.view.RedrawCell;

import java.util.*;

/**
 * It is just a GUI version of game field
 */
public class Field {
    public HashSet<Integer> numbersOfEmptyButtons = new HashSet<>();
    public RedrawCell[] buttons;
    public int mapSize;
}
