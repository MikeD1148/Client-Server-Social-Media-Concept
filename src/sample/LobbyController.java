package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LobbyController {

    @FXML
    private Button PlacementHelp;

    @FXML
    private Button StudyHelp;

    public void PlacementHelpOnAction(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ChatRoom1.fxml"));
            Stage stage = (Stage) PlacementHelp.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void StudyHelpOnAction(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ChatRoom2.fxml"));
            Stage stage = (Stage) StudyHelp.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

}
