package gui.controller;

import results.GameResult;
import repository.GameResultRepository;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.tinylog.Logger;
import state.*;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The controller class of the main view where the game can be played.
 */
public class TableGameController {

    private BoardState boardState;

    @FXML
    private VBox container;

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField numberOfMovesField;

    private IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    private ArrayList<Image> pieceImages = new ArrayList<Image>();

    private boolean isPieceChosen = false;

    private Position positionOfPieceToMove;

    /**
     * The name of the player playing with the dogs.
     */
    private String playerOneName;

    /**
     * The name of the player playing with the fox.
     */
    private String playerTwoName;

    private String winnerName;

    private GameResultRepository gameResultRepository = new GameResultRepository();

    /**
     * Sets the names of the two players.
     * @param playerOneName The name of the first player.
     * @param playerTwoName The name of the second player.
     */
    public void setPlayerNames(String playerOneName, String playerTwoName) {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
    }

    @FXML
    private void initialize() {
        File results = new File("results.json");
        if (results.exists()) {
            loadResults();
        }
        createControlBindings();
        loadImages();
        fillGridWithSquares();
        resetGame();
    }

    private void resetGame() {
        boardState = new BoardState();
        numberOfMoves.set(0);
        showBoardStateOnGrid();
        winnerName = "";
        Logger.debug("Game has been reset!");
    }

    private void removePiecesFromGrid() {
        for (int i = 0; i < boardState.getNumberOfPieces(); i++) {
            Position piecePosition = boardState.getPiece(i).getPosition();
            getNodeInGridByRowAndColumnIndex(gridPane, piecePosition.row(), piecePosition.col()).ifPresent(
                    node -> ((StackPane) node).getChildren().clear());
        }
    }

    private void showBoardStateOnGrid() {
        for (int i = 0; i < boardState.getNumberOfPieces(); i++) {
            Piece piece = boardState.getPiece(i);
            int row = piece.getPosition().row();
            int col = piece.getPosition().col();
            if (getNodeInGridByRowAndColumnIndex(gridPane, row, col).isPresent()) {
                StackPane square = (StackPane) getNodeInGridByRowAndColumnIndex(gridPane, row, col).get();
                ImageView pieceView = new ImageView();
                switch (piece.getPieceType()) {
                    case FOX -> {
                        pieceView.setImage(pieceImages.get(0));
                    }
                    case DOG -> {
                        pieceView.setImage(pieceImages.get(1));
                    }
                }
                square.getChildren().add(pieceView);
            }
        }
    }

