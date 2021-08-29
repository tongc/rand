package paxos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Coordinator {
    //magical global state
    public final ConcurrentMap<Float, Server> servers = new ConcurrentHashMap<>();
    private final AtomicInteger nextServerId = new AtomicInteger(1);

    public float register(Server server) {
        if (server.getId() == 0) {
            //support up to 100 servers
            float id = ((float)nextServerId.getAndIncrement())/100;
            servers.put(id, server);
            System.out.println("registered server " + id);
            return id;
        } else {
            if (!servers.containsKey(server.getId())) {
                servers.put(server.getId(), server);
            }
            return server.getId();
        }
    }

    public ConcurrentMap<Float, Server> getServers() {
        return servers;
    }

    public static void main(String[] args) {
        Coordinator coordinator = new Coordinator();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();

        while (true) ;
    }
}
