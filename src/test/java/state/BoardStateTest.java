package state;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardStateTest {

    BoardState state1 = new BoardState(); // original initial state

    BoardState state2 = new BoardState( // a non-final state
            PieceType.DOG,
            new Piece(PieceType.FOX, new Position(1, 1)),
            new Piece(PieceType.DOG, new Position(5, 1)),
            new Piece(PieceType.DOG, new Position(4, 2)),
            new Piece(PieceType.DOG, new Position(6, 6)),
            new Piece(PieceType.DOG, new Position(4, 6))
    );

    BoardState state3 = new BoardState( // a final state where the fox wins
            PieceType.DOG,
            new Piece(PieceType.FOX, new Position(6, 4)),
            new Piece(PieceType.DOG, new Position(5, 1)),
            new Piece(PieceType.DOG, new Position(4, 2)),
            new Piece(PieceType.DOG, new Position(5, 3)),
            new Piece(PieceType.DOG, new Position(4, 4))
    );

    BoardState state4 = new BoardState( // a final state where the dogs win
            PieceType.FOX,
            new Piece(PieceType.FOX, new Position(0, 4)),
            new Piece(PieceType.DOG, new Position(2, 4)),
            new Piece(PieceType.DOG, new Position(1, 3)),
            new Piece(PieceType.DOG, new Position(3, 3)),
            new Piece(PieceType.DOG, new Position(1, 5))
    );

    @Test
    void getPieceIndexByPosition() {
        assertTrue(0 == state2.getPieceIndexByPosition(new Position(1, 1)).get());
        assertTrue(2 == state3.getPieceIndexByPosition(new Position(4, 2)).get());
        assertTrue(3 == state4.getPieceIndexByPosition(new Position(3, 3)).get());
        assertFalse(0 == state4.getPieceIndexByPosition(new Position(1, 5)).get());
        assertEquals(Optional.empty(), state2.getPieceIndexByPosition(new Position(5, 5)));
        assertEquals(Optional.empty(), state2.getPieceIndexByPosition(new Position(3, -1)));
    }

    @Test
    void canMove() {
        assertTrue(state1.canMove(0, Direction.DOWN_RIGHT));
        assertFalse(state1.canMove(0, Direction.UP_LEFT));
        assertTrue(state1.canMove(1, Direction.UP_RIGHT));
        assertFalse(state1.canMove(4, Direction.DOWN_LEFT));
        assertFalse(state3.canMove(1, Direction.UP_RIGHT));
        assertFalse(state4.canMove(0, Direction.DOWN_LEFT));
    }

    @Test
    void canMove_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> state1.canMove(5, Direction.UP_LEFT));
    }

    @Test
    void move() {
        BoardState state1Copy = state1.clone();
        state1Copy.move(1, Direction.UP_LEFT);
        assertEquals(new BoardState(PieceType.FOX,
                new Piece(PieceType.FOX, new Position(0, 2)),
                new Piece(PieceType.DOG, new Position(6, 0)),
                new Piece(PieceType.DOG, new Position(7, 3)),
                new Piece(PieceType.DOG, new Position(7, 5)),
                new Piece(PieceType.DOG, new Position(7, 7))),
                state1Copy);
        BoardState state2Copy = state2.clone();
        state2Copy.move(3, Direction.UP_RIGHT);
        assertEquals(new BoardState(PieceType.FOX,
                new Piece(PieceType.FOX, new Position(1, 1)),
                new Piece(PieceType.DOG, new Position(5, 1)),
                new Piece(PieceType.DOG, new Position(4, 2)),
                new Piece(PieceType.DOG, new Position(5, 7)),
                new Piece(PieceType.DOG, new Position(4, 6))),
                state2Copy);
        BoardState state = new BoardState(PieceType.FOX,
                new Piece(PieceType.FOX, new Position(1, 3)),
                new Piece(PieceType.DOG, new Position(2, 0)),
                new Piece(PieceType.DOG, new Position(4, 4)),
                new Piece(PieceType.DOG, new Position(6, 6)),
                new Piece(PieceType.DOG, new Position(7, 7)));
        state.move(0, Direction.DOWN_RIGHT);
        assertEquals(new BoardState(PieceType.DOG,
                new Piece(PieceType.FOX, new Position(2, 4)),
                new Piece(PieceType.DOG, new Position(2, 0)),
                new Piece(PieceType.DOG, new Position(4, 4)),
                new Piece(PieceType.DOG, new Position(6, 6)),
                new Piece(PieceType.DOG, new Position(7, 7))),
                state);
    }

    @Test
    void move_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> state3.move(-1, Direction.DOWN_LEFT));
    }

    @Test
    void isGoal() {
        assertFalse(state1.isGoal());
        assertFalse(state2.isGoal());
        assertTrue(state3.isGoal());
        assertTrue(state4.isGoal());
    }

    @Test
    void foxWins() {
        assertFalse(state1.foxWins());
        assertFalse(state2.foxWins());
        assertTrue(state3.foxWins());
        assertFalse(state4.foxWins());
    }

    @Test
    void dogWins() {
        assertFalse(state1.dogWins());
        assertFalse(state2.dogWins());
        assertFalse(state3.dogWins());
        assertTrue(state4.dogWins());
    }

    @Test
    void getPossibleMoves() {
        ArrayList<Direction> possibleDirectionsOfFoxInStateOne = new ArrayList<Direction>();
        possibleDirectionsOfFoxInStateOne.add(Direction.DOWN_LEFT);
        possibleDirectionsOfFoxInStateOne.add(Direction.DOWN_RIGHT);
        assertEquals(possibleDirectionsOfFoxInStateOne,
                state1.getPossibleMoves(0));
        ArrayList<Direction> possibleDirectionsOfFirstDogInStateTwo = new ArrayList<Direction>();
        possibleDirectionsOfFirstDogInStateTwo.add(Direction.UP_LEFT);
        assertEquals(possibleDirectionsOfFirstDogInStateTwo,
                state2.getPossibleMoves(1));
    }

    @Test
    void isSquareEmpty() {
        assertTrue(state1.isSquareEmpty(new Position(5, 5)));
        assertTrue(state3.isSquareEmpty(new Position(6, 3)));
        assertFalse(state4.isSquareEmpty(new Position(1, 3)));
        assertFalse(state2.isSquareEmpty(new Position(6, 6)));
    }

    @Test
    void testConstructor_invalid() {
        assertThrows(IllegalArgumentException.class, () -> new BoardState(
                    PieceType.FOX,
                    new Piece(PieceType.FOX, new Position(1, 1)),
                    new Piece(PieceType.DOG, new Position(0, 2)),
                    new Piece(PieceType.DOG, new Position(7, 4)),
                    new Piece(PieceType.DOG, new Position(5, 3)),
                    new Piece(PieceType.DOG, new Position(2, 5)),
                    new Piece(PieceType.DOG, new Position(6, 3)))
        );
        assertThrows(IllegalArgumentException.class, () -> new BoardState(
                    PieceType.DOG,
                    new Piece(PieceType.FOX, new Position(1, 1)),
                    new Piece(PieceType.DOG, new Position(0, 2)),
                    new Piece(PieceType.DOG, new Position(7, 4)),
                    new Piece(PieceType.DOG, new Position(5, 3)),
                    new Piece(PieceType.DOG, new Position(2, 8))
        ));
        assertThrows(IllegalArgumentException.class, () -> new BoardState(
                    PieceType.FOX,
                    new Piece(PieceType.FOX, new Position(6, 3)),
                    new Piece(PieceType.DOG, new Position(4, 7)),
                    new Piece(PieceType.DOG, new Position(2, 5)),
                    new Piece(PieceType.DOG, new Position(5, 6)),
                    new Piece(PieceType.DOG, new Position(4, 7))
        ));
        assertThrows(IllegalArgumentException.class, () -> new BoardState(
                    PieceType.DOG,
                    new Piece(PieceType.DOG, new Position(6, 3)),
                    new Piece(PieceType.DOG, new Position(4, 7)),
                    new Piece(PieceType.DOG, new Position(2, 5)),
                    new Piece(PieceType.DOG, new Position(5, 6)),
                    new Piece(PieceType.DOG, new Position(4, 4))
        ));
    }

    @Test
    void testEquals() {
        assertTrue(state3.equals(state3.clone()));
        assertTrue(state3.equals(new BoardState(
                PieceType.DOG,
                new Piece(PieceType.FOX, new Position(6, 4)),
                new Piece(PieceType.DOG, new Position(5, 1)),
                new Piece(PieceType.DOG, new Position(4, 2)),
                new Piece(PieceType.DOG, new Position(5, 3)),
                new Piece(PieceType.DOG, new Position(4, 4)))));
        assertFalse(state3.equals(state4));
        assertFalse(state3.equals(new BoardState()));
        assertFalse(state2.equals(Integer.MAX_VALUE));
    }

    @Test
    void testHashCode() {
        assertTrue(state1.hashCode() == state1.hashCode());
        assertFalse(state2.hashCode() == state3.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("{DOG, [FOX: (1,1)], [DOG: (5,1)], [DOG: (4,2)], [DOG: (6,6)], [DOG: (4,6)]}",
                state2.toString());
    }

    @Test
    void testClone() {
        BoardState stateCopy = state4.clone();
        assertTrue(state4.equals(stateCopy));
        assertNotSame(state4, stateCopy);
    }
}