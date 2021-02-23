package Client;

import java.util.function.Consumer;

public class ClientAdapter {
    private final ClientNetwork network;
    private final ChatWindow window;

    public ClientAdapter(String host, int port) {
        this.network = new ClientNetwork(host, port);
        this.window = new ChatWindow(new Consumer<String>() {
            @Override
            public void accept(String message) {
                network.sendMessage(message);
            }
        });
        receive();
    }
    private void receive (){
        new Thread(()-> {
            while (true){
                String message = network.receive();
                if (!message.isBlank()) {
                    window.append(message);
                }
            }
        }).start();
    }
}
