package paxos.distributed

import org.testng.annotations.Test
import kotlin.test.assertTrue

class CoordinatorTest {
    private var testObject: Coordinator? = Coordinator(999F, 1111, "127.0.0.1")

    @Test
    fun shouldSerialize() {
        testObject!!.register()
        assertTrue { testObject!!.randomServers.size >= 0 }
        println(testObject!!.randomServers.size)
    }
}
