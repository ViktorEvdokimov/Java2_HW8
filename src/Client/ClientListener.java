package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClientListener extends AbstractAction implements KeyListener {
ChatWindow chatWindow;

    public ClientListener(ChatWindow chatWindow) {
        this.chatWindow = chatWindow;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==10) {
            chatWindow.sendMessage();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        chatWindow.sendMessage();
    }
}


