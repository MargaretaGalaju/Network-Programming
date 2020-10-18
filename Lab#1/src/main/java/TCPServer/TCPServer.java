package TCPServer;

import java.io.*;
import java.net.ServerSocket;

public class TCPServer {
    public MultiTCPServerThread thread;

    public TCPServer(int port) {
        ServerSocket server;
        boolean listening = true;

        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            System.out.println("Waiting for a client ...");

            while (listening) {
                thread = new MultiTCPServerThread(server.accept());
                thread.start();
            }

            server.close();
        } catch (IOException i) {
            thread.interrupt();
            Thread.currentThread().interrupt();
            System.out.println(i);
        }
    }
}

