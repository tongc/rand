package paxos;

public class Acceptor {
    private volatile float currentVersion = 0.0F;
    private final Coordinator coordinator;
    private final Server server;

    public Acceptor(Coordinator coordinator, Server server) {
        this.coordinator = coordinator;
        this.server = server;
    }

    public synchronized Proposal receive(Proposal proposal) {
        if(proposal.getMsg() != null) {
            if (proposal.getVersion() >= currentVersion) {
                System.out.println("server " + server.getName() + ":" + server.getId() + " received final value " + proposal.getMsg());
                server.setConsensusVal(proposal.getMsg());
                return proposal;
            } else {
                return null;
            }
        } else {
            if (proposal.getVersion() > currentVersion) {
                System.out.println("server " + server.getName() + ":" + server.getId() + " received a newer version to use " + proposal.getVersion() + " compare to current version " + currentVersion);
                currentVersion = proposal.getVersion();
                return proposal;
            } else {
                return null;
            }
        }
    }

    public float getCurrentVersion() {
        return currentVersion;
    }
}
