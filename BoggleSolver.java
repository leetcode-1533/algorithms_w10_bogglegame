import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.TrieSET;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;


public class BoggleSolver {

    private int size;
    private ArrayList<Bag<Integer>> dir;
    private BoggleBoard boggleboard;

    private TrieSET trieset;

    private int center(int i, int j) {
        return i * size + j;
    }

    private int up(int i, int j) {
        return (i - 1) * size + j;
    }

    private int down(int i, int j) {
        return (i + 1) * size + j;
    }

    private int left(int i, int j) {
        return i * size + j - 1;
    }

    private int right(int i, int j) {
        return i * size + j + 1;
    }

    private int[] index2ij(int index) {
        int i = index / size;
        int j = index - i * size;
        return new int[] {i, j};
    }

    private void dir_init() {
        int range = center(size - 1, size - 1);
        dir = new ArrayList<>(range + 2);
        Bag<Integer> master = new Bag<>();

        for (int index = 0; index < range + 1; index++) {
            Bag<Integer> cur_dir = new Bag<>();
            int[] ij = index2ij(index);
            int i = ij[0];
            int j = ij[1];

            if (i > 0)
                cur_dir.add(up(i, j));
            if (i < size - 1)
                cur_dir.add(down(i, j));
            if (j > 0)
                cur_dir.add(left(i, j));
            if (j < size - 1)
                cur_dir.add(right(i, j));

            dir.add(cur_dir);

            master.add(index); // master bag contain route to all bags
        }

        // master bag
        dir.add(master); // of index center(size, size) + 1
    }

    public BoggleSolver(String[] dictionary) {
        trieset = new TrieSET();
        for (String item : dictionary)
            trieset.add(item);
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        boggleboard = board;
        size = board.rows();
        dir_init();

        Set<String> container = new HashSet<>();
        dfs(new boolean[center(size - 1, size - 1) + 1], "", container, center(size - 1, size - 1) + 1);

        return container;
    }

    private void dfs(boolean[] visited, String str, Set<String> container, int index) {
        for (Integer opts : dir.get(index)) {
            if (!visited[opts]) {
                visited[opts] = true;
                int[] ij = index2ij(opts);
                str += boggleboard.getLetter(ij[0], ij[1]);
                if (trieset.longestPrefixOf(str) != null) {
                    if (trieset.contains(str))
                        container.add(str);
                    dfs(visited.clone(), str, container, opts);
                }
            }
        }
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        solver.getAllValidWords(board);
//        int score = 0;
//        for (String word : solver.getAllValidWords(board))
//        {
//            StdOut.println(word);
//            score += solver.scoreOf(word);
//        }
//        StdOut.println("Score = " + score);
    }
}
