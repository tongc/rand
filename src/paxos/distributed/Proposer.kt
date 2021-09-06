package paxos.distributed

import java.net.Inet4Address
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ThreadLocalRandom

class Proposer(id: Float, port: Int, ip: String) : Acceptor(id, port, ip) {
    private val versionConsensusCount: ConcurrentMap<Float, Int> = ConcurrentHashMap()

    @Volatile
    private var msgToSend: String? = null
    fun propose(currentVersion: Float) {}

    init {

    }

    companion object {
        private const val PROP_PORT = 11111
        private val instance = Proposer(ThreadLocalRandom.current().nextFloat(), PROP_PORT, Inet4Address.getLocalHost().hostAddress)

        @JvmStatic
        fun main(args: Array<String>) {
            instance.start(PROP_PORT)
            Thread.sleep(3000)
            instance.register()
            while(true) {}
        }
    }
}