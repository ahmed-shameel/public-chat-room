package Gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private final JTextField username;
    private final JTextField password;
    private final JButton loginButton;
    private final JButton registerButton;


    public Login(){
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");

        username = new JTextField();
        password = new JTextField();
        registerButton = new JButton("Register");
        loginButton = new JButton("Login");

        setLayout(null);

        usernameLabel.setBounds(20, 20, (int) usernameLabel.getPreferredSize().getWidth(),
                (int) usernameLabel.getPreferredSize().getHeight());
        passwordLabel.setBounds(20, 50, (int) passwordLabel.getPreferredSize().getWidth(),
                (int) passwordLabel.getPreferredSize().getHeight());

        username.setBounds(180, 20, 200, 20);
        password.setBounds(180, 50, 200, 20);

        loginButton.setBounds(300, 100, 80, 30);
        registerButton.setBounds(220, 100, 80, 30);


        add(usernameLabel);
        add(passwordLabel);
        add(username);
        add(password);
        add(loginButton);
        add(registerButton);

        setResizable(false);
        setSize(400,200);
        setTitle("Login");
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(loginButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addListenerToRegister(ActionListener register){
        registerButton.addActionListener(register);
    }

    public void addListenerToLogin(ActionListener login){
        loginButton.addActionListener(login);
    }

    public String getUsername(){
        return username.getText();
    }

    public String getPassword(){
        return password.getText();
    }
}
