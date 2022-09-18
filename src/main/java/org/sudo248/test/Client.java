package org.sudo248.test;

import org.sudo248.handshake.server.ServerHandshake;
import org.sudo248.client.WebSocketClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class Client extends WebSocketClient {
    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("new connection opened");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        System.err.println("an error occurred:" + ex);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        WebSocketClient client = new Client(new URI("ws://localhost:8887"));
        client.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = "";

        while (!str.equals("stop")) {
            System.out.println("client says: " + str);
            str = br.readLine();
            client.send(str);
        }
    }
}
