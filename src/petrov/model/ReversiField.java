package petrov.model;

import java.util.Arrays;

public class ReversiField {
    private int turn;
    private ReversiAI ai = new ReversiAI();
    private int[] field = new int[64];

    private int turnOpposite(int turn) {
        if (turn == 1) return 2;
        else return 1;
    }

    private boolean leftWrong(int cell){
        for(int i = 1 ; i < 8 ; i++){
            if(cell == i * 8) return true;
        }
        return false;
    }

    private boolean rightWrong(int cell){
        int d = 7;
        for(int i = 0 ; i < 8 ; i++){
            if(cell == d) return true;
            d += 8;
        }
        return false;
    }

    private boolean isWrongDirect(int cell , int dir){
        if(cell == 0){
            if(dir == -8 || dir == -9 || dir == -1 || dir ==8) return true;
        }
        if(cell == 7){
            if(dir == -8 || dir == -7 || dir == 1 || dir == 9) return true;
        }
        if(cell == 56){
            if(dir == 8 || dir == 7 || dir == -1 || dir == -9) return true;
        }
        if(cell == 63){
            if(dir == 8 || dir == 9 || dir == 1 || dir == -7) return true;
        }
        if(cell > 0 && cell < 8 ){
            if(dir == -9 || dir == -8 || dir == -7) return true;
        }
        if(leftWrong(cell)){
            if(dir == -1 || dir == -9 || dir == 7) return true;
        }
        if(rightWrong(cell)){
            if(dir == -7 || dir == 1 || dir == 9) return true;
        }
        if(cell > 56 && cell < 64){
            if(dir == 7 || dir == 8 || dir == 9) return true;
        }
        return false;
    }

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

    public boolean hasFreeCells() {
        for (int i : field) {
            if (i == 0) return true;
        }
        return false;
    }

    static private final int[] DIRECTIONS = new int[]{
            1, 7, 8, 9, -1, -7, -8, -9
    };

    private boolean correct(int cell) {
        return (cell >= 0 && cell < 64);
    }


    private boolean canMakeTurnInThisCell(int cell) {
        if (field[cell] == 0) {
            for (int dir : DIRECTIONS) {
                if (!isWrongDirect(cell , dir)) {
                    int current = cell + dir;
                    if (correct(current)) {
                        while (correct(current) && turnOpposite(turn) == field[current]) {
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

    private boolean canMakeTurn(int turn) {
        for (int i = 0; i < 64; i++) {
            if (field[i] == turn) {
                for (int dir : DIRECTIONS) {
                    if (!isWrongDirect(i, dir)) {
                        int current = i + dir;
                        if (correct(current)) {
                            while (correct(current) && turnOpposite(turn) == field[current]) {
                                current += dir;
                                if (correct(current) && field[current] == 0) return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkLength(int cell, int dir) {
        int result = 0;
        int direct = dir;
        if (correct(cell + direct) && !isWrongDirect(cell , direct)) {
            while (correct(cell + direct) && field[cell + direct] == turnOpposite(turn)) {
                result++;
                direct += dir;
            }
            if (correct(cell + direct)) {
                if (field[cell + direct] == 0) result = 0;
            }
            if (correct(cell + direct)) {
                if (field[cell + direct] == 1) return result != 0;
            }
        }
        return result != 0;
    }

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

    public boolean canTurn() {
        return canMakeTurn(1);
    }

    public boolean makeTurn(int cell) {
        if (!hasFreeCells() || (!canMakeTurn(turn) && !canMakeTurn(turnOpposite(turn)))) {
            System.out.println(winner());
            return false;
        }
        if (!canMakeTurn(turn)) {
            turn = turnOpposite(turn);
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

    public int getChip(int cell) {
        return field[cell];
    }

    private void flipOppositeChips(int cell) {
        for (int dir : DIRECTIONS) {
            int current = cell;
            if (checkLength(current, dir) && !isWrongDirect(current , dir)) {
                current += dir;
                if (correct(current)) {
                    while (correct(current) && !isWrongDirect(current , dir) && field[current] == turnOpposite(turn)) {
                        field[current] = turn;
                        current += dir;
                    }
                }
            }
        }
    }

    public void makeTurnAI() {
        ai.takeField(Arrays.copyOf(field, field.length));
        int turn = ai.rootSearch(true, 2000);
        makeTurn(turn);
    }
}