package paxos.distributed

import paxos.Config.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Inet4Address
import java.net.ServerSocket
import java.net.Socket


/**
 * Generic server responsible for sending and receiving messages over TCP
 */
abstract class Server(val id: Float, private val port: Int, private val ip: String = Inet4Address.getLocalHost().hostAddress) {
    private var serverSocket: ServerSocket? = null
    private var out: PrintWriter? = null
    private var input: BufferedReader? = null
    private var type: Class<Server> = Server::class.java
    protected var coordinatorAdd: String = "127.0.0.1"
    protected var coordinatorPort: Int = 8888

    companion object {
        val MSG_PREFIX: String = "@S"
    }

    protected abstract fun process(input: String?): String?

    init {
        this.type = this.javaClass
    }

    fun start(port: Int) {
        Thread {
            try {
                serverSocket = ServerSocket(port)
                while (true) {
                    val socket = serverSocket!!.accept()
                    val output = socket.getOutputStream()
                    out = PrintWriter(output, true)
                    input = BufferedReader(InputStreamReader(socket.getInputStream()))
                    try {
                        var inputLine: String
                        while (input!!.readLine().also { inputLine = it } != null) {
                            println("received: $inputLine")
                            processInput(inputLine)
                        }
                    } catch (e: Exception) {
                        System.err.println(e.message)
                    }
                }
            } catch (e: IOException) {
                System.err.println("fatal " + e.message)
            }
        }.start()
    }

    private fun processInput(input: String) {
        val output = process(input)
        output?.let { write(it) }
    }

    protected fun write(output: String?) {
        out!!.write(output!!)
    }

    protected fun sendMsg(ip: String, port: Int, msg: String): String? {
        val clientSocket = Socket(ip, port)
        clientSocket.soTimeout = 500

        out = PrintWriter(clientSocket.getOutputStream(), true)
        input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        out!!.write(msg)
        try {
            processInput(input!!.readText())
        } catch (e: Exception) {
            System.err.println(e.message)
        }
        out!!.close()
        input!!.close()
        return null
    }

    public fun serialize(server: Server): String {
        return MSG_PREFIX + MSG_START + server.id + DELIMETER + server.type.simpleName + DELIMETER + server.port + DELIMETER + server.ip + MSG_END
    }

    public fun deserialize(msg: String): Server? {
        if (msg.startsWith(MSG_PREFIX)) {
            val msgBody = msg.replaceFirst(MSG_PREFIX + MSG_START, "")
                .replace(MSG_END, "")
            val serverIdType = msgBody.split(DELIMETER).toTypedArray()
            when (serverIdType[1]) {
                "Proposer" -> return Proposer(serverIdType[0].toFloat(), serverIdType[2].toInt(), serverIdType[3])
                "Acceptor" -> return Acceptor(serverIdType[0].toFloat(), serverIdType[2].toInt(), serverIdType[3])
                "Coordinator" -> return Coordinator(serverIdType[0].toFloat(), serverIdType[2].toInt(), serverIdType[3])
            }
        }
        System.err.println("invalid input $msg")
        return null
    }

    public fun register() {
        sendMsg(coordinatorAdd, coordinatorPort, serialize(this))
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Server

        if (id != other.id) return false
        if (serverSocket != other.serverSocket) return false
        if (out != other.out) return false
        if (input != other.input) return false
        if (type != other.type) return false
        if (coordinatorAdd != other.coordinatorAdd) return false
        if (coordinatorPort != other.coordinatorPort) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (serverSocket?.hashCode() ?: 0)
        result = 31 * result + (out?.hashCode() ?: 0)
        result = 31 * result + (input?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        result = 31 * result + coordinatorAdd.hashCode()
        result = 31 * result + coordinatorPort
        return result
    }

    override fun toString(): String {
        return "Server(id=$id, port=$port, ip='$ip', serverSocket=$serverSocket, out=$out, input=$input, type=$type, coordinatorAdd='$coordinatorAdd', coordinatorPort=$coordinatorPort)"
    }
}