package paxos.distributed

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

/**
 * Generic server responsible for sending and receiving messages over TCP
 */
abstract class Server(val id: Float) {
    private var serverSocket: ServerSocket? = null
    private var out: PrintWriter? = null
    private var input: BufferedReader? = null
    protected abstract fun process(input: String?):String?
    fun start(port: Int) {
        Thread {
            try {
                serverSocket = ServerSocket(port)
                while (true) {
                    val socket = serverSocket!!.accept()
                    val output = socket.getOutputStream()
                    out = PrintWriter(output, true)
                    input = BufferedReader(InputStreamReader(socket.getInputStream()))
                    var inputLine: String
                    while (input!!.readLine().also { inputLine = it } != null) {
                        processInput(inputLine)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun processInput(input: String) {
        val output = process(input)
        output?.let { write(it) }
    }

    private fun write(output: String?) {
        out!!.write(output)
    }
}