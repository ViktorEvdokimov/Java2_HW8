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
    boolean authorisationFailed = false;
    Thread t1;

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


    }

    private void work (){
        try {
            sendMessage("Hello, for login start your message \"-auth\" then send login and password");
            authorizationLimit();
            authorization();
            waitingMessage();
        } catch (IOException e) {
            closeConnection();
            throw new RuntimeException("SWW when client handler work");
        } finally {
            closeConnection();
        }
    }

    private void waitingMessage () throws IOException {
        while (true){
            String message = in.readUTF();
            if (message.equals("/end")){
                server.broadcast(name + "logout");
                return;
            } else if (message.startsWith("/w")){
                privateMessage(message);
            } else if (!message.isBlank()) server.broadcast(name + ": " +message);
        }
    }

    private void authorizationLimit(){
        t1 = new Thread  (()->
        {
            try {
                Thread.sleep(120000);
                authorisationFailed = true;
                sendMessage("Time to connecting lost");
            } catch (InterruptedException e) {
                throw new RuntimeException("SWW when authorization Limit", e);
            }
        });
        t1.start();
    }

    private void privateMessage (String message) {
        String[] parts = message.split("\\s");
        if (parts.length >= 3) {
            StringBuilder sb = new StringBuilder(name);
            sb.append(" whisper you: ");
            for (int i = 2; i < parts.length; i++) {
                sb.append(parts[i]);
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1);
            if (server.privateMessage(parts[1], sb.toString())) sendMessage(parts[1] + " get your message.");
            else sendMessage("Receiver logout or incorrect message.");
        }
    }

    private void closeConnection(){
        server.unsubscribe(this);
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
//            if (message.equals("/end")) {
//                closeConnection();
//            }
            String[] parts = message.split("\\s");
            if (parts[0].equals("-auth")){
                name = server.getAuthService().getNickByLoginAndPass(parts[1],parts[2]);
                if (name!=null && parts.length==3){
                    if (authorisationFailed) {
                        sendMessage("Time to connecting lost");
                        closeConnection();
                        return;
                    } else if (server.isNickFree(name)) {
                        sendMessage("Login successful");
                        server.broadcast(name + " login");
                        server.subscribe(this);
                        authorisationFailed = false;
                        t1.stop();
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
