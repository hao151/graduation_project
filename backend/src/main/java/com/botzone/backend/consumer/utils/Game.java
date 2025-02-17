package com.botzone.backend.consumer.utils;

import java.util.Random;

public class Game {
    final private Integer rows, cols;
    final private int [][] g;
    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    public Game(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.g = new int[rows][cols];
    }

    public int[][] getG() {
        return g;
    }

    private boolean draw(){
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }

        Random random = new Random();

        for (int r = 0; r < this.rows; r ++ )
        {
            for (int c = 0; c < this.cols; c ++ )
            {
                if (c == 0 || r == 0 || c == this.cols - 1 || r == this.rows - 1){
                    g[r][c] = 1;
                }
            }
        }

        for (int cnt = 0; cnt < 26 / 2; cnt ++ ){
            for (int i = 0; i < 1000; i ++ ){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                if(r == this.rows - 2 && c == 1 || c == this.cols - 2 && r == 1) continue;
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
        //return true;

    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    public void createMap(){
        for (int i = 0; i < 1000; i++) {
            if (draw()) break;
        }
    }
}
