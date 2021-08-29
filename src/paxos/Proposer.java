package paxos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Proposer {
    private final Coordinator coordinator;
    private final Server server;
    private ConcurrentMap<Float, Integer> versionConsensusCount = new ConcurrentHashMap<>();

    public Proposer(Coordinator coordinator, Server server) {
        this.coordinator = coordinator;
        this.server = server;
    }

    public void propose(float currentVersion) {
        coordinator.getServers().forEach((id, server) -> {
            Proposal accept = server.accept(new Proposal(currentVersion, null));
            if (accept != null && accept.getVersion() == currentVersion) {
                versionConsensusCount.computeIfPresent(id, (id1, count) -> count + 1);
                versionConsensusCount.putIfAbsent(id, 1);
            }
        });
        if(versionConsensusCount.size() > coordinator.servers.size()/2) {
            System.out.println("server " + server.getName() + ":" + server.getId() + " reached consensus on version " + currentVersion + " and will move to next stage");
            coordinator.getServers().forEach((id, server) -> {
                server.accept(new Proposal(currentVersion, this.server.getId() + "msg"));
            });
        }
    }
}
