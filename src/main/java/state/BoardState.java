package state;

import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the state of the Fox Catcher game.
 */
public class BoardState implements Cloneable {

    /**
     * The size of the board the game is played on.
     */
    public static final int BOARD_SIZE = 8;

    /**
     * The array containing the pieces.
     */
    private Piece[] pieces;

    /**
     * The type of the piece that can be moved next time.
     */
    private PieceType pieceTypeToMove;

    /**
     * Creates a {@code BoardState} object with the pieces given.
     * This constructor makes it possible to define the pieces with
     * positions that are different from the ordinary ones. The constructor
     * expects a {@code PieceType} which specifies the type of the piece that can
     * be moved first, and either five {@code Piece} objects, or an array of five
     * {@code Piece} objects.
     * @param pieceTypeToMove The type of the piece that can be moved first.
     * @param pieces The pieces with which the game will be played.
     */
    public BoardState(PieceType pieceTypeToMove, Piece... pieces) {
        if (!(arePiecePositionsValid(pieces) && isFoxGiven(pieces))) {
            throw new IllegalArgumentException();
        }
        this.pieceTypeToMove = pieceTypeToMove;
        this.pieces = deepClone(pieces);
    }

    /**
     * Creates a {@code BoardState} object that represents the initial state
     * of the game. All pieces are placed in those positions that are specified in
     * the rules of the game. The constructor expects a {@code PieceType} that represents
     * the type of piece that can be moved first.
     * @param pieceTypeToMove The type of the piece that can be moved first.
     */
    public BoardState(PieceType pieceTypeToMove) {
        this(pieceTypeToMove,
                new Piece(PieceType.FOX, new Position(0, 2)),
                new Piece(PieceType.DOG, new Position(7, 1)),
                new Piece(PieceType.DOG, new Position(7, 3)),
                new Piece(PieceType.DOG, new Position(7, 5)),
                new Piece(PieceType.DOG, new Position(7, 7)));
    }

    /**
     * Creates a {@code BoardState} object which represents the initial state
     * of the game. All pieces are placed in those positions that are specified
     * in the rules of the game. By default, the dogs can be moved first.
     */
    public BoardState() {
        this(PieceType.DOG,
                new Piece(PieceType.FOX, new Position(0, 2)),
                new Piece(PieceType.DOG, new Position(7, 1)),
                new Piece(PieceType.DOG, new Position(7, 3)),
                new Piece(PieceType.DOG, new Position(7, 5)),
                new Piece(PieceType.DOG, new Position(7, 7)));
    }

    /**
     * Returns true if the given position is on the board. Otherwise,
     * the method returns false.
     * @param position The position to be checked.
     * @return True if the given position is on the board. Otherwise,
     * the method returns false.
     */
    private boolean isPositionValid(Position position) {
        return position.row() >= 0 && position.row() < BOARD_SIZE
                && position.col() >= 0 && position.col() < BOARD_SIZE;
    }

