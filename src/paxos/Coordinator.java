package paxos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Coordinator {
    //magical global state
    private final ConcurrentMap<Float, Server> servers = new ConcurrentHashMap<>();
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

    public Map<Float, Server> getServers() {
        return servers;
    }

    /**
     * simulate server not reachable
     */
    public Map<Float, Server> getRandomServers() {
        return servers.entrySet().stream()
                .filter(floatServerEntry -> ThreadLocalRandom.current().nextInt(9) > 3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static void main(String[] args) throws InterruptedException {
        Coordinator coordinator = new Coordinator();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();
        new Thread(() -> new Server(coordinator, Thread.currentThread().getName())).start();

        Thread.sleep(3000);
        coordinator.getServers().entrySet().forEach(new Consumer<Map.Entry<Float, Server>>() {
            @Override
            public void accept(Map.Entry<Float, Server> entry) {
                System.out.println("server " + entry.getKey() + " has value " + entry.getValue().getConsensusVal() + " has version " + entry.getValue().getAcceptor().getCurrentVersion());
            }
        });
        while (true) ;
    }
}
