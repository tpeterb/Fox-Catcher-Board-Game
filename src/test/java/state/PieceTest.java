package state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import state.Piece;
import state.PieceType;
import state.Position;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    Piece piece;

    static Stream<Piece> pieceProvider() {
        return Stream.of(
                new Piece(PieceType.FOX, new Position(2, 1)),
                new Piece(PieceType.DOG, new Position(3, 5)),
                new Piece(PieceType.FOX, new Position(5, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("pieceProvider")
    void testEquals(Piece piece) {
        assertTrue(piece.equals(piece));
        assertTrue(piece.equals(piece.clone()));
        assertFalse(piece.equals(new Piece(PieceType.DOG, new Position(Integer.MIN_VALUE, Integer.MAX_VALUE))));
        assertFalse(piece.equals(null));
        assertFalse(piece.equals("Hello World!"));
    }

    @ParameterizedTest
    @MethodSource("pieceProvider")
    void testHashCode(Piece piece) {
        assertTrue(piece.hashCode() == piece.hashCode());
        assertTrue(piece.hashCode() == new Piece(piece.getPieceType(), piece.getPosition()).hashCode());
    }

    @Test
    void testToString() {
        piece = new Piece(PieceType.FOX, new Position(5, 3));
        assertEquals("FOX: (5,3)", piece.toString());
    }

    @ParameterizedTest
    @MethodSource("pieceProvider")
    void testClone(Piece piece) {
        Piece copy = piece.clone();
        assertSame(piece.getPieceType(), copy.getPieceType());
        assertEquals(piece.getPosition(), copy.getPosition());
        assertNotSame(piece.getPosition(), copy.getPosition());
        assertEquals(piece, copy);
        assertNotSame(piece, copy);
    }
}