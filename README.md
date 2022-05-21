# Fox Catcher Game

This Apache Maven 3 project implements the Fox Catcher game,
which can be played on a chessboard with 4 black pawns and 1
white pawn. The black pawns are the dogs and the white
pawn is the fox. One of the players moves with the dogs,
and the other player moves with the fox. The two players are making
the moves alternately. The dogs can move one square diagonally, but only
forwards, and the fox can also move one square diagonally, but in contrast
with the dogs, it can move both forwards and backwards. The player
with the fox wins if he/she can guide the fox behind the dogs. The player
with the dogs wins if he/she can force the fox into a position in which it is
unable to move.

The project uses the MVC (model-view-controller) software design pattern.
The unit tests are written using JUnit 5.
