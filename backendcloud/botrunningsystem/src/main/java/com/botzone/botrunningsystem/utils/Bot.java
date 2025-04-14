package com.botzone.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class Bot implements com.botzone.botrunningsystem.utils.BotInterface {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private boolean check_tail_increasing(int step) {  // 检验当前回合，蛇的长度是否增加
        if (step <= 6 ) return true;
        return step % 3 == 0;
    }

    //将用户操作序列转化为蛇身体，res[0]为蛇尾横纵坐标，res[res.size - 1]为蛇头横纵坐标
    public List<Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1);
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int i = 0; i < steps.length(); i ++ ) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing( ++ step)) {
                res.remove(0);
            }
        }
        return res;
    }

    @Override
    public Integer nextMove(String input) {
        /*
        输出的格式：
        地图编码#我的横坐标#我的纵坐标#我曾经的操作序列#对手横坐标#对手纵坐标#对手曾经的操作序列
         */
        String[] strs = input.split("#");

        //g为地图，0为可走空地，1为不可走障碍物
        int[][] g = new int[13][14];
        for (int i = 0, k = 0; i < 13; i ++ ) {
            for (int j = 0; j < 14; j ++, k ++ ) {
                if (strs[0].charAt(k) == '1') {
                    g[i][j] = 1;
                }
            }
        }
        //Sx为横坐标，Sy为纵坐标
        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);  //我的起点横纵坐标
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);  //对手起点的横纵坐标

        List<Cell> aCells = getCells(aSx, aSy, strs[3]); //贪吃蛇a的身体
        List<Cell> bCells = getCells(bSx, bSy, strs[6]); //贪吃蛇b的身体

        for (Cell c: aCells) g[c.x][c.y] = 1;
        for (Cell c: bCells) g[c.x][c.y] = 1;    // 将蛇的身体在地图中标识出来

        //Your Code Here
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        for (int i = 0; i < 4; i ++ ) {
            int x = aCells.get(aCells.size() - 1).x + dx[i];
            int y = aCells.get(aCells.size() - 1).y + dy[i];
            if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {
                return i;
            }
        }

        return 0;
    }
}
