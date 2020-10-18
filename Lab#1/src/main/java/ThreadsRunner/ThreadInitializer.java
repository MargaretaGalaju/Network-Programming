package ThreadsRunner;


import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadInitializer{
    public static ThreadPoolExecutor threadPool  = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);
    public static void executeThread(LinkedList<String> queue) {
        for (String stringEntry : queue) {
            ThreadsRunner threadsRunner = new ThreadsRunner(stringEntry);
            threadPool.execute(threadsRunner);
        }

    }

}
