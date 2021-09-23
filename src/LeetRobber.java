import java.util.HashMap;
import java.util.Map;

public class LeetRobber {
    public static void main(String[] args) {
        int[] vals = new int[]{2,3,4,5,6,7,8};
        System.out.println(new LeetRobber().maxCash(vals, vals.length - 1));
        System.out.println(new LeetRobber().maxCashLoop(vals, 0));
        System.out.println(new LeetRobber().climbStairs(5));
        System.out.println(new LeetRobber().reverseString(new char[]{'a','b','c','d','e'}, 4));
        System.out.println(new LeetRobber().recursion(5));
    }

    private int maxCash(int[] nums, int index) {
        if(index < 0) {
            return 0;
        }
        return max(nums[index] + maxCash(nums, index - 2), maxCash(nums, index - 1));
    }

    private int maxCashLoop(int[] nums, int index) {
        int evenSum = 0;
        int oddSum = 0;
        for(int i=0,j=1;i<nums.length||j<nums.length;i+=2,j+=2) {
            evenSum += nums[i];
            if(j<nums.length) {
                oddSum += nums[j];
            }
        }
        return Math.max(evenSum, oddSum);
    }

    private int max(int a, int b) {
        if(a>b) {
            return a;
        } else {
            return b;
        }
    }

    static Map<Integer, Integer> cache = new HashMap<>();
    public int climbStairs(int n) {
        if(n==0) {
            return 1;
        }
        if(n <= 0) {
            return 0;
        }
        if(cache.containsKey(n)) {
            return cache.get(n);
        } else {
            int result = climbStairs(n-2) + climbStairs(n-1);
            cache.put(n, result);
            return result;
        }
    }

    public String reverseString(char[] input, int i) {
        if(i<0) {
            return "";
        }
        return input[i] + reverseString(input, i-1);
    }

    public int recursion(int n) {
        if(n<=0) {
            return 0;
        }
        return 1 + recursion(n - 1) + recursion(n - 2);
    }
}
