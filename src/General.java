import java.util.concurrent.ThreadLocalRandom;

public class General {
    public static void main(String[] args) {
        System.out.println(hash(1000000));
        System.out.println(hash(999999));
        for (int i=0;i<10;i++) {
            System.out.println(hash(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE)));
        }
    }

    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
}
