package clientView;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Register extends JFrame {
    private final JTextField usernameIn;
    private final JPasswordField passwordIn;
    private final JPasswordField cPassword;
    private final JButton registerButton;
    private final JButton loginButton;

    public Register(){
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        JLabel cPasswordLabel = new JLabel("Confirm password: ");

        usernameIn = new JTextField();
        passwordIn = new JPasswordField();
        cPassword = new JPasswordField();
        loginButton = new JButton("Back to login");
        registerButton = new JButton("Register");

        setLayout(null);

        usernameLabel.setBounds(20, 20, (int) usernameLabel.getPreferredSize().getWidth(),
                (int) usernameLabel.getPreferredSize().getHeight());
        passwordLabel.setBounds(20, 50, (int) passwordLabel.getPreferredSize().getWidth(),
                (int) passwordLabel.getPreferredSize().getHeight());
        cPasswordLabel.setBounds(20, 80, (int) cPasswordLabel.getPreferredSize().getWidth(),
                (int) cPasswordLabel.getPreferredSize().getHeight());

        usernameIn.setBounds(180, 20, 200, 20);
        passwordIn.setBounds(180, 50, 200, 20);
        cPassword.setBounds(180, 80, 200, 20);

        registerButton.setBounds(300, 120, 80, 30);
        loginButton.setBounds(180, 120, 120, 30);

        add(usernameLabel);
        add(passwordLabel);
        add(cPasswordLabel);
        add(usernameIn);
        add(passwordIn);
        add(cPassword);
        add(registerButton);
        add(loginButton);

        setResizable(false);
        setSize(400,220);
        setTitle("Register");
        getRootPane().setDefaultButton(registerButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addListenerToRegister(ActionListener register){
        registerButton.addActionListener(register);
    }
    public void addListenerToLogin(ActionListener login){
        loginButton.addActionListener(login);
    }

    public String getUsername(){
        return usernameIn.getText();
    }

    public String getPassword(){
        return passwordIn.getText();
    }

    public String getCPassword(){
        return cPassword.getText();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message);
    }
}
