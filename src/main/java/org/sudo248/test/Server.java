package org.sudo248.test;

import org.sudo248.WebSocket;
import org.sudo248.handshake.client.ClientHandshake;
import org.sudo248.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class Server extends WebSocketServer {

    public Server(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake handshake) {
        ws.send("Welcome to the server!");
        System.out.println("new connection to: " + ws.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket ws, int code, String reason, boolean remote) {
        System.out.println("closed " + ws.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket ws, String message) {
        System.out.println("received message from "	+ ws.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onMessage(WebSocket ws, ByteBuffer message) {
        System.out.println("received ByteBuffer from "	+ ws.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket ws, Exception ex) {
        System.err.println("an error occurred on connection " + ws.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 8887;

        WebSocketServer server = new Server(new InetSocketAddress(host, port));
        System.out.println("start server");
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.run();
            }
        }).start();

        System.out.println("start reader");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = "";

        while (!str.equals("stop")) {
            System.out.println("server says: " + str);
            str = br.readLine();
            server.broadcast(str);
        }
    }
}
