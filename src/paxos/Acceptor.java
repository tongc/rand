package paxos;

public class Acceptor {
    private volatile Float currentVersion = 0.0F;
    private final Coordinator coordinator;
    private final Server server;
    private volatile Proposal acceptedProposal;

    public Acceptor(Coordinator coordinator, Server server) {
        this.coordinator = coordinator;
        this.server = server;
    }


    public synchronized Proposal promise(Float version) {
        if (version > currentVersion) {
            System.out.println("server " + server.getName() + ":" + server.getId() + " received version " + version);
            currentVersion = version;
            if (acceptedProposal != null) {
                System.out.println("taking previously promised message " + acceptedProposal.getMsg());
                return acceptedProposal;
            } else {
                return new Proposal(version);
            }
        } else {
            return null;
        }
    }

    public synchronized Proposal accept(Proposal proposal) {
        if (proposal.getVersion() >= currentVersion) {
            System.out.println("server " + server.getName() + ":" + server.getId() + " received final value " + proposal.getMsg());
            currentVersion = proposal.getVersion();
            server.setConsensusVal(proposal.getMsg());
            return proposal;
        } else {
            return null;
        }
    }

    public float getCurrentVersion() {
        return currentVersion;
    }
}
