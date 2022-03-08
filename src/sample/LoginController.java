package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.sql.*;

public class LoginController {

    public static String DBuser = "";

    @FXML
    private Button LoginButton;

    @FXML
    private PasswordField Password;

    @FXML
    private Button returnButton;

    @FXML
    private Label incorrect;

    @FXML
    private TextField Username;

    public void returnButtonOnAction(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("RegisterOrLogin.fxml"));
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void loginButtonOnAction (ActionEvent event) {
        Connection conn = DatabaseConnection();

        String username = Username.getText();
        String password = Password.getText();

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE Username=(?) AND Password=(?)")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            String DBPassword = "";

            while (rs.next()) {
                DBuser = rs.getString("Username");

                DBPassword = rs.getString("Password");
            }

            if (username.equals(DBuser) && password.equals(DBPassword)) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
                    Stage stage = (Stage) LoginButton.getScene().getWindow();
                    stage.setScene(new Scene(root, 600, 400));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();
                }
            } else {
                incorrect.setText("You have failed to login!");

            }
        } catch (SQLException e) {
            System.out.println("Fail!");
        }
    }

    private static Connection DatabaseConnection(){

        String fileName = "SocialMedia.db";
        String url = "jdbc:sqlite:" + fileName;
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
