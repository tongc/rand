package paxos.distributed

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ThreadLocalRandom

class Coordinator(id: Float) : Server(id) {
    private val servers: ConcurrentMap<Float, Server> = ConcurrentHashMap()
    fun register(server: Server?) {
        servers[server?.id] = server
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
        return null
    }

    companion object {
        private const val COOR_PORT = 8888
        private val instance = Coordinator(ThreadLocalRandom.current().nextFloat())

        @JvmStatic
        fun main(args: Array<String>) {
            instance.start(COOR_PORT)
        }
    }
}