package paxos;

public class Server {
    private final Acceptor acceptor;
    private final Proposer proposer;
    private volatile boolean isLeader = false;
    private volatile boolean isFollower = false;
    private final Coordinator coordinator;
    private final float id;
    private final String name;
    private volatile String consensusVal;

    public Server(Coordinator coordinator, String name) {
        this.coordinator = coordinator;
        this.id = coordinator.register(this);
        this.name = name;
        acceptor = new Acceptor(coordinator, this);
        proposer = new Proposer(coordinator, this);
        propose();
    }

    public Proposal promise(Float version) {
        return acceptor.promise(version);
    }

    public Proposal accept(Proposal proposal) {
        return acceptor.accept(proposal);
    }

    private void propose() {
        if(!isLeader && !isFollower) {
            System.out.println("neither a leader nor a follower so start proposing");
            proposer.propose(acceptor.getCurrentVersion(), id);
        }
    }

    public float getId() {
        return id;
    }

    public void setConsensusVal(String consensusVal) {
        this.consensusVal = consensusVal;
    }

    public String getName() {
        return name;
    }

    public Acceptor getAcceptor() {
        return acceptor;
    }

    public String getConsensusVal() {
        return consensusVal;
    }
}
