package petrov.model;

import petrov.view.RedrawCell;

import java.util.*;

public class Field {
    public HashSet<Integer> numbersOfEmptyButtons = new HashSet<>();
    public RedrawCell[] buttons;
    public int mapSize;
}
