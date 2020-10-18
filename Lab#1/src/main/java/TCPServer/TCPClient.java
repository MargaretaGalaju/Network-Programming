package TCPServer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TCPClient {
    private Socket socket            = null;
    private DataInputStream  inputStream   = null;
    private DataInputStream  clientInput   = null;
    private DataOutputStream out     = null;

    public TCPClient(int port) throws UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();

        try {
            socket = new Socket(host.getHostName(), port);
            System.out.println("Connected");

            clientInput  = new DataInputStream(System.in);
            inputStream  = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch(UnknownHostException u) {
            System.out.println(u);
        } catch(IOException i) {
            System.out.println(i);
        }

        String clientInputMessage = "";
        while (!clientInputMessage.equals("exit")) {
            try {
                clientInputMessage = clientInput.readLine();
                out.writeUTF(clientInputMessage);
            } catch(IOException i) {
                System.out.println(i);
            }
        }

        String serverResponse = "";
        while (!serverResponse.equals("exit")) {
            try {
                serverResponse = inputStream.readUTF();
                System.out.println(serverResponse);
            } catch(IOException i) {
                System.out.println(i);
            }
        }

        try {
            inputStream.close();
            clientInput.close();
            out.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        TCPClient client = new TCPClient( 4000);
    }
}
