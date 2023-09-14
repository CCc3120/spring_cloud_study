package com.bingo.test.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-01-11 14:06
 * @Version 1.0
 */
public class TestMain {

    static TestMain testMain = new TestMain();

    private boolean[][] line = new boolean[9][9]; // 每行中是否取到那个数
    private boolean[][] column = new boolean[9][9]; // 每列中是否取到那个数
    private boolean[][][] block = new boolean[3][3][9]; // 每个3*3小方块里是否取到那个数
    private List<int[]> spaces = new ArrayList<>(); // 用于存放空格字符的坐标
    private boolean valid = false; // 用于记录是否遍历完

    private static List<char[][]> result = new ArrayList<>();

    public void solveSudoku(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') {
                    spaces.add(new int[]{i, j});
                } else {
                    int x = board[i][j] - '0' - 1;
                    // 是数字的话,需要修改上面创建好的数组
                    line[i][x] = column[j][x] = block[i / 3][j / 3][x] = true;
                }
            }
        }
        dfs(board, 0);
    }

    public void dfs(char[][] board, int pos) {
        if (pos == spaces.size()) {
            // valid = true;

            char[][] copy = new char[9][9];
            for (int i = 0; i < board.length; i++) {
                copy[i] = new char[9];
                System.arraycopy(board[i], 0, copy[i], 0, 9);
            }

            result.add(copy);
            return;
        }
        int[] arr = spaces.get(pos);
        int i = arr[0], j = arr[1];
        // 从9个数字中选
        for (int digit = 0; digit < 9 && !valid; digit++) {
            if (!line[i][digit] && !column[j][digit] && !block[i / 3][j / 3][digit]) {
                line[i][digit] = column[j][digit] = block[i / 3][j / 3][digit] = true;
                board[i][j] = (char) (digit + '0' + 1);
                dfs(board, pos + 1);
                line[i][digit] = column[j][digit] = block[i / 3][j / 3][digit] = false;
            }
        }
    }

    public static void solveSudoku() {
        char[][] board = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'},
        };

        // char[][] board = {
        //         {'5', '.', '.', '.', '7', '.', '.', '.', '.'},
        //         {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
        //         {'.', '.', '8', '.', '.', '.', '.', '6', '.'},
        //         {'8', '.', '.', '.', '6', '.', '.', '.', '.'},
        //         {'4', '.', '.', '8', '.', '3', '.', '.', '.'},
        //         {'7', '.', '.', '.', '2', '.', '.', '.', '.'},
        //         {'.', '.', '.', '.', '.', '.', '2', '8', '.'},
        //         {'.', '.', '.', '4', '1', '9', '.', '.', '.'},
        //         {'.', '.', '.', '.', '8', '.', '.', '7', '.'},
        // };

        testMain.print(board);
        long s = System.currentTimeMillis();
        testMain.solveSudoku(board);
        System.out.println("耗时[" + (System.currentTimeMillis() - s) + "ms]，共有[" + result.size() + "]种解法");

        for (char[][] chars : result) {
            testMain.print(chars);
        }
    }

    public static void main(String[] args) {
        solveSudoku();
    }

    private void print(char[][] board) {
        for (char[] chars : board) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }

        System.out.println("----------------------");
    }
}