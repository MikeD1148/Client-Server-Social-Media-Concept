Welcome to Tweeter the hallam chat sharing social media!

This application uses a database to store user's logins and csv file to store user's conversations.

Run the server by clicking run server.

The server will compile and run using a localhost with an address of 12345.

Run the client by clicking run main.

The client will open a java FXML file to display the GUI.

RegisterOrLoginController will display the RegisterOrLogin FXML file and prompts the user to either login or register and account in the database.

LoginController displays the Login FXML file and allows the user to login if they have an account in the database.

LobbyController displays the Lobby FXML file and allows the user to choose a chat room to talk in.

ChatRoom1 allows the user to chat with other users about the Placement help.

ChatRoom2 allows the user to chat with other users about the Study help.

ErrorResponse will inforn the user if they make a mistake whilst chatting with other users.

SuccessResponse will inforn the user if their message was successful.

PostRequest uses json to serialise the message and send it to the server.

ReadRequest will serialise a read request and send it to the server.

QuitRequest will handle a quit request caused by the user.

I did implement the persistence chat saving the chat records to a csv file.
