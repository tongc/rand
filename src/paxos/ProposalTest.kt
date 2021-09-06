package paxos

import org.testng.annotations.Test
import kotlin.test.assertEquals

class ProposalTest {
    private var testObject: Proposal? = null

    @Test
    fun shouldSerialize() {
        testObject = Proposal(1.1f, "testmsg")
        assertEquals(Proposal.MSG_PREFIX + "{1.1,testmsg}", testObject!!.serialize())
        assertEquals(testObject, testObject!!.deserialize(testObject!!.serialize()));
    }
}