    private void createControlBindings() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
        Logger.trace("Bindings created!");
    }

    private void fillGridWithSquares() {
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                var square = new StackPane();
                square.getStyleClass().add("square");
                if ((i + j) % 2 == 0) {
                    square.getStyleClass().add("white");
                } else {
                    square.getStyleClass().add("dark");
                }
                square.setOnMouseClicked(this::handleMouseClickOnSquare);
                gridPane.add(square, i, j);
            }
        }
    }

    private void handleGameOver() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game over");
        alert.setHeaderText("Result:");
        if (boardState.foxWins()) {
            winnerName = playerTwoName;
            alert.setContentText(winnerName + " wins in " + numberOfMoves.get() + " moves!");
        } else {
            winnerName = playerOneName;
            alert.setContentText(winnerName + " wins in " + numberOfMoves.get() + " moves!");
        }
        createGameResult();
        alert.showAndWait();
        removePiecesFromGrid();
        resetGame();
        switchToResultView();
    }

    private void makeMove(Direction direction) {
        boardState.getPieceIndexByPosition(positionOfPieceToMove).ifPresentOrElse( index -> {
                if (boardState.canMove(index, direction)) {
                    var oldBoardState = boardState.clone();
                    boardState.move(index, direction);
                    Logger.info("The new state after moving: {}", boardState);
                    updateBoardStateOnGrid(oldBoardState, positionOfPieceToMove, positionOfPieceToMove.getPositionAt(direction));
                    numberOfMoves.set(numberOfMoves.get() + 1);
                    if (boardState.isGoal()) {
                        Logger.info("Goal state reached!");
                        handleGameOver();
                    }
                }
            }, () -> Logger.debug("The move is not possible to make!")
        );
    }

    private void updateBoardStateOnGrid(BoardState oldBoardState, Position sourcePosition, Position destinationPosition) {
        if (getNodeInGridByRowAndColumnIndex(gridPane, sourcePosition.row(), sourcePosition.col()).isPresent()) {
            PieceType pieceTypeToMove = oldBoardState.getPiece(oldBoardState.getPieceIndexByPosition(sourcePosition).get()).getPieceType();
            ImageView pieceView = new ImageView();
            switch (pieceTypeToMove) {
                case FOX -> pieceView.setImage(pieceImages.get(0));
                case DOG -> pieceView.setImage(pieceImages.get(1));
            }
            StackPane square = (StackPane) getNodeInGridByRowAndColumnIndex(gridPane, sourcePosition.row(), sourcePosition.col()).get();
            square.getChildren().remove(square.getChildren().get(0));
            getNodeInGridByRowAndColumnIndex(gridPane, destinationPosition.row(), destinationPosition.col()).ifPresent(
                    node -> ((StackPane) node).getChildren().add(pieceView));
        }
    }

    @FXML
    private void handleMouseClickOnSquare(MouseEvent mouseEvent) {
        var eventSource = (Node) mouseEvent.getSource();
        int rowIndex = GridPane.getRowIndex(eventSource);
        int columnIndex = GridPane.getColumnIndex(eventSource);
        Position sourcePosition = new Position(rowIndex, columnIndex);
        Logger.info("A square was clicked at {}", sourcePosition.toString());
        if (!boardState.isSquareEmpty(sourcePosition)) {
            positionOfPieceToMove = sourcePosition;
            isPieceChosen = true;
            Logger.debug("Piece chosen at {}", positionOfPieceToMove);
        } else {
            if (isPieceChosen) {
                Optional<Direction> directionToMoveIn = getDirectionFromClick(sourcePosition);
                directionToMoveIn.ifPresent(direction -> {
                    Logger.debug("The chosen direction exists");
                    makeMove(direction);
                    isPieceChosen = false;
                });
            }
        }
    }

    private void loadImages() {
        String[] imagePaths = new String[] {
                "/images/white-pawn.png",
                "/images/black-pawn.png"
        };
        for (var path : imagePaths) {
            Logger.debug("Loading piece image: {}", path);
            pieceImages.add(new Image(path));
        }
    }

    private void loadResults() {
        try {
            gameResultRepository.loadFromFile(new File("results.json"));
            Logger.debug("Game results successfully loaded!");
        } catch (IOException e) {
            Logger.warn("Results could not be loaded!");
        }
    }

    private Optional<Node> getNodeInGridByRowAndColumnIndex(GridPane gridPane, Integer row, Integer col) {
        Optional<Node> square = Optional.empty();
        for (Node node : gridPane.getChildren()) {
            if (row.equals(GridPane.getRowIndex(node)) && col.equals(GridPane.getColumnIndex(node))) {
                square = Optional.of(node);
            }
        }
        return square;
    }

    private boolean isCorrectPieceTypeClickedToMove(Position sourcePosition) {
        if (boardState.getPieceIndexByPosition(sourcePosition).isPresent()) {
            return boardState.getPieceTypeToMove() == boardState.getPiece(boardState.getPieceIndexByPosition(sourcePosition).get()).getPieceType();
        }
        return false;
    }

    private Optional<Direction> getDirectionFromClick(Position destinationPosition) {
        Direction directionToMoveIn = null;
        try {
            directionToMoveIn = Direction.of(destinationPosition.row() - positionOfPieceToMove.row(),
                    destinationPosition.col() - positionOfPieceToMove.col());
            Logger.debug("Direction to move in is: {}", directionToMoveIn);
        } catch (IllegalArgumentException e) {
            Logger.warn("Direction is not valid!");
        }
        return Optional.ofNullable(directionToMoveIn);
    }

    private void createGameResult() {
        try {
            var gameResult = GameResult.builder()
                    .playerOne(playerOneName)
                    .playerTwo(playerTwoName)
                    .winner(winnerName)
                    .numberOfMoves(numberOfMoves.get())
                    .timeOfPlay(ZonedDateTime.now())
                    .build();
            gameResultRepository.add(gameResult);
            gameResultRepository.saveToFile(new File("results.json"));
            Logger.debug("Game result successfully created!");
        } catch (IOException e) {
            Logger.warn("Results could not be saved!");
        }
    }

    private void switchToResultView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/resultView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) container.getScene().getWindow();
            Scene scene = new Scene(root);
            String css = this.getClass().getResource("/css/resultView.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
            Logger.info("Switching from the main view to the result view");
        } catch (IOException e) {
            Logger.warn("Result view could not be loaded!");
        }
    }
}
