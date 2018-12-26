package model;

/**
 * This enum uses as directions in which players can make their turns
 */
public enum Dirs {
    dirNW(-9), dirN(-8), dirNE(-7), dirW(-1), dirE(1), dirSW(7), dirS(8), dirSE(9);

    private final int intDirection;

    Dirs(int intDirection) {
        this.intDirection = intDirection;
    }

    public int get() {
        return this.intDirection;
    }

}
