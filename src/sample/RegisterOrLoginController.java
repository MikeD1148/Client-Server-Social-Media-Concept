package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.sql.*;

public class RegisterOrLoginController {

    @FXML
    private TextField Username;

    @FXML
    private Label Message;

    @FXML
    private Button Login;

    @FXML
    private PasswordField Password;

    public void RegisterButtonOnAction (ActionEvent event){
        Connection conn = DatabaseConnection();

        try {

            String user = Username.getText();
            String pass = Password.getText();

            String sql = "INSERT INTO Users (Username, Password) VALUES(?,?)";

            PreparedStatement update = conn.prepareStatement(sql);
            update.setString(1, user);
            update.setString(2, pass);

            //execution of insert string
            update.executeUpdate();
            Message.setText("You made an account!");

        } catch (SQLException e) {
            Message.setText("Failed to make account!");
            System.out.println(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Message.setText("Failed to make account!");
            }
        }


    }

    public void ButtonLoginOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) Login.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
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
