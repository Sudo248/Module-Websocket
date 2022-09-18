package org.sudo248.server;

import org.sudo248.AbstractWebSocketListenerImpl;
import org.sudo248.WebSocket;
import org.sudo248.WebSocketImpl;
import org.sudo248.drafts.Draft;

import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class DefaultWebSocketServerFactory implements WebSocketServerFactory{
    @Override
    public WebSocketImpl createWebSocket(AbstractWebSocketListenerImpl webSocketListener, Draft draft) {
        return new WebSocketImpl(webSocketListener, draft);
    }

    @Override
    public WebSocketImpl createWebSocket(AbstractWebSocketListenerImpl webSocketListener, List<Draft> drafts) {
        return new WebSocketImpl(webSocketListener, drafts);
    }

    @Override
    public ByteChannel wrapChannel(SocketChannel channel, SelectionKey key) {
        return channel;
    }

    @Override
    public void close() {

    }
}