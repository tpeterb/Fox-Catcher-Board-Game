package state;

import org.tinylog.Logger;

/**
 * Represents the four main directions.
 */
public enum Direction {

    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

    private final int rowChange;
    private final int colChange;

    Direction(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * @return The change in the row coordinate when moving to the
     * direction.
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * @return The change in the column coordinate when moving to the
     * direction.
     */
    public int getColChange() {
        return colChange;
    }

    /**
     * @return The direction that corresponds to the coordinate changes
     * specified.
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     */
    public static Direction of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }
}