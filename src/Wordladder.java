import java.util.*;

public class Wordladder {
    public static void main(String[] args) {
        System.out.println(new Wordladder().overlapOneChar("abc", "aaa"));
        System.out.println(new Wordladder().overlapOneChar("abc", "aac"));
        System.out.println(new Wordladder().overlapOneChar("abc", "aacd"));
        System.out.println(new Wordladder().overlapOneChar("abc", "abc"));

        List<String> input = new ArrayList<>();
//        input.add("hit");
        input.add("hot");
        input.add("dot");
        input.add("dog");
        input.add("hht");
        input.add("lot");
        input.add("log");
        //input.add("cog");
        new Wordladder().calc("hit", "cog", input, 0);
        new Wordladder().bfs();
        System.out.println(new Wordladder().calc2("cog", "hit", input));
    }

    private void calc(String match, String previous, List<String> input, int counter) {
        if(input.size() == 1 && input.contains(match)) {
            System.out.println("found................" + counter);
            return;
        }
        for(String i:input) {
            if(overlapOneChar(previous, i)) {
                calc(match, i, newListWithout(input, i), counter+1);
            }
        }
    }

    /**
     * aaa, aab, aac, aad, aae, aaf, abd
     * @param first
     * @param match
     * @param input
     * @return
     */
    private int calc2(String first, String match, List<String> input) {
        Queue<String> q = new ArrayDeque<>();
        q.add(first);
        int layer = 0;
        while(!q.isEmpty()) {
            String pop = q.remove();
            if(overlapOneChar(match, pop)) {
                return layer;
            } else {
                List<String> temp = new ArrayList<>();
                for(String i:input) {
                    if(overlapOneChar(i, pop)) {
                        q.add(i);
                        temp.add(i);
                    }
                }
                input.removeAll(temp);
            }
            ++layer;
        }
        return 0;
    }

    private void bfs() {
        Tree<Integer> root = Tree.of(10);
        Tree<Integer> rootFirstChild = root.addChild(2);
        Tree<Integer> depthMostChild = rootFirstChild.addChild(3);
        Tree<Integer> rootSecondChild = root.addChild(4);

        Queue<Tree<Integer>> q = new ArrayDeque<>();
        q.add(root);
        while(!q.isEmpty()) {
            Tree<Integer> tree = q.remove();
            if(tree.getValue() == 4) System.out.println("found" + tree);
            else q.addAll(tree.getChildren());
        }
    }

    private List<String> newListWithout(List<String> input, String toRemove) {
        List<String> newList = new ArrayList<>(input);
        newList.remove(toRemove);
        return newList;
    }

    private boolean overlapOneChar(String a, String b) {
        if(a.length() != b.length()) return false;
        String[] asplit = a.split("");
        String[] bsplit = b.split("");
        int count = 0;
        for(int i=0;i<asplit.length;i++) {
            if(!asplit[i].equals(bsplit[i])) {
                ++count;
            }
        }
        return count == 1;
    }


}
