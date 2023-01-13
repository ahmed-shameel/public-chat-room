package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {
    private static final int DEFAULT_PORT = 2000;
    public static void main(String[] args) {

        ServerView serverView = new ServerView();
        serverView.setVisible(true);

        ArrayList<ClientHandler> threads = new ArrayList<>();
        ServerSocket serverSocket;

        try{
            if(args.length > 0){
                serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            } else {
                serverSocket = new ServerSocket(DEFAULT_PORT);
            }
            while (true){
                Socket socket = serverSocket.accept();
                ClientHandler serverThread = new ClientHandler(socket, threads, serverView);
                threads.add(serverThread);
                serverThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error in main: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
