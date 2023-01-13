package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private boolean alive;
    private final Socket socket;
    private final ArrayList<ClientHandler> threads;
    private PrintWriter outPut;
    private final ServerView serverView;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> threads, ServerView serverView){
        alive = true;
        this.socket = socket;
        this.threads = threads;
        this.serverView = serverView;
    }

    public void kill(){
        alive = false;
    }

    @Override
    public void run() {
        while(alive){
            try{
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outPut = new PrintWriter(socket.getOutputStream(), true);
                InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
                String ipAdress = socketAddress.getAddress().getHostAddress();
                serverView.addInfo(ipAdress + " is now logged in!");
                String msg;
                while (true) {
                    msg = input.readLine();
                    if (msg == null) {
                        break;
                    }
                    printToClients(msg);
                    serverView.addInfo(msg);
                }

                serverView.addInfo(ipAdress + " has left the chat");
                input.close();
                outPut.close();
                socket.close();
                kill();
                break;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printToClients(String output){
        for(ClientHandler s : threads){
            s.outPut.println(output);
        }
    }
}
