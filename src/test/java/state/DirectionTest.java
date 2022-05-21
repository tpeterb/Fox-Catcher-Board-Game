package state;

import org.junit.jupiter.api.Test;
import state.Direction;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {
    @Test
    void of() {
        assertSame(Direction.UP_LEFT, Direction.of(-1, -1));
        assertSame(Direction.UP_RIGHT, Direction.of(-1, 1));
        assertSame(Direction.DOWN_LEFT, Direction.of(1, -1));
        assertSame(Direction.DOWN_RIGHT, Direction.of(1, 1));
    }

    @Test
    void of_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Direction.of(1, 0));
    }
}