    /**
     * Returns true if there are exactly five pieces given
     * and if their positions are valid. Otherwise, the method returns false.
     * @param pieces The pieces with which the game would be played.
     * @return True if there are exactly five pieces specified and if
     * their positions are valid. Otherwise, the method returns false.
     */
    private boolean arePiecePositionsValid(Piece[] pieces) {
        if (pieces.length != 5) {
            Logger.error("The number of pieces given is not equal to five!");
            return false;
        }
        for (int i = 0; i < pieces.length; i++) {
            if (!isPositionValid(pieces[i].getPosition())) {
                Logger.error("One of the positions given does not correspond to a position on the board!");
                return false;
            }
            if (i < pieces.length - 1) {
                for (int j = i + 1; j < pieces.length; j++) {
                    if (pieces[i].getPosition().equals(pieces[j].getPosition())) {
                        Logger.error("At least two pieces have the same positions, which is not permitted!");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns the number of pieces on the board.
     * @return The number of pieces on the board.
     */
    public int getNumberOfPieces() {
        return pieces.length;
    }

    /**
     * Returns a copy of the {@code Piece} object at the given index.
     * @param index The index of the {@code Piece} object to be returned.
     * @return The copy of the {@code Piece} object at the given index.
     */
    public Piece getPiece(int index) {
        return pieces[index].clone();
    }

    /**
     * Returns an {@code Optional} object that wraps the index of
     * the piece whose position matches the position specified in tha parameter list.
     * If there's no piece in the given position, the method returns an empty {@code Optional} object.
     * @param position The position of the piece.
     * @return An {@code Optional} object which wraps the index of the piece whose position
     * matches the position specified in tha parameter list. If there's no piece in the given
     * position, the method returns an empty {@code Optional} object.
     */
    public Optional<Integer> getPieceIndexByPosition(Position position) {
        if (!isPositionValid(position)) {
            return Optional.empty();
        }
        Optional<Integer> index = Optional.empty();
        for (int i = 0; i < this.pieces.length; i++) {
            if (this.pieces[i].getPosition().equals(position)) {
                index = Optional.of(i);
                return index;
            }
        }
        return index;
    }

    /**
     * Returns the type of the piece that can be moved next.
     * @return The type of the piece that can be moved next.
     */
    public PieceType getPieceTypeToMove() {
        return this.pieceTypeToMove;
    }

    /**
     * Returns the index of the fox.
     * @return The index of the fox.
     */
    private int getFoxIndex() {
        int index = -1;
        for (int i = 0; i < this.getNumberOfPieces(); i++) {
            if (this.pieces[i].getPieceType() == PieceType.FOX) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Returns true if the {@code Piece} at the given index can
     * be moved in the specified direction. Otherwise, the method
     * returns false.
     * @param index The index of the {@code Piece} to be examined.
     * @param direction The direction in which the {@code Piece} would be moved.
     * @return True if the {@code Piece} at the given index can
     * be moved in the specified direction. Otherwise, the method
     * returns false.
     */
    public boolean canMove(int index, Direction direction) {
        if (isIndexInvalid(index)) {
            Logger.warn("The index passed to the canMove() method does not correspond to an index of a piece!");
            throw new IllegalArgumentException();
        }
        if (index == this.getFoxIndex()) {
            return canFoxMove(index, direction);
        } else {
            return canDogMove(index, direction);
        }
    }

    /**
     * Returns true if the fox can be moved in the direction given.
     * If the fox cannot be moved in the direction specified, the method
     * returns false.
     * @param index The index of the fox.
     * @param direction The direction in which the fox should be moved.
     * @return True if the fox can be moved in the direction given.
     * If the fox cannot be moved in the direction specified, the method
     * returns false.
     */
    private boolean canFoxMove(int index, Direction direction) {
        if (this.pieceTypeToMove != PieceType.FOX) {
            Logger.info("The fox can be moved on the next turn!");
            return false;
        }
        return switch (direction) {
            case UP_LEFT -> canMoveUpLeft(index);
            case UP_RIGHT -> canMoveUpRight(index);
            case DOWN_LEFT -> canMoveDownLeft(index);
            case DOWN_RIGHT -> canMoveDownRight(index);
        };
    }

    /**
     * Returns true if the dog at the given index can be moved in the specified direction.
     * If moving the dog in the given direction is not possible, the method returns false.
     * @param index The index of the dog.
     * @param direction The direction in which the dog should be moved.
     * @return True if the dog at the given index can be moved in the specified direction.
     * If moving the dog in the given direction is not possible, the method returns false.
     */
    private boolean canDogMove(int index, Direction direction) {
        if (this.pieceTypeToMove != PieceType.DOG) {
            Logger.info("The dog can be moved on the next turn!");
            return false;
        }
        return switch (direction) {
            case UP_LEFT -> canMoveUpLeft(index);
            case UP_RIGHT -> canMoveUpRight(index);
            case DOWN_LEFT, DOWN_RIGHT -> false;
        };
    }

    /**
     * Returns true if the piece at the given index can be moved up left. Otherwise,
     * the method returns false.
     * @param index The index of the piece to be checked.
     * @return True if the piece at the given index can be moved up left. Otherwise,
     * the method returns false.
     */
    private boolean canMoveUpLeft(int index) {
        if (isInFirstRow(index) || isInFirstColumn(index)) {
            return false;
        }
        if (isSquareEmpty(this.pieces[index].getPosition().getPositionAt(Direction.UP_LEFT))) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the piece at the given index can be moved up right. Otherwise,
     * the method returns false.
     * @param index The index of the piece to be checked.
     * @return True if the piece at the given index can be moved up right. Otherwise,
     * the method returns false.
     */
    private boolean canMoveUpRight(int index) {
        if (isInFirstRow(index) || isInLastColumn(index)) {
            return false;
        }
        if (isSquareEmpty(this.pieces[index].getPosition().getPositionAt(Direction.UP_RIGHT))) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the piece at the given index can be moved down left. Otherwise,
     * the method returns false.
     * @param index The index of the piece to be checked.
     * @return True if the piece at the given index can be moved down left. Otherwise,
     * the method returns false.
     */
    private boolean canMoveDownLeft(int index) {
        if (isInLastRow(index) || isInFirstColumn(index)) {
            return false;
        }
        if (isSquareEmpty(this.pieces[index].getPosition().getPositionAt(Direction.DOWN_LEFT))) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if the piece at the given index can be moved down right. Otherwise,
     * the method returns false.
     * @param index The index of the piece to be checked.
     * @return True if the piece at the given index can be moved down right. Otherwise,
     * the method returns false.
     */
    private boolean canMoveDownRight(int index) {
        if (isInLastRow(index) || isInLastColumn(index)) {
            return false;
        }
        if (isSquareEmpty(this.pieces[index].getPosition().getPositionAt(Direction.DOWN_RIGHT))) {
            return true;
        }
        return false;
    }

    /**
     * Moves the {@link Piece} at the given in the specified direction.
     * @param index The index of the {@code Piece} to be moved.
     * @param direction The direction in which the {@code Piece} should be moved.
     */
    public void move(int index, Direction direction) {
        if (isIndexInvalid(index)) {
            Logger.error("The index passed to the move() method does not correspond to an index of a piece!");
            throw new IllegalArgumentException();
        }
        if (index == this.getFoxIndex()) {
            moveFox(index, direction);
        } else {
            moveDog(index, direction);
        }
    }

    /**
     * Moves the fox in the direction specified.
     * @param index The index of the fox.
     * @param direction The direction in which the fox should be moved.
     */
    private void moveFox(int index, Direction direction) {
        if (canFoxMove(index, direction)) {
            pieces[this.getFoxIndex()].getPosition().setTo(direction);
            this.pieceTypeToMove = PieceType.DOG;
        }
    }

    /**
     * Moves the dog at the given index in the direction specified.
     * @param index The index of the dog.
     * @param direction The direction in which the dog should be moved.
     */
    private void moveDog(int index, Direction direction) {
        if (canDogMove(index, direction)) {
            pieces[index].getPosition().setTo(direction);
            this.pieceTypeToMove = PieceType.FOX;
        }
    }

    /**
     * Determines if the current state of the game is a goal state or not.
     * @return True if the {@link BoardState} object represents a goal state
     * of the game. Otherwise, the method returns false.
     */
    public boolean isGoal() {
        return foxWins() || dogWins();
    }

    /**
     * Determines if the {@code BoardState} object represents a state of the
     * game in which the fox wins.
     * @return True if the {@code BoardState} object represents a state in which the
     * fox wins. Otherwise, the method returns false.
     */
    public boolean foxWins() {
        int numberOfBypassedDogs = 0;
        for (int i = 0; i < this.getNumberOfPieces(); i++) {
            if (pieces[this.getFoxIndex()].getPosition().row() > pieces[i].getPosition().row()) {
                numberOfBypassedDogs++;
            }
        }
        if (numberOfBypassedDogs == 4) {
            return true;
        }
        return false;
    }

    /**
     * Determines if the {@code BoardState} object represents a state
     * of the game in which the dogs win.
     * @return True if the {@code BoardState} object represents a state
     * where the dogs win. Otherwise, the method returns false.
     */
    public boolean dogWins() {
        if (getPossibleMoves(this.getFoxIndex()).isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Returns the possible moves of the {@link Piece} at the given index.
     * @param index The index of the piece.
     * @return An {@link ArrayList} containing the directions in which the piece
     * at the given index can be moved. If the piece at the given index cannot move,
     * the method returns an empty {@code ArrayList}.
     */
    public ArrayList<Direction> getPossibleMoves(int index) {
        ArrayList<Direction> directions = new ArrayList<Direction>();
        if (index == this.getFoxIndex()) {
            if (canMoveUpLeft(index)) {
                directions.add(Direction.UP_LEFT);
            }
            if (canMoveUpRight(index)) {
                directions.add(Direction.UP_RIGHT);
            }
            if (canMoveDownLeft(index)) {
                directions.add(Direction.DOWN_LEFT);
            }
            if (canMoveDownRight(index)) {
                directions.add(Direction.DOWN_RIGHT);
            }
        } else {
            if (canMoveUpLeft(index)) {
                directions.add(Direction.UP_LEFT);
            }
            if (canMoveUpRight(index)) {
                directions.add(Direction.UP_RIGHT);
            }
        }
        return directions;
    }

    /**
     * Determines if a fox has been given among the pieces.
     * @param pieces An array of {@code Piece} objects that represent
     * the pieces on the board.
     * @return True if a fox has been given among the pieces. If there was no
     * fox given, the method returns false.
     */
    private boolean isFoxGiven(Piece[] pieces) {
        for (Piece piece : pieces) {
            if (piece.getPieceType() == PieceType.FOX) {
                return true;
            }
        }
        Logger.warn("There's no fox given among the pieces!");
        return false;
    }

    /**
     * Returns true if the square at the specified position is empty.
     * Otherwise, this method returns false;
     * @param position The position to be checked whether the square at the
     * specified position is empty or not.
     * @return True if the square at the given position is empty. Otherwise, this method
     * returns false;
     */
    public boolean isSquareEmpty(Position position) {
        for (var piece : pieces) {
            if (piece.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the piece at the specified index is in the first row
     * of the board. Otherwise, the method returns false.
     * @param index The index of the piece.
     * @return True if the piece at the given index is in the first row
     * of the board. Otherwise, the method returns false;
     */
    private boolean isInFirstRow(int index) {
        if (isIndexInvalid(index)) {
            Logger.error("The index passed to the isInFirstRow() method does not correspond to an index of a piece!");
            throw new IllegalArgumentException();
        }
        return pieces[index].getPosition().row() == 0;
    }

    /**
     * Returns true if the piece at the specified index is in the
     * last row of the board. Otherwise, the method returns false.
     * @param index The index of the piece.
     * @return True if the piece at the specified index is in the
     * last row of the board. Otherwise, the method returns false.
     */
    private boolean isInLastRow(int index) {
        if (isIndexInvalid(index)) {
            Logger.error("The index passed to the isInLastRow() method does not correspond to an index of a piece!");
            throw new IllegalArgumentException();
        }
        return pieces[index].getPosition().row() == BOARD_SIZE - 1;
    }

    /**
     * Returns true if the piece at the given index is in the first column
     * of the board. Otherwise, the method returns false.
     * @param index The index of the piece.
     * @return True if the piece at the specified index is in the first column
     * of the board. Otherwise, the method returns false.
     */
    private boolean isInFirstColumn(int index) {
        if (isIndexInvalid(index)) {
            Logger.error("The index passed to the isInFirstColumn() method does not correspond to an index of a piece!");
            throw new IllegalArgumentException();
        }
        return pieces[index].getPosition().col() == 0;
    }

    /**
     * Returns true if the piece at the given index is in the last column of the board.
     * Otherwise, this method returns false.
     * @param index The index of the piece.
     * @return True if the piece at the given index is in the last column of the board.
     * Otherwise, this method returns false.
     */
    private boolean isInLastColumn(int index) {
        if (isIndexInvalid(index)) {
            Logger.error("The index passed to the isInLastColumn() method does not correspond to an index of a piece!");
            throw new IllegalArgumentException();
        }
        return pieces[index].getPosition().col() == BOARD_SIZE - 1;
    }

    /**
     * Determines whether the given index corresponds to
     * an index of a piece on the board or not.
     * @param index The index to be checked.
     * @return True if the given index does not correspond to
     * an index of a piece. Otherwise, the method returns false.
     */
    private boolean isIndexInvalid(int index) {
        return index < 0 || index > getNumberOfPieces() - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return (o instanceof BoardState boardState) &&
                this.pieceTypeToMove == boardState.pieceTypeToMove &&
                Arrays.equals(this.pieces, boardState.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieces, pieceTypeToMove);
    }

    @Override
    public String toString() { // "{DOG, [FOX: (0, 2)], [...], ...}"
        String result = "{";
        result += pieceTypeToMove + ", ";
        for (var piece : pieces) {
            if (!piece.equals(pieces[pieces.length - 1])) {
                result += "[" + piece.toString() + "], ";
            } else {
                result += "[" + piece.toString() + "]}";
            }
        }
        return result;
    }

    @Override
    public BoardState clone() {
        BoardState copy;
        try {
            copy = (BoardState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.pieces = deepClone(this.pieces);
        return copy;
    }

    private static Piece[] deepClone(Piece[] pieces) {
        Piece[] copy = pieces.clone();
        for (int i = 0; i < pieces.length; i++) {
            copy[i] = pieces[i].clone();
        }
        return copy;
    }
}
