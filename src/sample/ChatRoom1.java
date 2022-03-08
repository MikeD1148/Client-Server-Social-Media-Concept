package sample;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONValue;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import static sample.LoginController.DBuser;

public class ChatRoom1 {

    @FXML
    private Button send;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField Chat;

    @FXML
    private Button refresh;

    @FXML
    private Button returns;

    private static Scanner x;
    public static String filepath = "PlacementHelp.csv";

    public static String hostName = "localhost";
    public static int portNumber = 12345;

    public static Socket socket;
    public static PrintWriter out;
    public static BufferedReader in;

    static {
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    public void refreshOnAction(){
        Chat.setText("read");
    }

    public void deleteOnAction(ActionEvent event){
        textArea.setText("");
    }

    public void returnOnAction(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            Stage stage = (Stage) returns.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void refreshOnAction(ActionEvent event){
        String saved;
        try{
            x = new Scanner(new File(filepath));
            x.useDelimiter("[\n]");

            while(x.hasNext())
            {

                saved = x.next();
                textArea.appendText(saved +  "\n");


            }
            x.close();

        }catch (Exception e){

        }
    }

    public void sendOnAction(ActionEvent event){

        try
        {
            String userInput = Chat.getText();
            while ((userInput = Chat.getText()) != null) {
                // Parse user and build request
                Request req;
                Scanner sc = new Scanner(userInput);
                Chat.setText(null);
                try {
                    switch (sc.next()) {
                        case "login":
                            req = new LoginRequest(DBuser);
                            break;
                        case "post":
                            req = new PostRequest(sc.skip(" ").nextLine() + "," + filepath);
                            break;
                        case "read":
                            req = new ReadRequest();
                            break;
                        case "quit":
                            req = new QuitRequest();
                            break;
                        default:
                            System.out.println("ILLEGAL COMMAND");
                            continue;
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("ILLEGAL COMMAND");
                    continue;
                }

                // Send request to server
                out.println(req);

                // Read server response; terminate if null (i.e. server quit)
                String serverResponse;
                if ((serverResponse = in.readLine()) == null)
                    break;

                // Parse JSON response, then try to deserialize JSON
                Object json = JSONValue.parse(serverResponse);
                Response resp;

                // Try to deserialize a success response
                if (SuccessResponse.fromJSON(json) != null) {
                    //display.appendText(String.valueOf(json) + "\n");
                    continue;
                }

                // Try to deserialize a list of messages
                if ((resp = MessageListResponse.fromJSON(json)) != null) {
                    for (Message m : ((MessageListResponse)resp).getMessages()) {
                        System.out.println(m);
                        textArea.appendText(String.valueOf(m) + "\n");


                    }
                    continue;
                }

                //Try to deserialize an error response
                if ((resp = ErrorResponse.fromJSON(json)) != null) {
                    System.out.println(((ErrorResponse)resp).getError());
                    continue;
                }

                // Not any known response
                System.out.println("PANIC: " + serverResponse +
                        " parsed as " + json);
                break;
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

}




