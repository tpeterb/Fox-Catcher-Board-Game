package state;

import java.util.Objects;

/**
 * Represents a 2D position.
 */
public class Position implements Cloneable {

    private int row;
    private int col;

    /**
     * Creates a {@code Position} object.
     *
     * @param row the row coordinate of the position
     * @param col the column coordinate of the position
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return The row coordinate of the position.
     */
    public int row() {
        return row;
    }

    /**
     * @return The column coordinate of the position.
     */
    public int col() {
        return col;
    }

    /**
     * @return The position whose vertical and horizontal distances from this
     * position are equal to the coordinate changes of the direction given.
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public Position getPositionAt(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * @return The position in the UP_LEFT direction, meaning the position
     * whose row and column coordinates are one less than
     * the current position's coordinates.
     */
    public Position getUpLeft() {
        return getPositionAt(Direction.UP_LEFT);
    }

    /**
     * @return The position in the UP_RIGHT direction, meaning the position
     * whose row coordinate is one less, and
     * whose column coordinate is one more than the current position's
     * row and column coordinate.
     */
    public Position getUpRight() {
        return getPositionAt(Direction.UP_RIGHT);
    }

    /**
     * @return The position in the DOWN_LEFT direction, meaning the position
     * whose row coordinate is one more, and
     * whose column coordinate is one less than the current position's
     * row and column coordinate.
     */
    public Position getDownLeft() {
        return getPositionAt(Direction.DOWN_LEFT);
    }

    /**
     * @return The position in the DOWN_RIGHT direction, meaning the position
     * whose row and column coordinates are one more than the current position's
     * row and column coordinate.
     */
    public Position getDownRight() {
        return getPositionAt(Direction.DOWN_RIGHT);
    }

    /**
     * Changes the position by the coordinate changes of the direction given.
     *
     * @param direction a direction that specifies a change in the coordinates
     */
    public void setTo(Direction direction) {
        row += direction.getRowChange();
        col += direction.getColChange();
    }

    /**
     * Changes the position by the coordinate changes of the UP_LEFT
     * direction, meaning that the row and column coordinates will be decreased by one.
     */
    public void setUpLeft() {
        setTo(Direction.UP_LEFT);
    }

    /**
     * Changes the position by the coordinate changes of the UP_RIGHT
     * direction, meaning that the row coordinate will be decreased by one,
     * and the column coordinate will be increased by one.
     */
    public void setUpRight() {
        setTo(Direction.UP_RIGHT);
    }

    /**
     * Changes the position by the coordinate changes of the DOWN_LEFT
     * direction, meaning that the row coordinate will be increased by one,
     * and the column coordinate will be decreased by one.
     */
    public void setDownLeft() {
        setTo(Direction.DOWN_LEFT);
    }

    /**
     * Changes the position by the coordinate changes of the DOWN_RIGHT
     * direction, meaning that the row and column coordinates will be increased by one.
     */
    public void setDownRight() {
        setTo(Direction.DOWN_RIGHT);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof Position p) && p.row == row && p.col == col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public Position clone() {
        Position copy;
        try {
            copy = (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        return copy;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }
}