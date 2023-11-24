package game2048;

public enum Direction {
    TOP(0, -1),
    RIGHT(1, 0),
    BOTTOM(0, 1),
    LEFT(-1, 0);

    private int x, y;

    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAngle(Direction other) {
        int angle = 0;
        Direction rotated = this;
        while (rotated != other) {
            rotated = rotated.rotateCCW();
            angle += 90;
        }
        return angle;
    }

    private Direction rotateCCW() {
        switch (this) {
            case TOP:
                return LEFT;
            case RIGHT:
                return TOP;
            case BOTTOM:
                return RIGHT;
            case LEFT:
                return BOTTOM;
        }
        return null;
    }
}
