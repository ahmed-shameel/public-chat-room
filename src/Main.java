import Gui.*;

import java.sql.*;

import java.sql.PreparedStatement;

public class Main {

    private static final Login loginView = new Login();
    private static final Register registerView = new Register();
    private static final Home homeView = new Home();
    private static final MessageDialog messageDialog = new MessageDialog();

    private static final String USERNAME = "usr_21952981";
    private static final String PASSWORD = "952981";
    private static final String URL = "jdbc:mysql://atlas.dsv.su.se/db_21952981";
    private static Connection connection;
    private static User loggedInUser;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement command = connection.createStatement();

            //Show old massages
            ResultSet messageData = command.executeQuery("SELECT * FROM messages");
            while (messageData.next()){
                Message message = new Message(messageData.getString("user"),
                        messageData.getString("message"));
                homeView.setMessages(message);
            }
            //Show users
            ResultSet usersData = command.executeQuery("SELECT * FROM users");
            while (usersData.next()){
                homeView.setUsers(usersData.getString("username"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

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

    private static void sendMessage(){
        try{
            if(!homeView.getMessage().isBlank()){
                Message message = new Message(loggedInUser.getUsername(), homeView.getMessage());
                PreparedStatement statement = connection.prepareStatement("INSERT INTO messages VALUES(?,?)");
                statement.setString(1, message.getMessage());
                statement.setString(2, message.getUser());
                statement.executeUpdate();
                homeView.setMessages(message);
            }
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