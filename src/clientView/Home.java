package clientView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Home extends JFrame{
    private JScrollPane chatPane;
    private JTextArea chatArea;
    private JScrollPane usersPane;
    private TextArea usersArea;
    private JTextField messageIn;
    private JButton sendButton;
    private JLabel chatroom;
    private JLabel logins;

    public Home(){
        setLayout(null);
        chatArea = new JTextArea();
        chatPane = new JScrollPane(chatArea);
        usersArea = new TextArea();
        usersPane = new JScrollPane(usersArea);
        messageIn = new JTextField();
        sendButton = new JButton("SEND");
        chatroom = new JLabel("Public chat");
        logins = new JLabel("Users");

        chatArea.setEditable(false);
        usersArea.setEditable(false);
        usersArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        chatPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        usersPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        usersPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        chatPane.setBounds(10, 30, 450, 570);
        usersPane.setBounds(470, 30, 220, 570);
        messageIn.setBounds(6, 620, 458, 40);
        sendButton.setBounds(470, 620, 220, 40);
        chatroom.setBounds(10, 10, 450, 20);
        logins.setBounds(470, 10, 220, 20);


        add(chatPane);
        add(usersPane);
        add(messageIn);
        add(sendButton);
        add(chatroom);
        add(logins);
        setResizable(false);
        setSize(700,700);
        setTitle("Login");
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(sendButton);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setUsers(String username) {
        usersArea.append(username + "\n");
    }

    public void addListenerToSend(ActionListener listener) {
        sendButton.addActionListener(listener);
    }

    public String getMessage() {
        return messageIn.getText();
    }

    public void addMessage(String message) {
        chatArea.append(message + "\n");
        messageIn.setText("");
    }
}
