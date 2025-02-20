package com.botzone.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer botId;
    private String code;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    private boolean check_tail_increasing(int step){  //当前蛇是否变长
        if (step <= 6) return true;
        return step % 3 == 0;

    }

    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        cells.add(new Cell(x,y));
        for (int d: steps){
            x += dx[d];
            y += dy[d];
            cells.add(new Cell(x,y));
            if (!check_tail_increasing(++step)){
                cells.remove(0);
            }
        }
        return cells;
    }

    public String stepsToString(){
        StringBuilder res = new StringBuilder();
        for (int d: steps){
            res.append(d);
        }
        return res.toString();
    }


}
