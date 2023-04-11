package code.lh.algorithm.leetcode.day02.m1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Solution.
 * 想法是：将数值放到一个map中，然后去遍历；后来发现这种写法优点别扭，而且看着代码量很大，故而放弃了。
 */
public class Solution {
    /**
     * Exist boolean.
     *
     * @param board the board
     * @param word  the word
     * @return the boolean
     */
    public boolean exist(char[][] board, String word) {

        int m = board.length;
        int n = board[0].length;
        // 默认就是false的
        boolean[][] walk = new boolean[m][n];

        // 将数值放到一个map中，key是value
        Map<Character, ArrayList<boolean[][]>> boardMap = new HashMap<>();
        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                if (boardMap.containsKey(board[i][j])) {
                    ArrayList arrayList = boardMap.get(board[i][j]);
                    arrayList.add(walk[i][j]);
                    boardMap.put(board[i][j], arrayList);
                    continue;
                }
                ArrayList newArray = new ArrayList<>();
                newArray.add(walk[i][j]);
                boardMap.put(board[i][j], newArray);

            }
        }


        return true;
    }


    private static boolean backExist(boolean[][] walk, int m, int n, Map<Character, ArrayList<boolean[][]>> boardMap, String word, int searchIndex) {
        char c = word.charAt(searchIndex);
        if (! boardMap.containsKey(c)) {
            return false;
        } else {
            ArrayList<boolean[][]> booleans = boardMap.get(c);
            if (booleans.size() == 1) {

            }

        }



        return false;
    }


}
