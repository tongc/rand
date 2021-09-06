package paxos.distributed

import java.net.Inet4Address
import java.util.concurrent.ThreadLocalRandom

open class Acceptor(id: Float, port: Int, ip: String) : Server(id, port, ip) {
    override fun process(input: String?):String? {
        TODO("Not yet implemented")
    }

    companion object {
        private const val ACCPT_PORT = 21111
        private val instance = Proposer(ThreadLocalRandom.current().nextFloat(), ACCPT_PORT, Inet4Address.getLocalHost().hostAddress)

        @JvmStatic
        fun main(args: Array<String>) {
            instance.start(ACCPT_PORT)
            Thread.sleep(3000)
            instance.register()
            while(true) {}
        }
    }
}