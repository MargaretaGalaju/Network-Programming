package TCPServer;

import Models.Person;
import Models.ResultResponse;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;

public class MultiTCPServerThread extends Thread {
    private Socket socket = null;
    private DataInputStream input;
    private DataOutputStream output;

    public MultiTCPServerThread(Socket socket) {
        super("MultiServerThread");
        this.socket = socket;
    }

    public void run() {
        while (true) {
            System.out.println("New client accepted");

            try {
                input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String clientMessage = "";
            while (!clientMessage.equals("exit")) {
                try {
                    clientMessage = input.readUTF();
                    String[] splitted = clientMessage.split(" ");

                    if(splitted[0].equals("SelectColumn")) {
                        String columnName = splitted[1];
                        ArrayList responseList = getDataByColumnName(columnName);

                        responseList.forEach((response) -> {
                            System.out.println(response);
                        });
                    } else {
                        System.out.println("Invalid command" + clientMessage);
                    }
                } catch(IOException i) {
                    System.out.println(i);
                }
            }

            System.out.println("Closing connection");

            try {
                socket.close();
                input.close();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList getDataByColumnName(String s) {
        try {
            ArrayList fieldValues = new ArrayList<String>();
            for (Object property : ResultResponse.personArrayList) {
                Field field = Person.class.getField(s);
                Object fieldValue = field.get(property);
                fieldValues.add(fieldValue);
            }
            return fieldValues;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
