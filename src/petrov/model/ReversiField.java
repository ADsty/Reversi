package petrov.model;

import java.util.Arrays;

public class ReversiField {
    private int turn;
    private ReversiAI ai = new ReversiAI();
    private int[] field = new int[64];

    /**
     * Method returns integer represent of player who will make next turn
     *
     * @param turn is integer represent of player who has make last turn
     * @return 1 if player will make next turn and 2 if it will be AI
     */
    private int turnOpposite(int turn) {
        if (turn == 1) return 2;
        else return 1;
    }

    /**
     * Method checks if the cell in the left side of field
     *
     * @param cell is the cell where you want to make turn
     * @return true if the cell in the left side of field and false if it is not
     */
    private boolean leftWrong(int cell) {
        for (int i = 1; i < 8; i++) {
            if (cell == i * 8) return true;
        }
        return false;
    }

    /**
     * Method checks if the cell in the right side of field
     *
     * @param cell is the cell where you want to make turn
     * @return true if the cell in the right side of field and false if it is not
     */
    private boolean rightWrong(int cell) {
        int d = 7;
        for (int i = 0; i < 8; i++) {
            if (cell == d) return true;
            d += 8;
        }
        return false;
    }

    /**
     * Method checks if this direction in this cell is prohibited
     *
     * @param cell is the cell where you want to make turn
     * @param dir  is direction which will be compared with prohibited ones
     * @return true if this direction is prohibited and false if it doesn't
     */
    private boolean isWrongDirect(int cell, int dir) {
        if (cell == 0) {
            if (dir == Dirs.dirN.get() || dir == Dirs.dirNW.get() || dir == Dirs.dirW.get() || dir == Dirs.dirS.get())
                return true;
        }
        if (cell == 7) {
            if (dir == Dirs.dirN.get() || dir == Dirs.dirNE.get() || dir == Dirs.dirE.get() || dir == Dirs.dirSE.get())
                return true;
        }
        if (cell == 56) {
            if (dir == Dirs.dirS.get() || dir == Dirs.dirSW.get() || dir == Dirs.dirW.get() || dir == Dirs.dirNW.get())
                return true;
        }
        if (cell == 63) {
            if (dir == Dirs.dirS.get() || dir == Dirs.dirSE.get() || dir == Dirs.dirE.get() || dir == Dirs.dirNE.get())
                return true;
        }
        if (cell > 0 && cell < 8) {
            if (dir == Dirs.dirNW.get() || dir == Dirs.dirN.get() || dir == Dirs.dirNE.get()) return true;
        }
        if (leftWrong(cell)) {
            if (dir == Dirs.dirW.get() || dir == Dirs.dirNW.get() || dir == Dirs.dirSW.get()) return true;
        }
        if (rightWrong(cell)) {
            if (dir == Dirs.dirNE.get() || dir == Dirs.dirE.get() || dir == Dirs.dirSE.get()) return true;
        }
        if (cell > 56 && cell < 64) {
            if (dir == Dirs.dirSW.get() || dir == Dirs.dirS.get() || dir == Dirs.dirSE.get()) return true;
        }
        return false;
    }

    /**
     * Method creates an empty field with 4 chips in the center
     */
    public void gameStarted() {
        turn = 1;
        for (int i = 0; i < 64; i++) {
            field[i] = 0;
        }
        field[27] = 1;
        field[36] = 1;
        field[35] = 2;
        field[28] = 2;
    }

    /**
     * Method checks if there is free cells to make turn in them
     *
     * @return true if there is free cells and false if it doesn't
     */
    public boolean hasFreeCells() {
        for (int i : field) {
            if (i == 0) return true;
        }
        return false;
    }

    /**
     * Directions where you can make a turn
     */
    static private final int[] DIRECTIONS = new int[]{
            Dirs.dirE.get(), Dirs.dirSW.get(), Dirs.dirS.get(), Dirs.dirSE.get(), Dirs.dirW.get(), Dirs.dirNE.get(),
            Dirs.dirN.get(), Dirs.dirNW.get()
    };

    /**
     * Method checks if the cell is in this field
     *
     * @param cell is a cell which should be in field
     * @return true if cell in the field and false if it doesn't
     */
    private boolean correct(int cell) {
        return (cell >= 0 && cell < 64);
    }


