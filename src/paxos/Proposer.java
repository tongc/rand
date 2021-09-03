package paxos;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

public class Proposer {
    private final Coordinator coordinator;
    private final Server server;
    private final ConcurrentMap<Float, Integer> versionConsensusCount = new ConcurrentHashMap<>();
    private volatile String msgToSend = null;

    public Proposer(Coordinator coordinator, Server server) {
        this.coordinator = coordinator;
        this.server = server;
    }

    public synchronized void propose(float currentVersion, float serverId) {
        //propose stage
        float newVersion = currentVersion + 1 + serverId;
        coordinator.getRandomServers().forEach((version, server) -> {
            Proposal accept = server.promise(newVersion);
            if (accept != null && accept.getVersion() == newVersion) {
                versionConsensusCount.computeIfPresent(version, (id1, count) -> count + 1);
                versionConsensusCount.putIfAbsent(version, 1);
                if(accept.getMsg()!=null) {
                    msgToSend = accept.getMsg();
                }
            }
        });

        //accept stage
        if(versionConsensusCount.size() > coordinator.getServers().size()/2) {
            System.out.println("====================server " + server.getName() + ":" + server.getId() + " reached majority vote on version " + newVersion + " and will move to next stage=========================");
            coordinator.getServers().forEach((id, server) -> {
//                System.out.println("server " + server.getName() + ":" + server.getId() + " send final msg to " + id);
                server.accept(new Proposal(newVersion, msgToSend==null?this.server.getId() + "msg":msgToSend));
            });
        } else {
            try {
                //sleep random time before propose again, random sleep to break potential livelock
                Thread.sleep(ThreadLocalRandom.current().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(serverId + "-----------------------............proposing again........---------------------");
            versionConsensusCount.clear();
            propose(currentVersion + 1, serverId);
        }
    }

}
