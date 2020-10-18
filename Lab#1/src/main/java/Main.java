import Deserializer.DataConverter;
import HttpRequestor.HttpRequestor;
import Models.ResultResponse;
import TCPServer.TCPServer;
import ThreadsRunner.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, InstantiationException, IOException {
        HttpRequestor httpRequestor = new HttpRequestor();
        LinkedList<String> queue = new LinkedList<>();

        httpRequestor.parseHomeResponse().getLink().forEach((k, v) -> queue.add(v));
        ThreadInitializer.executeThread(queue);

        try {
            if (!ThreadInitializer.threadPool.awaitTermination(20, TimeUnit.SECONDS)) {
                ThreadInitializer.threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            ThreadInitializer.threadPool.shutdown();
            Thread.currentThread().interrupt();
        }

        if (ResultResponse.mapData != null) {
            for (Map.Entry<String, String> entry : ResultResponse.mapData.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                if (Objects.nonNull(v)) {
                    DataConverter.convertToSpecificFormat(k, v);
                }
            }
        }

        TCPServer server = new TCPServer(4000);
    }
}
