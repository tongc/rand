import kotlin.Pair;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {

    public static final int THRESHOLD = 10000;
    public static final int INDEX_SPARSE_LEVEL = 100;
    public static final int INDEX_SPARSE_PREFIX_SIZE = 3;

    class BasicDb {
        private Map<String, Pair<Integer, Integer>> index;
        private Path dbFile;
        FileChannel fileChannelO;
        FileChannel fileChannelI;
        RandomAccessFile randomAccessFile;

        public BasicDb() {
            index = new HashMap<>();
            try {
                dbFile = Files.createTempFile("temp", ".db");
                fileChannelI = new FileInputStream(dbFile.toFile()).getChannel();
                fileChannelO = new FileOutputStream(dbFile.toFile()).getChannel();
                randomAccessFile = new RandomAccessFile(dbFile.toFile(), "r");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Pair<Integer, Integer> addElement(String key, String value) {
            try {
                long position = fileChannelO.position();
                {//need to be in one transaction, or use anti-antropy to verify and fix.
                    fileChannelO.write(ByteBuffer.wrap((key + value).getBytes(StandardCharsets.UTF_8)));
                    //dangerous cast
                    index.put(key, new Pair<>((int) position + key.length(), value.length()));
                    return new Pair(fileChannelO.position() + key.length(), + value.length());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getElement(String key) {
            try {
                byte[] bb = new byte[index.get(key).getSecond()];
                randomAccessFile.seek(index.get(key).getFirst());
                randomAccessFile.read(bb, 0, index.get(key).getSecond());
                return new String(bb, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String searchValue(String value) {
            return index.entrySet().stream().filter(eachEntry ->
                    value.equals(getElement(eachEntry.getKey()))).findFirst().get().getKey();
        }
    }

    class SSTable {
        private List<BasicDb> segments = new ArrayList<>();
        private Map<String, Map<Integer, Pair<Integer, Integer>>> index;
        //keys to be saved into file periodically
        private TreeMap<String, String> sortedEntries = new TreeMap<>();

        public String getElement(String key) {
            return null;
        }

        public void addElement(String key, String value) {
            sortedEntries.put(key, value);
            if (sortedEntries.size() > THRESHOLD) {
                flush();
            }
        }

        private void flush() {
            BasicDb basicDb = new BasicDb();
            segments.add(basicDb);
            AtomicInteger counter = new AtomicInteger();
            sortedEntries.forEach((key, value) -> {
                Pair<Integer, Integer> posAndSize = basicDb.addElement(key, value);
                //this is tricky
                String sparseKey = key.substring(0, INDEX_SPARSE_PREFIX_SIZE);
                if (posAndSize.getFirst() % INDEX_SPARSE_LEVEL == 0) {
                    if (index.containsKey(key)) {
                        index.get(key).put(segments.size(), posAndSize);
                    } else {
                        index.put(key, new HashMap<>() {{
                            put(segments.size(), posAndSize);
                        }});
                    }
                }
                counter.incrementAndGet();
            });
        }

        private void compactAndMerge() {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        BasicDb basicDb = new Database().new BasicDb();
        basicDb.addElement("a", "bbbbb");
        basicDb.addElement("a1", "aaddd");
        basicDb.addElement("a2", "cccc");
        basicDb.addElement("a2", "cccca");
        System.out.println(basicDb.getElement("a2"));
        System.out.println(basicDb.getElement("a1"));
        System.out.println(basicDb.searchValue("cccca"));

        SSTable ssTable = new Database().new SSTable();

        System.out.println("bbc".compareTo("bbd"));
        System.out.println("bbc".compareTo("aaa"));

        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1, 1);
        treeMap.put(4, 4);
        treeMap.put(6, 6);
        treeMap.put(8, 8);
        treeMap.put(9, 9);
        treeMap.put(12, 12);
        treeMap.put(50, 50);

        System.out.println(treeMap.ceilingKey(5));
        System.out.println(treeMap.floorKey(5));
    }
}
