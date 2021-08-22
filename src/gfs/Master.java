package gfs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Master {
    private static final int HEARTBEAT_INTERVAL_MS = 5000;
    private static final int PORT = 8888;
    private final Set<ChunkServer> chunkServers = new HashSet<>();
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws IOException {
        Master master = new Master();
        master.run();
    }

    public void run() throws IOException {
        new Thread(() -> {
            chunkServers.forEach(ChunkServer::check);
            try {
                Thread.sleep(HEARTBEAT_INTERVAL_MS);
            } catch (InterruptedException e) {
                System.out.println("error");
            }
        }).start();

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

    public void processInput(String input, Socket socket) throws IOException {
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(new Date().toString() + ": " + input);
    }
}
