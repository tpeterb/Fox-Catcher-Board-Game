package state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import state.Direction;
import state.Position;

import java.util.ArrayList;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position position;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col()));
    }

    static Stream<Position> positionProvider() {
        return Stream.of(
                new Position(3,5),
                new Position(1,2),
                new Position(6,4),
                new Position(4,3)
        );
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getPositionAt(Position position) {
        assertPosition(position.row() - 1, position.col() - 1,
                position.getPositionAt(Direction.UP_LEFT));
        assertPosition(position.row() - 1, position.col() + 1,
                position.getPositionAt(Direction.UP_RIGHT));
        assertPosition(position.row() + 1, position.col() - 1,
                position.getPositionAt(Direction.DOWN_LEFT));
        assertPosition(position.row() + 1, position.col() + 1,
                position.getPositionAt(Direction.DOWN_RIGHT));
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getUpLeft(Position position) {
        assertPosition(position.row() - 1, position.col() - 1,
                position.getUpLeft());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getUpRight(Position position) {
        assertPosition(position.row() - 1, position.col() + 1,
                position.getUpRight());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getDownLeft(Position position) {
        assertPosition(position.row() + 1, position.col() - 1,
                position.getDownLeft());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void getDownRight(Position position) {
        assertPosition(position.row() + 1, position.col() + 1,
                position.getDownRight());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void setTo_up_left(Position position) {
        Position positionCopy = position.clone();
        positionCopy.setTo(Direction.UP_LEFT);
        assertPosition(position.row() - 1, position.col() - 1,
                positionCopy);
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void setTo_up_right(Position position) {
        Position positionCopy = position.clone();
        positionCopy.setTo(Direction.UP_RIGHT);
        assertPosition(position.row() - 1, position.col() + 1,
                positionCopy);
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void setTo_down_left(Position position) {
        Position positionCopy = position.clone();
        positionCopy.setTo(Direction.DOWN_LEFT);
        assertPosition(position.row() + 1, position.col() - 1,
                positionCopy);
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void setTo_down_right(Position position) {
        Position positionCopy = position.clone();
        positionCopy.setTo(Direction.DOWN_RIGHT);
        assertPosition(position.row() + 1, position.col() + 1,
                positionCopy);
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void setUpLeft(Position position) {
        Position positionCopy = position.clone();
        positionCopy.setUpLeft();
        assertPosition(position.row() - 1, position.col() - 1,
                positionCopy);
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void setUpRight(Position position) {
        Position positionCopy = position.clone();
        positionCopy.setUpRight();
        assertPosition(position.row() - 1, position.col() + 1,
                positionCopy);
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void setDownLeft(Position position) {
        Position positionCopy = position.clone();
        positionCopy.setDownLeft();
        assertPosition(position.row() + 1, position.col() - 1,
                positionCopy);
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void setDownRight(Position position) {
        Position positionCopy = position.clone();
        positionCopy.setDownRight();
        assertPosition(position.row() + 1, position.col() + 1,
                positionCopy);
    }

    @Test
    void testEquals() {
        position = new Position(3, 8);
        assertTrue(position.equals(position));
        assertTrue(position.equals(new Position(3, 8)));
        assertTrue(position.equals(position.clone()));
        assertFalse(position.equals(new Position(3, 7)));
        assertFalse(position.equals(new Position(8, 8)));
        assertFalse(position.equals(new ArrayList<String>()));
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void testHashCode(Position position) {
        assertTrue(position.hashCode() == position.hashCode());
        assertTrue(position.hashCode() == new Position(
                position.row(), position.col()).hashCode());
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void testClone(Position position) {
        Position positionCopy = position.clone();

        assertEquals(position, positionCopy);
        assertTrue(position.row() == positionCopy.row());
        assertTrue(position.col() == positionCopy.col());
        assertNotSame(position, positionCopy);
    }

    @ParameterizedTest
    @MethodSource("positionProvider")
    void testToString(Position position) {
        assertEquals(String.format("(%d,%d)", position.row(),position.col()),
                position.toString());
    }
}