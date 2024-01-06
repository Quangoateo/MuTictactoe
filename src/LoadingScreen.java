import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


//Small class to handle initial start up page and take input
class LoadingScreen {
    String serverAddress = "null";
    JTextField textField = new JTextField(10);
    JOptionPane optionPane;
    JFrame dialog = new JFrame("TICTACTOE VGU");
    Object[] arr = {"Enter server address ", textField};

    public LoadingScreen() {
        optionPane = new JOptionPane(arr);
        optionPane.setBackground(Color.red);
        dialog.setSize(500,500);
        dialog.setResizable(false);
        dialog.setVisible(true);
        dialog.add(optionPane);
        dialog.pack();
    }
}
