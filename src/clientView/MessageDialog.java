package clientView;

import javax.swing.*;

public class MessageDialog extends JFrame {

    public void showErrorDialog(String message){
        JOptionPane.showMessageDialog(new JFrame(), message);
    }

}
