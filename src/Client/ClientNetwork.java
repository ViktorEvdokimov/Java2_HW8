package Client;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientNetwork {
    private final int port;
    private final String host;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public ClientNetwork(String host, int port) {
        this.port = port;
        this.host = host;
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        } finally {
            sendMessage("/end");
        }
    }

    public void sendMessage (String message){
        try {
            if (message.length()!=0) {
                out.writeUTF(message);
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW when send message", e);
        }
    }

    public String receive (){
        try {
                return in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("SWW when receive message", e);
        }
    }
}
