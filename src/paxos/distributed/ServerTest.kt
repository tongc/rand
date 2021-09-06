package paxos.distributed

import org.testng.Assert.*
import org.testng.annotations.Test

class ServerTest {
    @Test
    fun shouldSerializeServer() {
        val server = Proposer(123.0F, 1111, "127.0.0.1")
        assertEquals(server.serialize(server), "@S{123.0,Proposer}")
        assertEquals(server.deserialize(server.serialize(server)), server)
    }
}