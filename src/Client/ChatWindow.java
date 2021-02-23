package Client;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ChatWindow extends JFrame {
    private final StringBuilder sb;
    private final JTextField inputField;
    private final JTextArea textArea;
    private Consumer<String> consumer;


    public ChatWindow(Consumer<String> consumer) {
        this.consumer = consumer;
        sb = new StringBuilder();
        setTitle("Chat window");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100,100,400,500);
        setVisible(true);

        JPanel chatArea = new JPanel();
        chatArea.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        chatArea.add(textArea, BorderLayout.CENTER);

        JPanel inputArea = new JPanel();
        inputArea.setLayout(new BorderLayout());
        inputField = new JTextField();
        inputField.addKeyListener(new ClientListener(this));
        inputArea.add(inputField, BorderLayout.CENTER);
        JButton enter = new JButton("ENTER");
        enter.addActionListener(new ClientListener(this));
        inputArea.add(enter, BorderLayout.LINE_END);

        add(textArea, BorderLayout.CENTER);
        add(inputArea, BorderLayout.SOUTH);


    }

    public void append (String message){
        if (!message.isBlank()) {
            sb.append(message);
            sb.append("\n");
            textArea.setText(sb.toString());
        }
    }

    public void sendMessage (){
            consumer.accept(inputField.getText());
            inputField.setText("");
        }
    }

