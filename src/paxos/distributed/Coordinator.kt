package paxos.distributed

import java.net.Inet4Address
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ThreadLocalRandom

class Coordinator(id: Float, port: Int, ip:String) : Server(id, port, ip) {
    private val servers: ConcurrentMap<Float, Server>

    init {
        servers = ConcurrentHashMap()
    }

    fun getServers(): Map<Float, Server> {
        return servers
    }

    /**
     * simulate server not reachable
     */
    val randomServers: Map<Float, Server>
        get() = servers.filter { ThreadLocalRandom.current().nextInt(9) > 3 }

    override fun process(input: String?): String? {
        if(input!!.startsWith(Server.MSG_PREFIX, false)) {
            val server = deserialize(input)
            servers[server!!.id] = server
        }
        return null
    }

    companion object {
        private const val COOR_PORT = 8888
        private val instance = Coordinator(ThreadLocalRandom.current().nextFloat(), COOR_PORT, Inet4Address.getLocalHost().hostAddress)

        @JvmStatic
        fun main(args: Array<String>) {
            instance.start(COOR_PORT)
            while(true) {}
        }
    }
}