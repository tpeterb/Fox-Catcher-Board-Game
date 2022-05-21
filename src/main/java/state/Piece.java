package state;

import java.util.Objects;

/**
 * Represents a piece on the board.
 */
public class Piece implements Cloneable {

    /**
     * Initializes the piece with the given {@code PieceType} and {@code Position} objects.
     * @param pieceType
     * @param position
     */
    public Piece(PieceType pieceType, Position position) {
        this.pieceType = pieceType;
        this.position = position.clone();
    }

    /**
     * The type of the piece.
     */
    private final PieceType pieceType;

    /**
     * The position of the piece on the board.
     */
    private Position position;

    /**
     * Returns the type of the piece.
     * @return The type of the piece.
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Returns the position of the piece.
     * @return The position of the piece.
     */
    public Position getPosition() {
        return this.position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return (o instanceof Piece p) && this.getPieceType() == p.getPieceType()
                && this.getPosition().equals(p.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, position);
    }

    @Override
    public Piece clone() {
        Piece copy;
        try {
            copy = (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.position = this.position.clone();
        return copy;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.getPieceType(), this.position.toString());
    }
}
