package results;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Represents the result of a game.
 */
@Builder
@Data
public class GameResult {

    /**
     * The name of the first player.
     */
    private String playerOne;

    /**
     * The name of the second player.
     */
    private String playerTwo;

    /**
     * The name of the winner.
     */
    private String winner;

    /**
     * The number of moves the two players played.
     */
    private int numberOfMoves;

    /**
     * The time of finishing the game.
     */
    private ZonedDateTime timeOfPlay;
}
