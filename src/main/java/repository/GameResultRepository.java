package repository;

import results.GameResult;

import java.util.Comparator;
import java.util.List;

/**
 * Represents a repository or collection of {@link GameResult} objects.
 */
public class GameResultRepository extends GsonRepository<GameResult> {

    /**
     * Creates a {@code GameResultRepository} object
     * that can contain {@code GameResult} objects.
     */
    public GameResultRepository() {
        super(GameResult.class);
    }

    /**
     * Returns the best {@code a} game results from
     * the previous games.
     * @param a The number of results to be obtained.
     * @return A {@code List} of {@code GameResult} objects
     * representing the best {@code a} results from the
     * previous games.
     */
    public List<GameResult> findBestResults(int a) {
        return elements.stream()
                .sorted(Comparator.comparingInt(GameResult::getNumberOfMoves)
                        .thenComparing(GameResult::getTimeOfPlay, Comparator.reverseOrder()))
                .limit(a)
                .toList();
    }

}