    /**
     * Method checks if you can make a turn in this cell
     *
     * @param cell is a cell where you want to make a turn
     * @return true if you can make the turn and false if you can't
     */
    private boolean canMakeTurnInThisCell(int cell) {
        if (field[cell] == 0) {
            for (int dir : DIRECTIONS) {
                if (!isWrongDirect(cell, dir)) {
                    int current = cell + dir;
                    if (correct(current)) {
                        while (correct(current) && turnOpposite(turn) == field[current] && !isWrongDirect(cell, dir)) {
                            current += dir;
                            if (correct(current) && field[current] == turn) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Method checks if current player can make a turn in any cells
     *
     * @param turn is a number which means who tries to make this turn
     * @return true if he can and false if he doesn't
     */
    private boolean canMakeTurn(int turn) {
        for (int i = 0; i < 64; i++) {
            if (field[i] == 0) {
                for (int dir : DIRECTIONS) {
                    if (!isWrongDirect(i, dir)) {
                        int current = i + dir;
                        if (correct(current)) {
                            while (correct(current) && turnOpposite(turn) == field[current]) {
                                current += dir;
                                if (correct(current) && field[current] == turn) return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Methods looks how much chips will flip in this direction
     *
     * @param cell is a cell from which you start to flip chips
     * @param dir  is direction in which chips will flip
     * @return the number of chips that will flip
     */
    private int checkLength(int cell, int dir) {
        int result = 0;
        int direct = dir;
        if (correct(cell + direct) && !isWrongDirect(cell, direct)) {
            while (correct(cell + direct) && field[cell + direct] == turnOpposite(turn)) {
                result++;
                direct += dir;
            }
            if (correct(cell + direct)) {
                if (field[cell + direct] == 0) result = 0;
            }
            if (correct(cell + direct)) {
                if (field[cell + direct] == turnOpposite(turn)) return result;
            } else result = 0;
        }
        return result;
    }

    /**
     * Methods looks on the field and says who is winner
     *
     * @return result of the game from player's point of view
     */
    public String winner() {
        int white = 0;
        int black = 0;
        for (int i = 0; i < 64; i++) {
            if (field[i] != 0) {
                if (field[i] == 1) white++;
                else black++;
            }
        }
        if (black == white) return "Draw";
        else {
            if (black > white) return "You lose";
            else return "You won";
        }
    }

    /**
     * Method checks if player (not AI) can make any turn
     */
    public boolean canTurn() {
        return canMakeTurn(1);
    }

    /**
     * Method makes turn in the field
     *
     * @param cell is a cell in which you want to make the turn
     * @return true if you was make the turn and false if you wasn't make
     */
    public boolean makeTurn(int cell) {
        if (!hasFreeCells() || (!canMakeTurn(turn) && !canMakeTurn(turnOpposite(turn)))) {
            System.out.println(winner());
            return false;
        }
        if (!canMakeTurn(turn)) {
            return false;
        }
        if (correct(cell)) {
            if (correct(cell) && field[cell] == 0 && canMakeTurnInThisCell(cell)) {
                field[cell] = turn;
                flipOppositeChips(cell);
                turn = turnOpposite(turn);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Method gets a chip from the field
     *
     * @param cell is a cell where chip will be
     * @return 0 if there is no chip , 1 if hip is white and 2 if chip is black
     */
    public int getChip(int cell) {
        return field[cell];
    }

    /**
     * Method flips all chips between current one and other chips of the player in all directions after a turn
     *
     * @param cell is a cell where you was make a turn
     */
    private void flipOppositeChips(int cell) {
        for (int dir : DIRECTIONS) {
            int current = cell;
            int check = checkLength(current, dir);
            if (check != 0 && !isWrongDirect(current, dir)) {
                current += dir;
                if (correct(current)) {
                    while (correct(current) && !isWrongDirect(current, dir) && field[current] == turnOpposite(turn)) {
                        if (check == 0) break;
                        field[current] = turn;
                        current += dir;
                        check--;
                    }
                }
            }
        }
    }

    /**
     * Method just gives current position of all chips to AI and makes turn where AI wants to
     */
    public void makeTurnAI() {
        ai.takeField(Arrays.copyOf(field, field.length));
        int turn = ai.rootSearch(true, 1);
        makeTurn(turn);
    }
}