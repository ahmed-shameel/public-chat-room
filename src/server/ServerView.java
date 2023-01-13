package server;
import javax.swing.*;

public class ServerView extends JFrame {
    private final JTextArea textArea;

    public ServerView(){
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane);
        setSize(400, 500);
        setTitle("Chat server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addInfo(String message){
        textArea.append(message + "\n");
    }
}
