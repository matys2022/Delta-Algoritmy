package models;

public class BoundingDimensions {
    private int LEFT;
    private int TOP;
    private int RIGHT;
    private int BOTTOM;

    public BoundingDimensions(int LEFT, int TOP, int RIGHT, int BOTTOM) {
        this.LEFT = LEFT;
        this.TOP = TOP;
        this.RIGHT = RIGHT;
        this.BOTTOM = BOTTOM;
    }

    public BoundingDimensions(int LR, int TB) {
        this(LR, TB, LR, TB);
    }

    public BoundingDimensions(int ALL) {
        this(ALL, ALL, ALL, ALL);
    }

    public int getLEFT() {
        return LEFT;
    }

    public int getTOP() {
        return TOP;
    }

    public int getRIGHT() {
        return RIGHT;
    }

    public int getBOTTOM() {
        return BOTTOM;
    }
}
