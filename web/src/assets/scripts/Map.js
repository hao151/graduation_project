import { GameObject } from "./GameObject.js";
import { snake } from "./snakes.js";
import { Wall } from './wall.js';

export class Map extends GameObject{
    constructor(ctx, parent, store){
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0;
        this.rows = 13;
        this.cols = 14;
        this.walls = [];
        this.snakes = [
            new snake({id:0, color:"#4876EC", r:this.rows - 2, c:1}, this),
            new snake({id:1, color:"#F94848", r:1, c:this.cols - 2}, this),
        ];
    }

    start(){
        this.create_walls()
        this.add_listening_events();
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update()
    {
        this.update_size();
        
        if (this.check_move()) {
            this.next_step();
        }

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
        const g = this.store.state.pk.gamemap;

        for (let r = 0; r < this.rows; r ++ )
        {
            for (let c = 0; c < this.cols; c ++ )
            {
                if (g[r][c]) this.walls.push(new Wall(r, c, this));
            }
        }

        return true;


    }

    check_move(){  //两条蛇是否可以行进
        for (const snake of this.snakes){
            if (snake.direction === -1 || snake.status !== 'still') 
                return false;
        }

        return true;
    }


    add_listening_events() {
        this.ctx.canvas.focus();

        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", (e) => {
            const arrowKeys = ['ArrowLeft', 'ArrowUp', 'ArrowRight', 'ArrowDown'];
            if (arrowKeys.includes(e.key)) {
                e.preventDefault();
            }

            if (e.key === 'w') snake0.set_direction(0);
            else if (e.key === 'd') snake0.set_direction(1);
            else if (e.key === 's') snake0.set_direction(2);
            else if (e.key === 'a') snake0.set_direction(3);
            else if (e.key === 'ArrowUp') snake1.set_direction(0);
            else if (e.key === 'ArrowRight') snake1.set_direction(1);
            else if (e.key === 'ArrowDown') snake1.set_direction(2);
            else if (e.key === 'ArrowLeft') snake1.set_direction(3);
        });
    }
   
    check_over (body) {  //检查游戏是否结束
        for (const wall of this.walls) {
            if (body.r === wall.r && body.c === wall.c) {
                return true;
            }
        }
        for (const snake of this.snakes) {
            let k = snake.body.length;
            if (!snake.check_tail()) k --;

            for (let i = 0; i < k; i ++ ) {
                if (body.r === snake.body[i].r && body.c === snake.body[i].c) 
                    return true;
            }
        }

        return false;
    }



    next_step() {  // 让两条蛇进入下一回合
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }


    
}