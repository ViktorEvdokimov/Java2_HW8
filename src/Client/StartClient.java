package Client;

public class StartClient {
    public static void main(String[] args) {
//        ChatWindow chatWindow = new ChatWindow();
//        ClientNetwork clientNetwork = new ClientNetwork("127.0.0.1", 8888);
        ClientAdapter clientAdapter = new ClientAdapter("127.0.0.1", 8888);
    }
}
