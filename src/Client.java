import javax.swing.*;
import java.awt.event.*;
import java.net.Socket;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Client {
    String serverAddress;
    Socket socket;
    Scanner input;
    PrintWriter output;
    //frame of the game play
    JFrame frame = new JFrame("TICTACTOE VGU");
    //board of the tictactoe
    JPanel boardPanel = new JPanel(new GridLayout(6, 6));
    //Chat session
    JPanel chatPanel = new JPanel();
    JTextArea messageArea = new JTextArea(22,10); //where we read
    JTextField textField = new JTextField(10); //where we type
    //Frame of tictactoe
    JFrame frame1 = new JFrame("GUIDELINE GAME");
    JPanel buttonPanel = new JPanel(new GridLayout(2,2));
    JButton multiplayerPlay = new JButton("PVP");
    JButton soloPlay = new JButton("SOLO");
    JButton leaveButton = new JButton("Leave");
    JButton guide = new JButton("Guide ?");
    JButton quit = new JButton("Quit");
    private final Square[] board = new Square[36];
    private Square selectedSquare;


    public Client() {
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        textField.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        boardPanel.setBackground(Color.BLACK);

        //Create new Square and add Action listener add into array board
        for(int i = 0; i < board.length; i++) {

            final int j = i;
            board[i] = new Square(j);

            if(i < 6) {
                board[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        board[j].circle.setVisible(true);
                        boardPanel.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        board[j].circle.setVisible(false);

                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                        selectedSquare = board[j];
                        output.println("MOVE" + selectedSquare.id);
                    }
                });
            }

            boardPanel.add(board[i]);
        }

        frame.add(boardPanel, BorderLayout.CENTER);
        multiplayerPlay.addActionListener(e -> output.println("MESSAGE/multiplayerPlay"));
        soloPlay.addActionListener(e -> output.println("MESSAGE/soloPlay"));
        leaveButton.addActionListener(e -> output.println("MESSAGE/leave"));
        quit.addActionListener(e -> 
        { output.println("MESSAGE/quit");
                System.exit(0); // You can use a different exit code if needed
        });
        frame1.setSize(300,200);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //guide line buttons
        guide.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(frame1,"How to Play Tic-Tac-Toe:\n" +
                    "\n" +
                    "Draw a 3x3 grid.\n" +
                    "Decide who's \"X\" and who's \"O.\"\n" +
                    "Take turns placing your mark on an empty spot.\n" +
                    "Win by getting three marks in a row horizontally, vertically, or diagonally.\n" +
                    "If the grid is full and no one wins, it's a draw.\n" +
                    "Enjoy the simplicity and play again!\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "" ,"guide line by GWANG WOO 10421103",JOptionPane.INFORMATION_MESSAGE);
        });
        

        //display bttons
        buttonPanel.add(multiplayerPlay);
        buttonPanel.add(soloPlay);
        buttonPanel.add(leaveButton);
        buttonPanel.add(quit);
        buttonPanel.add(guide);
        
        chatPanel.add(new JScrollPane(messageArea));
        chatPanel.add(buttonPanel);
        chatPanel.add(textField);
        frame.add(chatPanel, BorderLayout.EAST);
        frame.pack();
        textField.addActionListener(e -> {
            output.println("MESSAGE" + textField.getText());
            textField.setText("");
        });
    }

    public boolean testSocket(String add) {
        try {
            //create  a stream socket and connects to the specified port number on the named host.
            socket = new Socket(add, 59001);
            //Set the server address to the provided address .
            this.serverAddress = add;

        } catch (IOException io) {
            //If an IOException occurs
            // catch the exception and return false
            return false;
        }

        return true;
    }
    //chpse a username dial
    private String getName() {
        return JOptionPane.showInputDialog(frame, "Choose a username:", "Username selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void run() throws IOException {
        try {
            int loc;
            int playerNum;

            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);

            while(input.hasNextLine()) {
                String line = input.nextLine();
                if(line.startsWith("SUBMIT-NAME")){
                    output.println(getName());
                } else if (line.startsWith("NAME-ACCEPTED")) {
                    textField.setEditable(true);
                } else if (line.startsWith("RESET")) {
                    resetBoard();
                    boardPanel.repaint();
                } else if(line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(8) + "\n");
                } else if (line.startsWith("VALID_MOVE")) {
                    playerNum = Integer.parseInt(line.substring(10,11));
                    loc = Integer.parseInt(line.substring(11));
                    board[loc].circle.player = playerNum;
                    boardPanel.repaint();
                } else if (line.startsWith("WINNER")) {
                    JOptionPane.showMessageDialog(frame, "Winner Winner Chicken Dinner");
                } else if (line.startsWith("DRAW")) {
                    JOptionPane.showMessageDialog(frame, "LAME DRAW");
                } else if (line.startsWith("LOSER")) {
                    JOptionPane.showMessageDialog(frame, "LOSER");
                }
            }

        } finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    //Set each player of circle in each square of the board to set to 0 to be drawn empty(WHITE)
    private void resetBoard() {
        for(int i = 6; i < board.length; i++) {
            board[i].circle.player = 0;
        }
    }

    //Square class holds circle class with override paintComp to draw Oval into specific locations
    static class Square extends JPanel {
        Circle circle;
        int id;

        public Square(int n) {
            id = n;
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            circle = new Circle(n);
            add(circle);
            if(!(n < 6)) {
                setBorder(BorderFactory.createLineBorder(Color.CYAN));
                setBackground(Color.ORANGE);
                circle.setVisible(true);
            } else {
                setBorder(BorderFactory.createLineBorder(Color.WHITE));
                setBackground(Color.WHITE);
                circle.setVisible(false);
                circle.player = -1;
            }
        }

        static class Circle extends JPanel {
            //X MAX 70 for 750 px width window
            final int x = 5;
            final int y = 5;
            int n;
            int player;

            Circle(int n) {
                this.n = n;
            }
            @Override
            //designing size of oval or squar
            public void paintComponent(Graphics g) {
                if(player == -1){
                    g.setColor(Color.CYAN);
                } else if(player == 2) {
                    g.setColor(Color.RED);
                } else if (player == 1){
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.drawOval(x, y, 50, 50);
                g.fillOval(x, y, 50, 50);
            }
        }

    }

    public static void main(String[] args) throws Exception {
        
        //declare client class
        Client client;
        //new loadingscreen class
        LoadingScreen loadingScreen = new LoadingScreen();
        //prompt loadingScreen dialog
        loadingScreen.dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Check serverAddress after entering .
        //Then dispose it .
        loadingScreen.textField.addActionListener(e -> {
            loadingScreen.serverAddress = loadingScreen.textField.getText();
            loadingScreen.textField.setText("");
            loadingScreen.dialog.dispose();
        });
        
        do {
            System.out.print("");
        }
        while (loadingScreen.serverAddress.equals("null"));
            client = new Client();
        //check ip address of this computer
        if(!client.testSocket(loadingScreen.serverAddress)) {
            System.out.println("Couldn't connect to server");
        } else {
            //you matched!!!!!
            client.frame.setSize(600, 480);
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setVisible(true);
            client.run();
        }

    }
}