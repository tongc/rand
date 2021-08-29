package gfs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChunkServer {
    //The capacity of Chunk Server. In real life this number * Global.CHUNK_SIZE close to the size of the HDD/SSD.
    private static final int NUM_OF_CHUNKS = 100;
    //File to Chunk is 1:M, it could be 1:1
    Map<String, List<Integer>> fileToChunks = new HashMap<>();

    private static final int PORT = 8889;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws IOException {
        ChunkServer cs = new ChunkServer();
        cs.run();
    }

    private void run() throws IOException {
        serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (".".equals(inputLine)) {
                    break;
                }
                processInput(inputLine, socket);
            }
        }
    }

    public boolean check() {
        return true;
    }

    public void processInput(String input, Socket socket) throws IOException {
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(new Date() + ": " + input);
    }

    class Chunk {
        Character[] data = new Character[Global.CHUNK_SIZE];
    }
}
