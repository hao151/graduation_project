import { GameObject } from "./GameObject.js";
import { Wall } from './wall.js';

export class Map extends GameObject{
    constructor(ctx, parent){
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.rows = 13;
        this.cols = 13;
        this.walls = [];
    }

    start(){
        this.create_walls();
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }
    update()
    {
        this.update_size();
        this.render();
    }

    render()
    {
        const color_even = '#AAD751'  
        const color_odd = '#A2D149'  

        for (let r = 0; r < this.rows; r ++) {
            for (let c = 0; c < this.cols; c ++) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even
                }
                else {
                    this.ctx.fillStyle = color_odd
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }

    }

    create_walls(){
        const g = [];
        for (let r = 0; r < this.rows; r ++ ){
            g[r] = [];
            for (let c = 0; c < this.cols; c ++ ){
                g[r][c] = false;
            }
        }

        for (let r = 0; r < this.rows; r ++ )
        {
            for (let c = 0; c < this.cols; c ++ )
            {
                if (c == 0 || r == 0 || c == this.cols - 1 || r == this.rows - 1){
                    new Wall(r, c, this);
                    g[r][c] = true;
                }
            }
        }

        for (let cnt = 0; cnt < 24 / 2; cnt ++ ){
            for (let i = 0; i < 1000; i ++ ){
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if (g[r][c] || g[c][r]) continue; 
                if(r == this.rows - 2 && c == 1 || c == this.cols - 2 && r == 1) continue;
                g[r][c] = g[c][r] = true;
                new Wall(r, c, this);
                new Wall(c, r, this);
                break;
            }
        }


    }
}