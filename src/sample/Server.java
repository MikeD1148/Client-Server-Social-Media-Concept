package sample;

import org.json.simple.*;  // required for JSON encoding and decoding
import java.net.*;
import java.io.*;
import java.util.*;        // required for List and Scanner
import org.json.simple.*;  // required for JSON encoding and decoding

public class Server {

    static class Clock {
        private long t;

        public Clock() { t = 0; }

        // tick the clock and return the current time
        public synchronized long tick() { return ++t; }
    }


    static class ClientHandler extends Thread {
        // shared message board
        private static List<Message> board = new ArrayList<Message>();

        // shared logical clock
        private static Clock clock = new Clock();

        // number of messages that were read by this client already
        private int read;

        // login name; null if not set
        private String login;

        private Socket client;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) throws IOException {
            client = socket;
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            read = 0;
            login = null;
        }

        public void run() {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {



                    // tick the clock and record the current time stamp
                    long ts = clock.tick();

                    // logging request (+ login if possible) to server console
                    if (login != null)
                        System.out.println(login + ": " + inputLine);
                    else
                        System.out.println(inputLine);

                    // parse request, then try to deserialize JSON
                    Object json = JSONValue.parse(inputLine);
                    Request req;

                    // login request? Must not be logged in already
                    if (login == null &&
                            (req = LoginRequest.fromJSON(json)) != null) {
                        // set login name
                        login = ((LoginRequest)req).getName();
                        // response acknowledging the login request
                        out.println(new SuccessResponse());
                        continue;
                    }

                    // post request? Must be logged in
                    if (login != null && (req = PostRequest.fromJSON(json)) != null) {

                        String message = ((PostRequest)req).getMessage();
                        String[] splitArray = message.split(",");

                        String filepath = splitArray[splitArray.length - 1];
                        String savedMessage = "";

                        for(int i=0;i < splitArray.length - 1;i++){

                            savedMessage += splitArray[i];
                        }


                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            // add message with login and timestamp
                            board.add(new Message(savedMessage, login, ts));

                        }
                        try{
                            FileWriter fw = new FileWriter(filepath,true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter pw = new PrintWriter(bw);

                            pw.println(login + ": "+ savedMessage + " (" + ts + ")");
                            pw.flush();
                            pw.close();


                        }catch (Exception e){

                        }


                        // response acknowledging the post request
                        out.println(new SuccessResponse());
                        continue;
                    }

                    // read request? Must be logged in
                    if (login != null && ReadRequest.fromJSON(json) != null) {
                        List<Message> msgs;
                        // synchronized access to the shared message board
                        synchronized (ClientHandler.class) {
                            msgs = board.subList(read, board.size());
                        }
                        // adjust read counter
                        read = board.size();
                        // response: list of unread messages
                        out.println(new MessageListResponse(msgs));
                        continue;
                    }

                    // quit request? Must be logged in; no response
                    if (login != null && QuitRequest.fromJSON(json) != null) {
                        in.close();
                        out.close();
                        return;
                    }

                    // error response acknowledging illegal request
                    out.println(new ErrorResponse("ILLEGAL REQUEST"));
                }
            } catch (IOException e) {
                System.out.println("Exception while connected");
                System.out.println(e.getMessage());
            }
        }
    }


    public static void main(String[] args) {


        int portNumber = 12345;

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("Exception listening for connection on port " +
                    portNumber);
            System.out.println(e.getMessage());
        }
    }



}
