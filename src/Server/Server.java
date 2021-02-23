package Server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private int port;
    private final ServerSocket serverSocket;
    private final AuthService authService;
    private final Set<ClientHandler> clients;

    public Server(int port) {
        try {
            this.port = port;
            serverSocket = new ServerSocket(port);
            authService = new AuthService();
            clients = new HashSet<ClientHandler>();
            while (true){
                System.out.println("Waiting for new client");
                Socket socket = serverSocket.accept();
                System.out.println("Client "+ socket+ " connect.");
                ClientHandler clientHandler = new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW when up a server", e);
        }

    }

    public synchronized void broadcast (String message){
        for (ClientHandler client : clients){
            client.sendMessage(message);
        }
    }

    public synchronized boolean privateMessage (String name, String message){
        for (ClientHandler client : clients){
            if (client.getName().equals(name)) {
                client.sendMessage(message);
                return true;
            }
        }
        return false;
    }

    public synchronized void subscribe (ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    public synchronized void unsubscribe (ClientHandler clientHandler){
        clients.remove(clientHandler);
    }

    public synchronized boolean isNickFree(String name){
        for (ClientHandler client : clients){
            if (client.getName().equals(name)) return false;
        }
        return true;
    }

    public AuthService getAuthService() {
        return authService;
    }
}
