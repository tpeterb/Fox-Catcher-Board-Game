package gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * The controller class of the start view where the players' names can be given.
 */
public class StartViewController {

    @FXML
    private TextField playerOneTextField;

    @FXML
    private TextField playerTwoTextField;

    @FXML
    private Button startButton;

    /**
     * Switches to the main view after the {@code startButton} was hit.
     * @param actionEvent The {@code ActionEvent} that represents the event
     * when the {@code startButton} was fired. The method passes the players' names
     * to the {@code TableGameController} class.
     * @throws IOException If the fxml file cannot be loaded.
     */
    public void switchToMainView(ActionEvent actionEvent) throws IOException {
        String playerOneName = playerOneTextField.getText();
        String playerTwoName = playerTwoTextField.getText();
        if (playerOneName == "" || playerTwoName == "") {
            Logger.info("At least one player's name has not been given!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Missing information");
            alert.setHeaderText("Please fill in all the fields!");
            alert.setContentText("At least one player's name has not been given! Please enter both player's name!");
            alert.showAndWait();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainView.fxml"));
        Parent root = loader.load();
        TableGameController gameController = loader.getController();
        gameController.setPlayerNames(playerOneName, playerTwoName);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/css/mainView.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
        Logger.debug("Switching from the start view to the main view");
        Logger.info("Player 1's name is set to {}, and Player 2's name is set to {}",
                playerOneName, playerTwoName);
    }
}