package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String name;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread (this::work).start();
        } catch (IOException e) {
            throw new RuntimeException("SWW when create client handler.");
        }
        sendMessage("Hello, for login start your message \"-auth\" then send login and password");

    }

    private void work (){
        try {
            authorization();
            waitingMessage();
        } catch (IOException e) {
            throw new RuntimeException("SWW when client handler work");
        } finally {
            closeConnection();
        }
    }

    private void waitingMessage () throws IOException {
        while (true){
            String message = in.readUTF();
            if (message.equals("/end")){
                return;
            } else if (message.startsWith("/w")){
                privateMessage(message);
            } else if (!message.isBlank()) server.broadcast(name + ": " +message);
        }
    }

    private void privateMessage (String message) {
        String[] parts = message.split("\\s");
        if (parts.length > 3) {
            StringBuilder sb = new StringBuilder(name);
            sb.append(" whisper you: ");
            for (int i = 2; i < parts.length; i++) {
                sb.append(parts[i]);
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1);
            if (server.privateMessage(parts[1], sb.toString())) sendMessage(parts[1] + "get your message.");
            else sendMessage("Receiver logout or incorrect message.");
        }
    }

    private void closeConnection(){
        server.unsubscribe(this);
        server.broadcast(name + "logout");
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("SWW when close connection.");
        }

    }

    private  void authorization() throws IOException {
        while (true){
            String message = in.readUTF();
            String[] parts = message.split("\\s");
            if (parts[0].equals("-auth")){
                name = server.getAuthService().getNickByLoginAndPass(parts[1],parts[2]);
                if (name!=null && parts.length==3){
                    if (server.isNickFree(name)) {
                        sendMessage("Login successful");
                        server.broadcast(name + " login");
                        server.subscribe(this);
                        return;
                    } else sendMessage("This login busy");
                } else sendMessage("Incorrect message length, login of password.");
            } else sendMessage("To continue you should login. Please start your message \"-auth\"");
        }
    }

    public void sendMessage (String message){
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException("SWW when send message.", e);
        }
    }

    public String getName() {
        return name;
    }
}
