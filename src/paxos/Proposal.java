package paxos;

import java.util.Objects;

import static paxos.Config.*;

public class Proposal {
    public static final String MSG_PREFIX = "@P";
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

    public String serialize() {
        return MSG_PREFIX + MSG_START + version + DELIMETER  + msg + MSG_END;
    }

    public Proposal deserialize(String msg) {
        if (msg.startsWith(MSG_PREFIX)) {
            String msgBody = msg.replaceFirst(MSG_PREFIX + "\\" + MSG_START, "").replace(MSG_END, "");
            String[] versionMsg = msgBody.split(DELIMETER);
            if (versionMsg.length == 1) {
                return new Proposal(Float.parseFloat(versionMsg[0]));
            } else if (versionMsg.length == 2) {
                return new Proposal(Float.parseFloat(versionMsg[0]), versionMsg[1]);
            }
        }
        System.err.println("invalid input " + msg);
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposal proposal = (Proposal) o;
        return Float.compare(proposal.version, version) == 0 && Objects.equals(msg, proposal.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, msg);
    }

    @Override
    public String toString() {
        return "Proposal{" +
                "version=" + version +
                ", msg='" + msg + '\'' +
                '}';
    }
}
