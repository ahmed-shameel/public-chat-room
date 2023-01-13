package Client;

import clientView.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import java.sql.PreparedStatement;

public class Client {

    //GUI
    private static final Login loginView = new Login();
    private static final Register registerView = new Register();
    private static final Home homeView = new Home();
    private static final MessageDialog messageDialog = new MessageDialog();

    //CONNECTION
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;



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

    private static void login(){
        if(loginView.getUsername().isBlank() || loginView.getPassword().isEmpty()){
            messageDialog.showErrorDialog("Empty Password/Username");
        } else{
            String username = loginView.getUsername();
            String password = encryptPassword(loginView.getPassword());

            try {
                Statement stm = connection.createStatement();
                String sql = "select * from users where username='"+ username +"' and  password='" + password + "'";
                ResultSet rs = stm.executeQuery(sql);

                if(rs.next()){
                    loggedInUser = new User(username, password);
                    setConnection(loginView.getHost(), loginView.getPort());
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
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.ISO_8859_1), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            Thread thread = new Thread(() -> {
                while (true){
                    listenData();
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
            String msg = in.readLine();
            while (msg != null){
                homeView.addMessage(msg);
                msg = in.readLine();
            }
            messageDialog.showErrorDialog("The server has closed!");
            out.close();
            clientSocket.close();
            homeView.dispose();

        } catch (IOException ex) {
            System.err.println("ERROR: error listening data" + ex);
        }
    }


    private static void initiateDataFromDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement command = connection.createStatement();

            //Show old massages
            ResultSet messageData = command.executeQuery("SELECT * FROM messages");
            while (messageData.next()){
                String message = messageData.getString("user") +
                        ": " +messageData.getString("message");
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

    private static void sendMessage(){
        if(!homeView.getMessage().isBlank()){
            Thread sender = new Thread(new Runnable() {
                final String msg = loggedInUser.getUsername() + ": " + homeView.getMessage();

                @Override
                public void run() {
                    out.println(msg);
                    out.flush();
                    postToDB(msg);
                }
            });
            sender.start();
        }
    }

    private static void postToDB(String msg) {
        String[] split = msg.split(":");
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages VALUES(?,?)");
            statement.setString(1, split[1].trim());
            statement.setString(2, split[0]);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
                        //Encrypt password before storing in DB
                        password = encryptPassword(password);
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

    //MD5 Hashing Technique
    private static String encryptPassword(String password){
        String encryptedpassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            encryptedpassword = s.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedpassword;
    }

}