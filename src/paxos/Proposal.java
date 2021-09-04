package paxos;

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
        return MSG_PREFIX + "{" + version + "," + msg + "}";
    }

    public Proposal deserialize(String msg) {
        if (msg.startsWith(MSG_PREFIX)) {
            String msgBody = msg.replaceFirst(MSG_PREFIX + "\\{", "").replace("\\}", "");
            String[] versionMsg = msgBody.split(",");
            if (versionMsg.length == 1) {
                return new Proposal(Float.parseFloat(versionMsg[0]));
            } else if (versionMsg.length == 2) {
                return new Proposal(Float.parseFloat(versionMsg[0]), versionMsg[1]);
            }
        }
        System.err.println("invalid input " + msg);
        return null;
    }
}
