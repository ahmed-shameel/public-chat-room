package clientView;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String DEFAULT_PORT = "2000";

    private final JTextField username;
    private final JPasswordField password;
    private final JTextField port;
    private final JTextField host;
    private final JButton loginButton;
    private final JButton registerButton;


    public Login(){
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        JLabel portLabel = new JLabel("Port: ");
        JLabel hostLabel = new JLabel("Host: ");

        username = new JTextField();
        password = new JPasswordField();
        port = new JTextField();
        port.setText(DEFAULT_PORT);
        host = new JTextField();
        host.setText(DEFAULT_HOST);


        registerButton = new JButton("Register");
        loginButton = new JButton("Login");

        setLayout(null);

        usernameLabel.setBounds(20, 20, (int) usernameLabel.getPreferredSize().getWidth(),
                (int) usernameLabel.getPreferredSize().getHeight());
        passwordLabel.setBounds(20, 50, (int) passwordLabel.getPreferredSize().getWidth(),
                (int) passwordLabel.getPreferredSize().getHeight());
        portLabel.setBounds(20, 80, (int) portLabel.getPreferredSize().getWidth(),
                (int) portLabel.getPreferredSize().getHeight());
        hostLabel.setBounds(20, 110, (int) hostLabel.getPreferredSize().getWidth(),
                (int) hostLabel.getPreferredSize().getHeight());


        username.setBounds(180, 20, 200, 20);
        password.setBounds(180, 50, 200, 20);
        port.setBounds(180, 80, 100, 20);
        host.setBounds(180, 110, 100, 20);

        loginButton.setBounds(300, 160, 80, 30);
        registerButton.setBounds(220, 160, 80, 30);


        add(usernameLabel);
        add(passwordLabel);
        add(hostLabel);
        add(portLabel);
        add(host);
        add(port);
        add(username);
        add(password);
        add(loginButton);
        add(registerButton);

        port.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }
            }
        });

        setResizable(false);
        setSize(400,260);
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

    public String getHost() {
        return host.getText();
    }

    public int getPort() {
        return Integer.parseInt(port.getText());
    }
}
