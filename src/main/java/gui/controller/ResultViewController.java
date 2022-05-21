package gui.controller;

import results.GameResult;
import repository.GameResultRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * The controller class of the result view which shows the best results.
 */
public class ResultViewController {

    @FXML
    private TableView<GameResult> scoreTable;

    @FXML
    private TableColumn<GameResult, String> playerOneColumn;

    @FXML
    private TableColumn<GameResult, String> playerTwoColumn;

    @FXML
    private TableColumn<GameResult, String> winnerColumn;

    @FXML
    private TableColumn<GameResult, Integer> numberOfMovesColumn;

    @FXML
    private TableColumn<GameResult, ZonedDateTime> timeOfPlayColumn;

    @FXML
    private Button newGameButton;

    private GameResultRepository gameResultRepository = new GameResultRepository();
    
    @FXML
    private void initialize() {

        Logger.info("Loading the results from the previous games");
        loadResults();

        List<GameResult> bestResults = gameResultRepository.findBestResults(15);

        playerOneColumn.setCellValueFactory(new PropertyValueFactory<GameResult, String>("playerOne"));
        playerTwoColumn.setCellValueFactory(new PropertyValueFactory<GameResult, String>("playerTwo"));
        winnerColumn.setCellValueFactory(new PropertyValueFactory<GameResult, String>("winner"));
        numberOfMovesColumn.setCellValueFactory(new PropertyValueFactory<GameResult, Integer>("numberOfMoves"));
        timeOfPlayColumn.setCellValueFactory(new PropertyValueFactory<GameResult, ZonedDateTime>("timeOfPlay"));

        ObservableList<GameResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(bestResults);
        scoreTable.setItems(observableResult);
    }

    /**
     * When the New Game button is pressed on the result view, this method is invoked and
     * it switches to the start view.
     * @param actionEvent The {@code ActionEvent} that represents the event when the New Game button was fired.
     * @throws IOException If the fxml file cannot be loaded.
     */
    public void switchToStartView(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/startView.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/css/startView.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
        Logger.trace("Switching from the result view to the start view");
    }

    /**
     * Loads the best results from the previous games.
     */
    private void loadResults() {
        try {
            gameResultRepository.loadFromFile(new File("results.json"));
        } catch (IOException e) {
            Logger.warn("The results could not be loaded!");
        }
    }
}
