package gfs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkServer {
    //The capacity of Chunk Server. In real life this number * Global.CHUNK_SIZE close to the size of the HDD/SSD.
    private static final int NUM_OF_CHUNKS = 100;
    //File to Chunk is 1:M, it could be 1:1
    Map<String, List<Integer>> fileToChunks = new HashMap<>();

    public static void main(String[] args) {
        ChunkServer cs = new ChunkServer();
        cs.run();
    }

    private void run() {
        while(true) {

        }
    }

    public boolean check() {
        return true;
    }

    class Chunk {
        Character[] data = new Character[Global.CHUNK_SIZE];
    }
}
