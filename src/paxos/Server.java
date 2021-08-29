package paxos;

public class Server {
    private final Acceptor acceptor;
    private final Proposer proposer;
    private boolean isLeader = false;
    private boolean isFollower = false;
    private final Coordinator coordinator;
    private final float id;

    public Server(Coordinator coordinator) {
        this.coordinator = coordinator;
        this.id = coordinator.register(this);
        acceptor = new Acceptor(coordinator, this);
        proposer = new Proposer(coordinator, this);
        init();
    }

    public Proposal accept(Proposal proposal) {
        return acceptor.receive(proposal);
    }

    private void init() {
        if(!isLeader && !isFollower) {
            System.out.println("neither a leader nor a follower so start proposing");
            proposer.propose(acceptor.getCurrentVersion() + 1 + id);
        }
    }

    public float getId() {
        return id;
    }
}
