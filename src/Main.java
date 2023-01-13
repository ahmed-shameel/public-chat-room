import Gui.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;

import java.sql.PreparedStatement;

public class Main {
    //GUI
    private static final Login loginView = new Login();
    private static final Register registerView = new Register();
    private static final Home homeView = new Home();
    private static final MessageDialog messageDialog = new MessageDialog();

    //Client
    private static boolean running = true;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 2000;

    private static Socket clientSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static DataInputStream inData;
    private static DataOutputStream outData;
    //Database
    private static final String USERNAME = "usr_21952981";
    private static final String PASSWORD = "952981";
    private static final String URL = "jdbc:mysql://atlas.dsv.su.se/db_21952981";
    private static Connection connection;
    private static User loggedInUser;

    public static void main(String[] args) {

        //Upload data from database
        initiateDataFromDB();

        //Initiate GUI
        initiateGui();

        //Login
        loginView.addListenerToLogin(e -> {
            login();
        });

        //Register new user
        registerView.addListenerToRegister(e -> {
            register();
        });

        //Send message
        homeView.addListenerToSend(e -> {
         sendMessage();
        });
    }

    private static void initiateDataFromDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement command = connection.createStatement();

            //Show old massages
            ResultSet messageData = command.executeQuery("SELECT * FROM messages");
            while (messageData.next()){
                Message message = new Message(messageData.getString("user"),
                        messageData.getString("message"));
                homeView.addMessage(message);
            }
            //Show users
            ResultSet usersData = command.executeQuery("SELECT * FROM users");
            while (usersData.next()){
                homeView.setUsers(usersData.getString("username"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private static void initiateGui(){
        loginView.setVisible(true);

        loginView.addListenerToRegister(e -> {
            registerView.setLocation(loginView.getLocation());
            loginView.dispose();
            registerView.setVisible(true);
        });

        registerView.addListenerToLogin(e -> {
            loginView.setLocation(registerView.getLocation());
            registerView.dispose();
            loginView.setVisible(true);
        });
    }

    private static void sendMessage(){
        try{
            if(!homeView.getMessage().isBlank()){
                Message msg = new Message(loggedInUser.getUsername(), homeView.getMessage());

                outputStream = clientSocket.getOutputStream();
                outData = new DataOutputStream(outputStream);
                outData.writeUTF(msg.toString());
                outData.flush();

                homeView.addMessage(msg);
                postToDB(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void postToDB(Message msg) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages VALUES(?,?)");
            statement.setString(1, msg.getMessage());
            statement.setString(2, msg.getUser());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void login(){
        if(loginView.getUsername().isBlank() || loginView.getPassword().isEmpty()){
            messageDialog.showErrorDialog("Empty Password/Username");
        } else{
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            try {
                Statement stm = connection.createStatement();
                String sql = "select * from users where username='"+ username +"' and  password='" + password + "'";
                ResultSet rs = stm.executeQuery(sql);

                if(rs.next()){
                    loggedInUser = new User(username, password);
                    setConnection(DEFAULT_HOST, DEFAULT_PORT);
                    homeView.setLocationRelativeTo(null);
                    loginView.setVisible(false);
                    homeView.setVisible(true);
                }else {
                    messageDialog.showErrorDialog("Wrong username or password");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void setConnection(String ip, int port) {
        try{
            clientSocket = new Socket(ip, port);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running){
                        listenData();
                    }
                }
            });
            thread.start();
            // check connection
            System.out.println("successfully connected to " + ip + ":" + port);
        } catch (IOException e) {
            System.out.println("ERROR: connection error" + e);
        }
    }

    private static void listenData() {
        try {
            inputStream = clientSocket.getInputStream();
            inData = new DataInputStream(inputStream);
            Message message = new Message(loggedInUser.getUsername(), inData.readUTF());
            homeView.addMessage(message);
        } catch (IOException ex) {
            System.err.println("ERROR: error listening data" + ex);
        }
    }

    private static void register(){
        if(registerView.getUsername().isBlank() || registerView.getPassword().isEmpty()){
            messageDialog.showErrorDialog("Please fill in all fields!");
        } else{
            String username = registerView.getUsername();
            String password = registerView.getPassword();
            String cPassword = registerView.getCPassword();

            try {
                Statement stm = connection.createStatement();
                String sql = "select * from users where username='"+ username +"'";
                ResultSet rs = stm.executeQuery(sql);

                if(rs.next()){
                    registerView.showError("Username already exist. Login instead or choose another username!");
                }else {
                    if(!password.equals(cPassword)){
                        registerView.showError("Passwords don't match!");
                    }else {
                        PreparedStatement statement = connection.prepareStatement("INSERT INTO users VALUES(?,?)");
                        statement.setString(1, username);
                        statement.setString(2, password);
                        statement.executeUpdate();
                        initiateGui();
                        registerView.dispose();
                        messageDialog.showErrorDialog("Account was created successfully!");
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}