package paxos.distributed

import org.testng.annotations.Test
import kotlin.test.assertTrue

class CoordinatorTest {
    private var testObject: Coordinator? = Coordinator(999F)

    @Test
    fun shouldSerialize() {
        testObject!!.register(Acceptor(888F))
        testObject!!.register(Acceptor(888.1F))
        testObject!!.register(Acceptor(888.2F))
        testObject!!.register(Acceptor(888.3F))
        testObject!!.register(Acceptor(888.4F))
        assertTrue { testObject!!.randomServers.size >= 0 }
        println(testObject!!.randomServers.size)
    }
}
