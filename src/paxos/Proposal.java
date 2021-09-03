package paxos;

import java.util.concurrent.atomic.AtomicInteger;

public class Proposal {
    private final float version;
    private final String msg;

    public Proposal(float version, String msg) {
        this.version = version;
        this.msg = msg;
    }

    public Proposal(float version) {
        this.version = version;
        this.msg = null;
    }

    public float getVersion() {
        return version;
    }

    public String getMsg() {
        return msg;
    }
}
