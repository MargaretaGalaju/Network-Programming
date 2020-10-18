package ThreadsRunner;

import HttpRequestor.HttpRequestor;

import Models.ResultResponse;
import Models.RoutesResponse;

import java.util.*;

public class ThreadsRunner implements Runnable {
    public  LinkedList<String> queue = new LinkedList<>();
    public HttpRequestor httpRequestor = new HttpRequestor();
    private String token = httpRequestor.parseRegisterResponse().getAccess_token();
    private String route;

    public ThreadsRunner(String route) {
        this.route = route;
    }

    @Override
    synchronized public void run() {
        System.out.println("Current Thread: " + Thread.currentThread().getName());
        System.out.println("Route: " + route);

        RoutesResponse routesResponse = httpRequestor.getRequest(route, token).getBody().as(RoutesResponse.class);

        if (Objects.nonNull(routesResponse.getLink())) {
            routesResponse.getLink().forEach((k, v) -> queue.add(v));
            ThreadInitializer.executeThread(queue);
        }

        ResultResponse.mapData.put( routesResponse.getData(), routesResponse.getMime_type() == null ? "application/json" : routesResponse.getMime_type());
    }
}
