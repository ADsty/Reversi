package model;

class ReversiAI {
    private int turn;
    private int WIN = 64000;
    private int[] field = new int[64];
    private int[] stable = new int[64];
    private int[][] history = new int[3][64];
    private int[][] bestMoves = new int[64][2];

    // Methods that duplicate methods from main logic of game

    static private final int[] DIRECTIONS = new int[]{
            Dirs.dirE.get(), Dirs.dirSW.get(), Dirs.dirS.get(), Dirs.dirSE.get(), Dirs.dirW.get(), Dirs.dirNE.get(),
            Dirs.dirNW.get(), Dirs.dirNW.get()
    };

    private void flipOppositeChips(int cell, int[] caps, int n) {
        for (int dir : DIRECTIONS) {
            int current = cell;
            int check = checkLength(current, dir);
            if (check != 0 && !isWrongDirect(current, dir)) {
                current += dir;
                if (correct(current)) {
                    while (correct(current) && !isWrongDirect(current, dir) && field[current] == turnOpposite(turn)) {
                        if (check == 0) break;
                        field[current] = turn;
                        n++;
                        caps[n] = current;
                        current += dir;
                        check--;
                    }
                }
            }
        }
    }

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
                if (field[cell + direct] == 1) return result;
            }
        }
        return result;
    }

    private int turnOpposite(int turn) {
        if (turn == 1) return 2;
        else return 1;
    }

    private boolean canMakeTurnInThisCell(int cell) {
        if (field[cell] == 0) {
            for (int dir : DIRECTIONS) {
                if (!isWrongDirect(cell, dir)) {
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

    private boolean leftWrong(int cell) {
        for (int i = 1; i < 8; i++) {
            if (cell == i * 8) return true;
        }
        return false;
    }

    private boolean rightWrong(int cell) {
        int d = 7;
        for (int i = 0; i < 8; i++) {
            if (cell == d) return true;
            d += 8;
        }
        return false;
    }

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


    private boolean correct(int cell) {
        return (cell >= 0 && cell < 64);
    }

    // End of copied methods

    /**
     * Method takes the field from current game
     *
     * @param field is the field that AI wants to take
     */
    void takeField(int[] field) {
        this.field = field;
    }

    /**
     * @param cell is a cell where AI wants to make the turn
     * @param dir  is direction of the turn
     * @param caps is array of captured chips which needs to unmake the turn
     * @param n    is number of chips that is under AI control for now
     * @return number of chips that is under AI control for now
     */
    private int capture(int cell, int dir, int[] caps, int n) {
        int current = cell + dir;
        while (correct(current) && !isWrongDirect(current, dir) && field[current] == turnOpposite(turn)) current += dir;
        if (correct(current) && !isWrongDirect(current, dir) && field[current] == turn) {
            for (int i = cell + dir; i != current; i += dir) {
                n++;
                caps[n] = i;
                field[i] = turn;
                flipOppositeChips(field[i], caps, n);
            }
        }
        return n;
    }

    /**
     * Method generates all possible moves for AI
     *
     * @param moves is array of all moves that will be used to compare with each other
     * @return number of generated moves
     */
    private int genMoves(int[] moves) {
        int n = 0;
        for (int i = 0; i < 64; i++) {
            if (field[i] == 0) {
                if (canMakeTurnInThisCell(i)) {
                    moves[n++] = i;
                }
            }
        }
        return n;
    }

    /**
     * Method make a move for AI
     *
     * @param cell is a cell where AI wants to make the turn
     * @param caps is array of captured chips
     * @return number of captured chips
     */
    private int makeMoveAI(int cell, int[] caps) {
        field[cell] = turn;
        int n = 0;
        for (int dir : DIRECTIONS) {
            n = capture(cell, dir, caps, n);
        }
        turn = turnOpposite(turn);
        return n;
    }

    /**
     * @param cell is a cell where AI was make a turn in a past
     * @param caps is array of captured in past chips
     * @param n    number of captured in past chips
     */
    private void unmakeMoveAI(int cell, int[] caps, int n) {
        field[cell] = 0;
        for (int i = 0; correct(caps[i]) && i < n; i++) {
            field[caps[i]] = turn;
        }
        turn = turnOpposite(turn);
    }

    /**
     * Method evaluates current field
     *
     * @return a number which means who is winning now and how much is difference
     */
    private int exactEval() {
        int score = 0;
        for (int i = 0; i < 64; i++) {
            if (field[i] == 2) score += 100;
            if (field[i] == 1) score -= 100;
        }
        if (turn == 2) return score;
        else return -score;
    }

    /**
     * Method sets a chips on current direction as stable
     *
     * @param sq  is chip from which you start
     * @param dir is current direction
     */
    private void stableRay(int sq, int dir) {
        int s;
        for (s = sq + dir; correct(s) && field[s] == field[sq]; s += dir) {
            stable[s] = 1;
        }
        if (!correct(s) || field[s] == 0) return;
        int e;
        for (e = s + dir; correct(e); e += dir) {
            if (field[e] == 0) return;
        }
        for (e = s + dir; correct(e); e += dir) {
            stable[e] = 1;
        }
    }

    /**
     * Method looks for stable chips on the field. Stable means that the chip will never flip
     */
    private void boarderStable(int from, int to, int step, int dir1, int dir2, int dir3) {
        int i, j;
        if (field[from] != 0) {
            for (i = from; i <= to && correct(field[i]); i += step) {
                if (field[i] != field[from]) break;
                stable[i] = 1;
                stableRay(i, dir1);
                stableRay(i, dir2);
                if (i != from && i != to) stableRay(i, dir3);
            }
            if (correct(i) && field[i] != 0) {
                for (j = i + 1; j <= to && correct(field[j]); j += step) {
                    if (field[j] == 0) break;
                }
                if (j > to) {
                    for (j = i + 1; j <= to && correct(field[j]); j += step) {
                        field[j] = 1;
                        stableRay(j, dir1);
                        stableRay(j, dir2);
                        if (i != to) stableRay(j, dir3);
                    }
                    return;
                }
            }
        }
        if (field[to] != 0) {
            for (i = to; correct(i) && field[i] == field[to]; i -= step) {
                stable[i] = 1;
                stableRay(i, dir1);
                stableRay(i, dir2);
                if (i != to) stableRay(i, dir3);
            }
        }
    }

    /**
     * Method evaluate current field with division chips on stable and unstable
     *
     * @return a number which means who is winning now and how much is difference
     */
    private int eval() {
        for (int i = 0; i < 64; i++) {
            stable[i] = 0;
        }
        boarderStable(0, 7, 1, Dirs.dirSW.get(), Dirs.dirSE.get(), Dirs.dirS.get());
        boarderStable(56, 63, 1, Dirs.dirNW.get(), Dirs.dirNE.get(), Dirs.dirN.get());
        boarderStable(7, 63, 8, Dirs.dirNW.get(), Dirs.dirSW.get(), Dirs.dirW.get());
        boarderStable(0, 56, 8, Dirs.dirNE.get(), Dirs.dirSE.get(), Dirs.dirE.get());
        int score = 0;
        for (int i = 0; i < 64; i++) {
            if (field[i] == 2) {
                if (stable[i] == 1) score += 100;
                else {
                    for (int dir : DIRECTIONS) {
                        int current = i + dir;
                        if (correct(current) && field[current] == 0) score -= 10;
                    }
                }
            }
            if (field[i] == 1) {
                if (stable[i] == 1) score -= 100;
                else {
                    for (int dir : DIRECTIONS) {
                        int current = i + dir;
                        if (correct(current) && field[current] == 0) score += 10;
                    }
                }
            }
        }
        return score;
    }

    /**
     * Method uses alpha-beta pruning and searches for best moves
     *
     * @param depth is depth to which we need to go
     * @param ply   is a number for best moves on this depth
     * @param alpha is alpha number for alpha-beta pruning
     * @param beta  is beta number for alpha-beta pruning
     * @param pass  means if last player passed on his turn
     * @return last alpha value
     */
    private int Search(int depth, int ply, int alpha, int beta, boolean pass) {
        int i;
        int j;

        if (depth <= 0) {
            return eval();
        }
        int[] moves = new int[64];
        int n = genMoves(moves);

        if (n == 0) {
            if (pass) {
                return exactEval();
            }
            turn = turnOpposite(turn);
            int score = -Search(depth, ply + 1, -beta, -alpha, true);
            turn = turnOpposite(turn);
            return score;
        }
        int[] scores = new int[64];
        for (i = 0; i < n; i++) {
            if (moves[i] == bestMoves[ply][0]) {
                scores[i] = bestMoves[ply][0];
            } else if (moves[i] == bestMoves[ply][1]) {
                scores[i] = bestMoves[ply][1];
            } else {
                scores[i] = history[turn][moves[i]];
            }
        }
        for (i = 0; i < n; i++) {
            for (j = n - 1; j > i; j--) {
                if (scores[i] < scores[j]) {
                    int t = scores[i];
                    scores[i] = scores[j];
                    scores[j] = t;

                    t = moves[i];
                    moves[i] = moves[j];
                    moves[j] = t;
                }
            }
        }

        int best_move = 0;
        for (i = 0; i < n; i++) {
            int[] caps = new int[64];
            int nc = makeMoveAI(moves[i], caps);
            int score;
            if (i == 0) {
                score = -Search(depth - 1, ply + 1, -beta, -alpha, false);
            } else {
                score = -Search(depth - 1, ply + 1, -alpha - 1, -alpha, false);
                if (score > alpha && score < beta) {
                    score = -Search(depth - 1, ply + 1, -beta, -score, false);
                }
            }
            unmakeMoveAI(moves[i], caps, nc);
            if (score > alpha) {
                if (score >= beta) {
                    if (moves[i] != bestMoves[ply][0]) {
                        bestMoves[ply][1] = bestMoves[ply][0];
                        bestMoves[ply][0] = moves[i];
                    }
                    return score;
                }
                best_move = moves[i];
                alpha = score;
            }
        }

        if (best_move != bestMoves[ply][0]) {
            bestMoves[ply][1] = bestMoves[ply][0];
            bestMoves[ply][0] = best_move;
        }

        history[turn][best_move] += depth * depth;
        return alpha;
    }


    /**
     * Main method of AI which makes everything works
     *
     * @param btm  just means who is make turn now
     * @param time is time to think for AI after which it needs to return best current turn
     * @return best current turn
     */
    int rootSearch(boolean btm, long time) {
        int i, d;
        int best;
        long startTime = System.currentTimeMillis();
        if (btm) turn = 2;
        else turn = 1;
        for (i = 0; i < 64; i++) history[0][i] = history[1][i];
        int[] moves = new int[64];
        int n = genMoves(moves);
        if (n == 0) return 0;
        if (n == 1) return moves[0];
        for (d = 1; d < 64; d++) {
            best = -WIN;
            for (int j = 0; j < n; j++) {
                int[] caps = new int[64];
                int nc = makeMoveAI(moves[j], caps);
                int score;
                if (n == 0) score = -Search(d - 1, 1, -WIN, -best, false);
                else {
                    score = -Search(d - 1, 1, -best - 1, -best, false);
                    if (score > best) score = -Search(d - 1, 1, -WIN, -score, false);
                }
                unmakeMoveAI(moves[j], caps, nc);
                if (score > best) {
                    best = score;
                    int bmove = moves[j];
                    for (int g = j; g > 0; g--) moves[g] = moves[g - 1];
                    moves[0] = bmove;
                }
            }
            if (System.currentTimeMillis() - startTime > time) break;
        }
        return moves[0];
    }

}