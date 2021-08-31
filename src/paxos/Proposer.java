package paxos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

public class Proposer {
    private final Coordinator coordinator;
    private final Server server;
    private final ConcurrentMap<Float, Integer> versionConsensusCount = new ConcurrentHashMap<>();

    public Proposer(Coordinator coordinator, Server server) {
        this.coordinator = coordinator;
        this.server = server;
    }

    public synchronized void propose(float currentVersion, float serverId) {
        float newVersion = currentVersion + 1 + serverId;
        coordinator.getRandomServers().forEach((id, server) -> {
            Proposal accept = server.accept(new Proposal(newVersion, null));
            if (accept != null && accept.getVersion() == newVersion) {
                versionConsensusCount.computeIfPresent(id, (id1, count) -> count + 1);
                versionConsensusCount.putIfAbsent(id, 1);
            }
        });
        if(versionConsensusCount.size() > coordinator.getServers().size()/2) {
            System.out.println("====================server " + server.getName() + ":" + server.getId() + " reached majority vote on version " + newVersion + " and will move to next stage=========================");
            coordinator.getServers().forEach((id, server) -> {
//                System.out.println("server " + server.getName() + ":" + server.getId() + " send final msg to " + id);
                server.accept(new Proposal(newVersion, this.server.getId() + "msg"));
            });
        } else {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(serverId + "-----------------------............proposing again........---------------------");
            versionConsensusCount.clear();
            propose(currentVersion + 1, serverId);
        }
    }

}
