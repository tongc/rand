import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class General {
    public static void main(String[] args) {
        System.out.println(hash(1000000));
        System.out.println(hash(999999));
        for (int i=0;i<10;i++) {
            System.out.println(hash(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE)));
        }

        slidingWindow();
    }

    private static void slidingWindow() {
        int windowSize = 6;
        List<Integer> data = new ArrayList<>();
        for(int i=0;i<100;i++) {
            data.add(i);
        }
        List<Integer> result = new ArrayList<>();
        int firstWindow = 0;
        for(int i=0;i<windowSize;i++) {
            firstWindow += data.get(i);
        }
        result.add(firstWindow);
        for(int i=windowSize;i<data.size();i++) {
            int previousWindow = result.get(i-windowSize);
            int newWindow = previousWindow - data.get(i-windowSize) + data.get(i);
            result.add(newWindow);
        }
        System.out.println(result);
    }

    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
}
