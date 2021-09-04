package paxos.distributed

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class Proposer(private val coordinator: Coordinator, id: Float) : Acceptor(id) {
    private val versionConsensusCount: ConcurrentMap<Float, Int> = ConcurrentHashMap()

    @Volatile
    private var msgToSend: String? = null
    fun propose(currentVersion: Float) {}
}