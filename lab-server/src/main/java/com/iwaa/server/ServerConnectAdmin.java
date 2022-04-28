package com.iwaa.server;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.Request;
import com.iwaa.common.network.Serializer;
import com.iwaa.common.state.State;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class ServerConnectAdmin implements Runnable {
    private static final int STANDARD_BUFFER_SIZE = 1024;
    private static final int SELECT_DELAY = 1000;
    private final Map<SocketChannel, ByteBuffer> channels = new HashMap<>();

    private final Selector selector;
    private final ServerSocketChannel serverChannel;

    public ServerConnectAdmin() throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        while (true) {
            try {
                serverChannel.socket().bind(new InetSocketAddress(inputPort()));
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Port must be in the range from 0 to 65535");
            }
        }
        System.out.println("Server using port - " + serverChannel.socket().getLocalPort());
    }

    private int inputPort() {
        while (true) {
            try {
                System.out.println("Enter port:");
                Scanner sc = new Scanner(System.in);
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter number");
            }
        }
    }

    public void run() {
        try {
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            listen();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            close();
        }
    }

    private void close() {
        try {
            selector.close();
            serverChannel.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listen() throws IOException {
        while (State.getPerformanceStatus()) {
            SelectionKey key = null;
            try {
                int numberOfKeys = selector.select(SELECT_DELAY);
                if (numberOfKeys != 0) {
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        key = keys.next();
                        keys.remove();
                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                accept();
                            } else if (key.isReadable()) {
                                read(key);
                            } else if (key.isWritable()) {
                                write(key);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                if (key != null) {
                    kill((SocketChannel) key.channel());
                }
            }
        }
    }

    private void accept() throws IOException {
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        channels.put(channel, ByteBuffer.allocate(0));
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(STANDARD_BUFFER_SIZE);
        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            kill(channel);
            return;
        }
        ByteBuffer newBuffer = ByteBuffer.allocate(channels.get(channel).capacity() + bytesRead);
        newBuffer.put(channels.get(channel).array());
        newBuffer.put(ByteBuffer.wrap(buffer.array(), 0, bytesRead));
        channels.put(channel, newBuffer);

        Request request = (Request) Serializer.deserialize(channels.get(channel).array());

        if (request != null) {
            channels.put(channel, ByteBuffer.wrap(Serializer.serialize(
                    CommandAdmin.executeCommand(request.getCommand(), request.getArgs()))));
            channel.register(selector, SelectionKey.OP_WRITE);
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = channels.get(channel);
        channel.write(buffer);
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
        channels.put(channel, ByteBuffer.allocate(0));
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void kill(SocketChannel channel) throws IOException {
        channels.remove(channel);
        channel.close();
    }
}
