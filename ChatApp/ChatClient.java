import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private BufferedReader in;
    private PrintWriter out;

    public ChatClient() {
        JFrame frame = new JFrame("Chat Client");
        JTextArea messageArea = new JTextArea(20, 50);
        messageArea.setEditable(false);
        JTextField inputField = new JTextField(40);
        JButton sendButton = new JButton("Send");

        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try {
            Socket socket = new Socket("localhost", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to connect to server.");
            System.exit(0);
        }

        sendButton.addActionListener(e -> {
            out.println(inputField.getText());
            inputField.setText("");
        });

        inputField.addActionListener(e -> {
            out.println(inputField.getText());
            inputField.setText("");
        });

        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    messageArea.append(msg + "\n");
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        }).start();
    }

    public static void main(String[] args) {
        new ChatClient();
    }
}
